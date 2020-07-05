## Benchmarking Distributed Algorithms with OpenMP

In this repository, you'll find an implementation of various distributed
graph algorithms. Included are:

 - Shortest path tree (due to Bellman and Ford)
 - Shortest path tree (due to Dijkstra)
 - Byzantine consensus (due to Motwani and Raghavan)
 - Dominating set (due to Wattenhofer)
 - k-Committee (due to Wattenhofer)
 - Leader election (due to David Peleg)
 - Leader election (due to Hirschberg and Sinclair)
 - Leader election (due to Chang and Roberts)
 - Maximal independent set (due to Wattenhofer)
 - Minimum Spanning Tree (due to Gallager, Humblet and Spira)
 - Vertex coloring (due Wattenhofer)

### Compilation

To compile the benchmark suite, `cd` into the `src` directory and do:

```bash
$ cmake .
$ make
```

This should produce a binary for each algorithm.

### Running the executables

Each program can be run with no arguments. A default input will be generated and
used. However, you can also specify the input parameters - for example, in
dominating set, if you want to generate a graph of 200 vertices and 1000 edges,
you will execute the program as:

```bash
$ ./dominating_set 200 1000
```

The exact parameters for each problem varies - you can take a look at the source
code to know the exact specifications.

Alternatively, you can provide an input file. This file needs to adhere to the
particular format used in IMSuite. It will be automatically parsed and used as
the input data. For example, if you wanted to run `dominating_set` on the test
file `inputdominatingSet_16_-spar_min.txt`, you'll execute the program and
measure the average time per execution over 10000 iterations as:

```bash
$ ./dominating_set - /path/to/input/inputdominatingSet_16_-spar_min.txt 10000
```

### Running the benchmarking tool

In the `src/` directory, a script `run_tests` is included. This tool can be used
to automate the testing process. For example, if all the input test files from
IMSuite are present in `input/`, go into `src/` and run:

```bash
$ cd src
$ ./run_tests
```

This would automatically find all the input files and run each executable
against each corresponding input file. The wall clock duration is measured, and
and the exit code (which tells you whether the produced solution was correct or
incorrect) is noted. If there's an error, the corresponding problem and input
file is printed to screen.

Each input file's associated wall clock run time is stored in a file called
`output_times`. This is done regardless of whether the program exited with
return code 0 or not.

### Logging

Modify the value of `LOG_LEVEL` in `utils.h` to tune the level of logging:

 - `#define LOG_LEVEL 0` - All output is suppressed.
 - `#define LOG_LEVEL 1` - Only information text is printed to screen. This
   is just the final message (whether the computed solution for each problem is
   correct or not).
 - `#define LOG_LEVEL 2` - If something went wrong, a warning message is
   printed. This includes a more detailed message on what went wrong in an
   incorrect solution. Using this automatically prints all information text too.
 - `#define LOG_LEVEL 3` - All debug statements are printed too. You probably
   don't need this. This will automatically print all information and warning
   text as well.

You'll need to recompile the entire suite whenever you change the logging level.
