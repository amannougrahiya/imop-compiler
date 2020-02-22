
#define V 6
#define n 3

void floydWarshall_div(int dist[V/n][V/n])
{

    int i, j, k;

    for ( k = 0 ; k < V/n ; k++)
    {
        for ( i = 0 ; i < V/n ; i++)
        {
            for ( j = 0 ; j < V/n ; j++)
            {
                if ( dist[i][k] + dist[k][j] < dist[i][j] ){
                    dist[i][j] = dist[i][k] + dist[k][j];
                }
            }
        }
        // printf("AAA\n");
    }
 
}
 


int main()
{

    int graph[V][V];

    int i,j,x,y;

    for(i = 0 ; i < V ; i++)
    {
        for(j = 0 ; j < V ; j = j + V/n*V/n)
        {

            floydWarshall_div(&graph[i][j]);
        }

    }


    return 0;
}
