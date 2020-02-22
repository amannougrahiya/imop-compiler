int main() {
	double **u;
	double **w;
	unsigned long int _imopVarPre147;
	void *_imopVarPre148;
	_imopVarPre147 = 500 * sizeof(double *);
	_imopVarPre148 = malloc(_imopVarPre147);
	u = (double **) _imopVarPre148;
	unsigned long int _imopVarPre151;
	void *_imopVarPre152;
	_imopVarPre151 = 500 * sizeof(double *);
	_imopVarPre152 = malloc(_imopVarPre151);
	w = (double **) _imopVarPre152;
	int p;
	for (p = 0; p < 500; p++) {
		unsigned long int _imopVarPre155;
		void *_imopVarPre156;
		_imopVarPre155 = 500 * sizeof(double);
		_imopVarPre156 = malloc(_imopVarPre155);
		u[p] = (double *) _imopVarPre156;
		unsigned long int _imopVarPre159;
		void *_imopVarPre160;
		_imopVarPre159 = 500 * sizeof(double);
		_imopVarPre160 = malloc(_imopVarPre159);
		w[p] = (double *) _imopVarPre160;
	}
#pragma omp parallel
	{
		int i, j;
#pragma omp for nowait
		for (i = 1; i < 500 - 1; i++) {
			for (j = 1; j < 500 - 1; j++) {
				w[i][j] =
						(u[i - 1][j] + u[i + 1][j] + u[i][j - 1] + u[i][j + 1])
								/ 4.0;
			}
		}
#pragma omp barrier
		my_diff = 0.0;
#pragma omp for nowait
		for (i = 1; i < 500 - 1; i++) {
			for (j = 1; j < 500 - 1; j++) {
				double _imopVarPre167;
				double _imopVarPre168;
				_imopVarPre167 = w[i][j] - u[i][j];
				_imopVarPre168 = fabs(_imopVarPre167);
				if (my_diff < _imopVarPre168) {
					double _imopVarPre170;
					double _imopVarPre171;
					_imopVarPre170 = w[i][j] - u[i][j];
					_imopVarPre171 = fabs(_imopVarPre170);
					my_diff = _imopVarPre171;
				}
			}
		}
	}

}
