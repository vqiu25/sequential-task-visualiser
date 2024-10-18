# Sequential Task Visualiser

<p align="center">
  <img width="450" alt="stv" src="https://github.com/user-attachments/assets/19840a09-8653-4ce3-8b37-6050b9014c7e">
</p>

## About
Project for SOFTENG 306 (Software Engineering Design 2), a compulsory course for Part II Software Engineering students at the University of Auckland.

Sequential Task Visualiser is an application designed to visualise task scheduling across multiple processors. It helps illustrate the allocation of tasks to processors and provides insights into optimising scheduling strategies. The application uses the A* search algorithm to find optimal task schedules, employing the following pruning techniques:
* **Duplication Detection**: Discards states already processed with a shorter makespan using a closed hashmap
* **Processor Normalisation**: Prunes symmetric states by standardising processor assignments
* **Heuristic Scheduling**:  Prunes states exceeding a predefined upper bound based

This application features:
* **Metrics**: Input Metrics, and CPU and Memory Usage
* **Task Graph**: Visualisation of the Input Graph
* **Schedule Graph**: Visualisation of Partial Schedules
* **State Space Search Graph**: Visualisation of States explored by A*

## Images

<p align="center">
  <img width="450" alt="stv" src="https://github.com/user-attachments/assets/505f8437-9e98-47ef-84f8-27e1a17c3cc5">
</p>

## Video

https://github.com/user-attachments/assets/8895640e-cbba-4d27-80ba-7b97759924da

## Packaging

To package this application, feel free to use the Maven wrapper provided to execute:

`./mvnw clean package`

## Usage

To run this application (jar) execute:

```
java -jar scheduler.jar INPUT.dot P [-v] [-o=OUTPUT]

INPUT.dot       a task graph with integer weights in dot format in the same directory
P               no. of processors to schedule the input graph on

-o=OUTPUT       name output file OUTPUT.dot (default is INPUT-output.dot)

(also works without using '=' e.g. java -jar input-graph.dot 3 -p 4 -o output-graph -v)
```

## Team members

* Kevin Cheung
* Liam Parker
* Victor Qiu
* Ben Weng
* Nate Williamson
