#include <stdio.h>
#include <omp.h>
#include <stdlib.h>
#include <string.h>

#include "ompdist/election.h"
#include "ompdist/vector.h"
#include "ompdist/utils.h"
#include "ompdist/queues.h"
#include "ompdist/msr.h"
#include "config.h"

typedef struct {
    int starter_label;
    int hops_left;
    int direction;
    int direction_changed;
    int stop_initiating;
} message;

/**
 * generate_send_messages - Creates a new phase's initial messages.
 *
 * @processes: the processes themselves
 * @l:         current phase number
 * @N:         total number of processes
 * @send_ql:   a queuelist object that's going to store this round's messages
 */
void generate_send_messages(process* processes,
                   int l,
                   int N,
                   queuelist* send_ql) {
    DEBUG("Generating 2*%d messages for %d processes\n", N, N);

    #pragma omp parallel for schedule(SCHEDULING_METHOD)
    for (int i = 0; i < N; i++) {
        process* p = processes+i;

        /**
         * If this node has been asked (or decided) to not send out any more
         * original messages (all nodes will always be up for propagating; but
         * creating new messages is a privilege that nodes might lose), don't
         * do anything.
         */
        if (p->status == -1)
            continue;

        message to_right = {i, 1 << l,  1, 0, 0};
        message to_left  = {i, 1 << l, -1, 0, 0};

        enqueue(send_ql, i, &to_right);
        enqueue(send_ql, i, &to_left);
    }
}

/**
 * propagate_messages - Propagates incomplete messages through the ring.
 *
 * @processes: the processes themselves
 * @l:         current phase number
 * @N:         total number of processes
 * @send_ql:   a queuelist object that's going to store this round's messages
 * @recv_ql:   a temporary queuelist object that will collect and transmit
 */
void propagate_messages(process* processes,
                        int l,
                        int N,
                        queuelist* send_ql,
                        queuelist* recv_ql) {
    DEBUG("propagating messages on phase %d\n", l);

    #pragma omp parallel for schedule(SCHEDULING_METHOD)
    for (int i = 0; i < N; i++) {
        DEBUG("i = %d\n", i);
        process* p = processes+i;

        while (!is_ql_queue_empty(send_ql, i)) {
            message* m = dequeue(send_ql, i);
            DEBUG("m->starter_label = %d\n", m->starter_label);

            /**
             * If the starter_label is the current node, then ther are two
             * possibilities:
             *  - this node has returned back home; increment status and be
             *    done with that message
             *  - this node never turned direction; this node is the winner
             *    so make this the leader
             * 
             * Otherwise the message reached the far end. It's time to change
             * direction, refresh the number of hops_left and go back.
             */
            if (m->starter_label == i && m->hops_left != (1 << l)) {
                if (m->stop_initiating)
                    p->status = -1;
                else {
                    if (m->direction_changed)
                        p->status++;
                    else {
                        p->status = 3;
                        break;
                    }
                }
                continue;
            }

            if (m->hops_left == 0) {
                DEBUG("zero hops left\n");
                m->hops_left = 1 << l;
                m->direction *= -1;
                m->direction_changed = 1;
            }

            /**
             * Make sure this message is good enough to propagate. A message
             * passes through a node only if the origin is not lesser than
             * the current node's label. A message that passes through a node
             * in one direction _will_ pass through the same node when it's
             * coming back.
             *
             * When a node passes a message along, it can no longer win.
             * Therefore, it'll mark itself as status = -1, meaning that
             * it'll no longer start messages.
             *
             * If a message is not passed through (m->starter_label < i) then
             * the origin must be asked to not pass messages anymore.
             */
            if (m->starter_label < i) {
                /**
                 * Of the (1 << l) hops the message intended to complete, it
                 * has `hops_left` left, implying that it took
                 * `(1 << l) - hops_left` hops to get here. It'll take exactly
                 * the same number to go back to its origin.
                 */
                m->hops_left = (1 << l) - m->hops_left;
                m->direction *= -1;
                m->direction_changed = 1;
                m->stop_initiating = 1;
                continue;
            }
            else {
                m->hops_left--;
                p->status = -1;
            }
            
            int next_label = (N + i + m->direction) % N;
            enqueue(recv_ql, next_label, m);
        }
    }

    /**
     * At this point, every queue in `send_ql` must be empty (since that's the
     * only way each process breaks out of its while loop. The `dequeue`
     * operation will automatically set the number of elements to zero after
     * the last element.
     */

    #pragma omp parallel for schedule(SCHEDULING_METHOD)
    for (int i = 0; i < N; i++) {
        process* p = processes+i;

        while (!is_ql_queue_empty(recv_ql, i)) {
            enqueue(send_ql, i, dequeue(recv_ql, i));
        }
    }

    /**
     * At this point, every queue in `recv_ql` must be empty just like the last
     * time when `send_ql` was empty.
     */
}

/**
 * check_status - Iterates through every process and checks if there are any
 * more phases to go through.
 *
 * @processes: The processes themselves.
 * @N:         The number of processes
 * @send_ql:   A pointer to the to-send queuelist
 *
 * Returns:
 *   2: This phase is done, but no leader has been found yet; phase++
 *   1: The phase isn't over; some messages haven't propagated completely yet
 * <=0: Negative of this number is the leader; all phases are complete
 */
int check_statuses(process* processes,
                   int N,
                   queuelist* send_ql) {
    for (int i = 0; i < N; i++) {
        process* p = processes+i;

        /**
         * If p->status is 3, this node has decided to be the leader.
         */
        if (p->status == 3)
            return -i;
    }

    for (int i = 0; i < N; i++) {
        if (!is_ql_queue_empty(send_ql, i))
            return 1;
    }

    return 2;
}

/**
 * debug_display_queuelist - Prints debug information to stdout. Disabled if
 * `LOG_LEVEL` is less than 3.
 *
 * @ql: A pointer to a queuelist object
 */
void debug_display_queuelist(queuelist* ql) {
    DEBUG("displaying the queuelist\n");
    for (int i = 0; i < ql->N; i++) {
        vector* v = ql->queues[i];
        for (int j = ql->front[i]; j < v->used; j++) {
            message* m = elem_at(v, j);
            DEBUG("%d: {%d %d %2d %d %d}\n", i,
                                             m->starter_label,
                                             m->hops_left,
                                             m->direction,
                                             m->direction_changed,
                                             m->stop_initiating);
        }
    }
}

/**
 * Hirshberg-Sinclair algorithm for leader election. Source: Distributed
 * Algorithms (lecture notes for MIT's 6.852, fall 1992) by Nancy A. Lynch
 * and Boaz Patt-Sinclair - Section 2.1.1 Algorithm 2
 */
int main(int argc, char* argv[]) {
    int N;
    process* processes;

    int iterate;
    int iterations = 1;

    if ((iterate = input_through_argv(argc, argv))) {
        FILE* in = fopen(argv[2], "r");

        fscanf(in, "%d", &N);

        processes = generate_nodes(N);

        for (int i = 0; i < N; i++) {
            int x;
            fscanf(in, "%d", &x);
            processes[i].id = processes[i].leader = processes[i].send = x;
        }

        sscanf(argv[3], "%d", &iterations);
    }
    else {
        N = 16;

        if (argc > 1)
            sscanf(argv[1], "%d", &N);

        processes = generate_nodes(N);
    }

    long long duration = 0;
    double total_energy = 0;

    int verification;

    for (int i = 0; i < iterations; i++) {
        process* ps = generate_nodes(N);

        memcpy(ps, processes, sizeof(process)*N);

        /**
         * We need two different queue lists for the following reason. Say there
         * are two nodes A and B. For the sake of argument, say B has no messages
         * to pass on. B will immediately exit its loop. Say A wants to enqueue
         * a message into B's queue. Since B is already done, it'll never see this
         * message. To avoid this, we have two queues - messages will be picked
         * up from the send_ql, processed and added to the destination's recv_ql.
         * Then, after all threads complete, messages are copied from recv_ql
         * and simply copied to send_ql.
         */
        queuelist* recv_ql = new_queuelist(N, sizeof(message));
        queuelist* send_ql = new_queuelist(N, sizeof(message));

        begin_timer();
        init_energy_measure();

        int chosen_id = -1;

        int l = 0;

        int finished = 0;
        while (!finished) {
            l += 1;
            DEBUG("starting phase %d\n", l);

            generate_send_messages(ps, l, N, send_ql);

            while (1) {
                propagate_messages(ps, l, N, send_ql, recv_ql);

                int status = check_statuses(ps, N, send_ql);

                DEBUG("status = %d\n", status);

                /**
                 * Not all messages have propagated fully. Keep going.
                 */
                if (status == 1)
                    continue;

                /**
                 * All messages were processed, but there's no clear leader. Go
                 * for another phase.
                 */
                if (status == 2)
                    break;

                /**
                 * A leader has been chosen! Make sure everybody knows this
                 * and exit.
                 */
                if (status <= 0) {
                    chosen_id = -status;
                    set_leader(ps, N, chosen_id);
                    finished = 1;
                    break;
                }
            }
        }

        total_energy += total_energy_used();
        duration += time_elapsed();

        INFO("chosen leader: %d\n", chosen_id);
        INFO("number of phases: %d\n", l);

        free_queuelist(send_ql);
        free_queuelist(recv_ql);
        free(ps);
    }

    if (iterate)
        printf("%.2lf %.2lf\n", ((double) duration) / iterations, total_energy / iterations);

    return 0;
}
