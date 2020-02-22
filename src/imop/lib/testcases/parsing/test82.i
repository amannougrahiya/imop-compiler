void aman() {
	int dist[6/3][6/3];
	int dist2[2];
	if (1) {
		aman();
	}
}
void floydWarshall_div(int dist[6/3][6/3])
{
    int i, j, k;
    for ( k = 0 ; k < 6/3 ; k++)
    {
        for ( i = 0 ; i < 6/3 ; i++)
        {
            for ( j = 0 ; j < 6/3 ; j++)
            {
                if ( dist[i][k] + dist[k][j] < dist[i][j] ){
                    dist[i][j] = dist[i][k] + dist[k][j];
                }
            }
        }
    }
}
int main()
{
    int graph[6][6];
    int i,j,x,y;
    for(i = 0 ; i < 6 ; i++)
    {
        for(j = 0 ; j < 6 ; j = j + 6/3*6/3)
        {
            floydWarshall_div(&graph[i][j]);
        }
    }
    return 0;
}
