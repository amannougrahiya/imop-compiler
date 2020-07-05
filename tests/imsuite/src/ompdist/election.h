#ifndef _ELECTION_H_
#define _ELECTION_H_

typedef struct {
    int id;             // ID of the node
    int send;           // the ID to send along
    int received;       // the ID it just received this round
    int status;         // whether or not this node is the leader
    int leader;         // at completion, this will have the leader ID
} process;

process* generate_nodes(int);

void set_leader(process*, int, int);

#endif
