# SE306 Project 2 - Team 18 - :D

This project uses Maven as a build tool to manage dependencies and package the application. 

To package this application feel free to use the Maven wrapper provided to execute:

`./mvnw clean package` or `.\mvnw.cmd clean package` (for Windows computers)

## Usage

To run this application (jar) execute:

```
java -jar scheduler.jar INPUT.dot P [-v] [-o=OUTPUT] [-p=N]

INPUT.dot       a task graph with integer weights in dot format in the same directory
P               no. of processors to schedule the input graph on

-o=OUTPUT       name output file OUTPUT.dot (default is INPUT-output.dot)
-p=N            no. of cores to use for execution in parallel (default is sequential)
-v              visualise the search

(also works without using '=' e.g. java -jar input-graph.dot 3 -p 4 -o output-graph -v)
```

## Team members

* Kevin Cheung
* Liam Parker
* Victor Qiu
* Ben Weng
* Nate Williamson
