int main() {
#pragma omp parallel
	{
		if (!omp_get_thread_num()) {
#pragma omp critical
			{
				printf("Started 1\n");
				sleep(2);
				printf("Ended 1\n");
			}
		} else {
#pragma omp critical
			{
				printf("Started 2\n");
				sleep(2);
				printf("Ended 2\n");
			}
		}
	}
}
