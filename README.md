# IMOP: IIT Madras OpenMP Framework #
##### PACE Lab, Department of Computer Science and Engineering, IIT Madras, India #####
***
#### About
This is the public-release beta version of IMOP. This document explains how to setup and use IMOP.
For a quick tutorial on IMOP, as presented at ACM CGO 2020, please visit IMOP's webpage at [bit.ly/imop-cgo20](https://bit.ly/imop-cgo20), where you can also find two tech-reports explaining the details of IMOP.

#### Dependencies
- Java SE 8 (or higher)
- Apache Ant(TM) version 1.9.* (or higher)
- The Z3 Theorem Prover (*see below*)

#### Setting up Z3 Solver
In order to use SAT solver modules (like the one used for performing array-dependence checks to enable field-sensitivity), we need to install the Z3 solver (*build it with `--java` switch to enable Java bindings*). Instructions for the same can be found [here](https://github.com/Z3Prover/z3#z3-bindings).
For quick reference, the installations instructions are reiterated below:
*Note that the script `mk_make.py` can also take `--prefix=<install-path>` option to enable local installation of Z3.*
```
git clone https://github.com/Z3Prover/z3.git
cd z3
python scripts/mk_make.py --java
cd build
make    
make install
```
Note that the build process may take about 30-40 minutes on an Intel Core(TM) i7 with 16 GB RAM.
After successful installation of Z3, set the environment variable `Z3HOME` to point to the `z3` folder; this variable is used by the scripts in `runner` folder, which are used to execute IMOP on input OpenMP C programs.

Since IMOP may rely on certain native libraries created during the build of Z3, we need to specify the path to those libraries to the `java` runtime while using IMOP. There are two options for achieving the same:
1. Set the library-path property as `-Djava.library.path=<install-path>/build` during invocation of the JRE (`java`).
**OR**
2. Alternatively, update the `PATH` environment variable, by adding the following suffix: `<install-path>/build`.

#### Setting up IMOP

Following are the simple steps to build and set up IMOP.
```
git clone https://github.com/amannougrahiya/imop-compiler.git
cd imop-compiler
ant clean
ant
```
After the build process is complete, set the environment variable `IMOPHOME` to point to the `imop-compiler` folder; this is used by the scripts in `runner`.

Note that for the purpose of compilation, the ant-script automatically adds `third-party-tools/com.microsoft.z3.jar` to classpath.
These commands should compile the project and generate the class files in `bin` folder.

#### Testing IMOP via command-line interface ####
Two sample scripts to run IMOP on a set of input OpenMP C programs can be found in the `imop-compiler/runner` folder. One of the scripts, `pre-and-post.sh` is used to execute `imop.Main` twice -- first in *prepass* mode, and then in *postpass* mode -- on all the `*.c` files available in a folder provided as the first argument to the script.
Whereas, the other script, `post-pass-only.sh`, is used to execute `imop.Main` on all the `*.i` (already-prepassed) files, in postpass mode, from the folder provided as the first argument to the script.

The error-output (STDERR) of each invocation of these scripts is saved in the folder `/tmp` (and then dumped to the screen), whereas the STDOUT directly gets printed on the screen. All the output files representing the output program(s), are created at the folder `imop-compiler/output-dump`.
Note that these scripts already set the `-cp` and `-Djava.library.path` properties while invoking `java` on `imop.Main`. For invoking `java` manually on a single `*.c` or `*.i` file, one can use the invocation syntax provided within these scripts. Kindly refer to the scripts for more detail.

*Important: Note that a folder named `output-dump` will be created in `imop-compiler` by IMOP, if not already present -- this folder is used by default to save all the output-program (or intermediate-program) files that are dumped by IMOP during its run.*

#### Using IMOP in Eclipse IDE *(Optional, but recommended)*
Following are the steps to set up the project in Eclipse.
1. Create a *New Java Project* in Eclipse, on top of the same location where IMOP's repository has been cloned. Let's assume that the name of this project is `imop-compiler`.
2. Perform the following changes in the *Properties* of the project (Right-click on the project's name in the *Package Explore*, and select *Properties*):
    * In *Java Build Path* --> *Libraries* --> *Add JARs*, select all the JARs present at `imop-compiler/third-party-tools`.
    * Next, we need to set up a default run-configuration, say *Main*.
    If a default configuration doesn't already exist, create one using *Run/Debug Settings* --> *New...* --> *Java Application* --> *Edit Configuration*. The *Name* field can be set as *Main* (or any other name of your choice). Set `imop.Main` as the *Main class*.
    In our default configuration, we need to make the following changes:
        * In *Arguments* --> *Working Directory*, select *Other*, and set it as `${workspace_loc:imop-compiler/bin}`.
        * In *Arguments* --> *VM Arguments*, add the following: `-ea -Xms512M -Xmx42048M` (Feel free to use smaller values for smaller projects.)
    
#### Testing IMOP via Eclipse ####
All our sample client codes are written in `imop.Main`. In `Main.main()`, parsing and some pre-processing of the input file is done via a call to `Program.parseNormalizeInput(args)`, where `args` are the command-line arguments received by `main()`.
If we are running our project from within Eclipse, and provide no command-line arguments explicitly (by setting them in the Run/Debug configurations, for example), then IMOP will assume that the file-path string is the one that is returned by `Program.defaultCommandLineArguments()`. This method also sets the values for various global flags, which are used whenever we are running the project from within the Eclipse IDE (i.e., when no command-line arguments are provided). An explanation of these flags can be found in **Section 1** of the "Part B: Code Review Document". Further explanation about the method `Program.parseAndNormalize(args)` can be found in **Section 2** of the same document.

There are various methods in `Main.main()` that are used to test different components of the proposed optimizations. For more information, kindly check their respective documentations/notes.

#### Verifying the output program ####
For small- and medium-sized programs, the output programs can be manually compared with the original/intermediate state of input program. In case of NPB benchmarks, we test the correctness of an output file by pasting it to the appropriate location within `imop-compiler/NPB3.0-omp-C-master`, after renaming it to the required `*.c` file, making it, and then executing it.

Note that all intermediate and final output files are generated at `imop-compiler/output-dump`. 
   
    

***
***
