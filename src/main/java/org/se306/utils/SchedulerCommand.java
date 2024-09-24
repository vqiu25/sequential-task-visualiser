package org.se306.utils;

import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

public class SchedulerCommand implements Runnable {

  // TODO: sort out relative paths or something
  @Parameters(
      index = "0",
      paramLabel = "INPUT.dot",
      description = "a task graph with integer weights in dot format")
  private String inputFileName;

  @Parameters(
      index = "1",
      paramLabel = "P",
      description = "no. of processors to schedule the input graph on")
  private int processors;

  @Option(
      names = "-p",
      paramLabel = "N",
      description = "no. of cores to use for execution in parallel (default is sequential)",
      defaultValue = "1")
  private int cores;

  @Option(names = "-v", description = "visualise the search", defaultValue = "false")
  private boolean visualise;

  @Option(
      names = "-o",
      paramLabel = "OUTPUT",
      description = "name output file OUTPUT.dot (default is INPUT-output.dot)")
  private String outputFileName;

  @Override
  public void run() {
    // TODO: parse/validate input file name when reading?

    if (outputFileName == null) {
      outputFileName =
          String.format(
              "%s-output.dot", inputFileName.substring(0, inputFileName.lastIndexOf('.')));
    } else {
      // TODO: validate output file name
      outputFileName = String.format("%s.dot", outputFileName);
    }
  }

  public String getInputFileName() {
    return inputFileName;
  }

  public int getProcessors() {
    return processors;
  }

  public int getCores() {
    return cores;
  }

  public boolean checkVisualise() {
    return visualise;
  }

  public String getOutputFileName() {
    return outputFileName;
  }
}
