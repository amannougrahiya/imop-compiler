#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <errno.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <inttypes.h>
#include <math.h>
#include "msr.h"

#define MSR_RAPL_POWER_UNIT            0x606
#define MSR_PKG_ENERGY_STATUS          0x611
#define MSR_DRAM_ENERGY_STATUS         0x619

#define ENERGY_UNIT_OFFSET             0x08
#define ENERGY_UNIT_MASK               0x1f00

#define MAX_NUM_CORES                  1024
#define MAX_NUM_PACKAGES               1024

#define CPU_SANDYBRIDGE                42
#define CPU_SANDYBRIDGE_EP             45
#define CPU_IVYBRIDGE                  58
#define CPU_IVYBRIDGE_EP               62
#define CPU_HASWELL                    60
#define CPU_HASWELL_ULT                69
#define CPU_HASWELL_GT3E               70
#define CPU_HASWELL_EP                 63
#define CPU_BROADWELL                  61
#define CPU_BROADWELL_GT3E             71
#define CPU_BROADWELL_EP               79
#define CPU_BROADWELL_DE               86
#define CPU_SKYLAKE                    78
#define CPU_SKYLAKE_HS                 94
#define CPU_SKYLAKE_X                  85
#define CPU_KNIGHTS_LANDING            87
#define CPU_KNIGHTS_MILL               133
#define CPU_KABYLAKE_MOBILE            142
#define CPU_KABYLAKE                   158

int open_msr_fd(int core)
{
    char filename[256];
    sprintf(filename, "/dev/cpu/%d/msr", core);

    int fd = open(filename, O_RDONLY);
    if (fd < 0) {
        fprintf(stderr, "fatal error: cannot open %s: %s\n", filename, strerror(errno));
        exit(1);
    }

    return fd;
}

long long read_msr(int fd, int offset)
{
    uint64_t data;
    pread(fd, &data, sizeof(uint64_t), offset);

    return (long long) data;
}

int num_cores[MAX_NUM_PACKAGES];
int first_core[MAX_NUM_PACKAGES];

int total_packages = -1;

void get_num_packages()
{
    char filename[256];

    for (int package = 0; package < MAX_NUM_PACKAGES; package++) {
        num_cores[package] = 0;
        first_core[package] = -1;
    }

    int total_cores = 0;
    total_packages = 0;
    for (int core = 0; core < MAX_NUM_CORES; core++) {
        sprintf(filename, "/sys/devices/system/cpu/cpu%d/topology/physical_package_id", core);

        FILE* f = fopen(filename, "r");
        if (f == NULL)
            break;

        int package;
        fscanf(f, "%d", &package);
        fclose(f);

        num_cores[package]++;
        total_cores++;

        if (first_core[package] == -1) {
            first_core[package] = core;
            total_packages++;
        }
    }
}

double cpu_energy_units[MAX_NUM_PACKAGES];
double dram_energy_units[MAX_NUM_PACKAGES];

void init_energy_units(int total_packages)
{
    long long result;
    for (int package = 0; package < total_packages; package++) {
        int fd = open_msr_fd(first_core[package]);

        result = read_msr(fd, MSR_RAPL_POWER_UNIT);

        cpu_energy_units[package] = pow(0.5, (double) ((result >> 8) & 0x1f)) * 1e9;

        if (CPU_USED >= CPU_HASWELL)
            dram_energy_units[package] = pow(0.5, (double) 16) * 1e9;
        else
            dram_energy_units[package] = cpu_energy_units[package];

        close(fd);
    }
}

double cpu_energy_before[MAX_NUM_PACKAGES];
double dram_energy_before[MAX_NUM_PACKAGES];

void read_energy_usage(int total_packages, double* cpu_energy, double* dram_energy)
{
    long long result;
    for (int package = 0; package < total_packages; package++) {
        int fd = open_msr_fd(first_core[package]);

        result = read_msr(fd, MSR_PKG_ENERGY_STATUS);
        cpu_energy[package] = ((double) result) * cpu_energy_units[package];

        result = read_msr(fd, MSR_DRAM_ENERGY_STATUS);
        dram_energy[package] = ((double) result) * dram_energy_units[package];

        close(fd);
    }
}

void init_energy_measure()
{
    if (total_packages == -1) {
        get_num_packages();
        init_energy_units(total_packages);
    }

    read_energy_usage(total_packages, cpu_energy_before, dram_energy_before);
}

double total_energy_used()
{
    if (total_packages == -1) {
        fprintf(stderr, "fatal error: total_energy_used() called before init_energy_measure()\n");
        exit(1);
    }

    double cpu_energy_after[MAX_NUM_PACKAGES];
    double dram_energy_after[MAX_NUM_PACKAGES];
    double total_energy = 0;

    read_energy_usage(total_packages, cpu_energy_after, dram_energy_after);

    for (int package = 0; package < total_packages; package++) {
        total_energy += cpu_energy_after[package] - cpu_energy_before[package];
        total_energy += dram_energy_after[package] - dram_energy_before[package];
    }
    
    return total_energy;
}
