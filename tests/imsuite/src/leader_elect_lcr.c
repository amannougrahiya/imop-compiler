#include <stdio.h>
#include <omp.h>
#include <string.h>
#include <stdlib.h>

#include "ompdist/election.h"
#include "ompdist/utils.h"
#include "ompdist/msr.h"
#include "config.h"

/**
 * receive_leaders - Sets the next process' received leader to be the current
 * process' candidate.
 *
 * @processes: the list of processes
 * @n:         number of processes
 */
void receive_leaders(process* processes, int N) {
    #pragma omp parallel for schedule(SCHEDULING_METHOD)
    for (int i = 0; i < N; i++) {
        int next = (i+1) % N;
        processes[next].received = processes[i].send;
    }
}

/**
 * determine_leaders - Based on the received ID of potential leaders, decide
 * whether to propagate or not.
 *
 * @processes: the list of processes
 * @n:         number of processes
 */
void determine_leaders(process* processes, int N) {
    #pragma omp parallel for schedule(SCHEDULING_METHOD)
    for (int i = 0; i < N; i++) {
        if (processes[i].received > processes[i].leader) {
            processes[i].send = processes[i].received;
            processes[i].leader = processes[i].received;
        }
        else if (processes[i].received == processes[i].id) {
            processes[i].leader = processes[i].id;
            processes[i].status = 1;
        }
    }
}

/**
 * identify_leader - Identifies a leader for the processes by looking at each
 * process' status field.
 *
 * @processes: the list of processes
 * @N:         number of processes
 *
 * Returns `chosen_id`, the id of the chosen leader.
 */
int identify_leader(process* processes, int N) {
    int chosen_id = -1;

    #pragma omp parallel for schedule(SCHEDULING_METHOD)
    for (int i = 0; i < N; i++) {
        if (processes[i].status == 1) {
            chosen_id = i;
            // this will happen at most once
        }
    }

    return chosen_id;
}

/**
 * Uses the distributed leader elect algorithm due to Chang and Roberts (1979)
 * that is used to determine the leader of a ring of nodes connected to one
 * another.
 *
 * Links:
 *   - https://en.wikipedia.org/wiki/Chang_and_Roberts_algorithm
 *   - http://www.cse.iitm.ac.in/~augustine/cs6100_even2012/slides/13_LeaderElection.pdf
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

        begin_timer();
        init_energy_measure();

        for (int r = 0; r < N; r++) {
            receive_leaders(ps, N);
            determine_leaders(ps, N);
        }

        int chosen_id = identify_leader(ps, N);
        if (chosen_id == -1) {
            INFO("Incorrect: no solution found.\n");
            verification = 1;
            break;
        }

        set_leader(ps, N, chosen_id);

        INFO("Chosen leader: %d\n", chosen_id);

        total_energy += total_energy_used();
        duration += time_elapsed();

        free(ps);
        verification = 0;
    }

    if (iterate)
        printf("%.2lf %.2lf\n", ((double) duration) / iterations, total_energy / iterations);

    return verification;
}
