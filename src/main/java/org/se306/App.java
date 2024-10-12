package org.se306;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.se306.algorithms.AStarSearch;
import org.se306.utils.GraphParser;
import org.se306.utils.SchedulerCommand;
import org.se306.visualisation.FxApp;
import org.slf4j.Logger;

import picocli.CommandLine;

/** Hello world! */
public class App {

  private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(App.class);

  public static void main(String[] args) {
    logArgs(args);
    SchedulerCommand command = new SchedulerCommand();
    CommandLine cmdLine = new CommandLine(command);
    if (cmdLine.execute(args) != 0) {
      LOGGER.error("Error parsing command line arguments");
      return;
    }
    logCommandInfo(command);

    AppState state = AppState.getInstance();
    try (InputStream inputStream = new FileInputStream(command.getInputFileName())) {
      state.setGraph(GraphParser.dotToGraph(inputStream));
    } catch (IOException e) {
      LOGGER.error("Error reading input file", e);
      return;
    }

    // run scheduler here, using command.getProcessors(), command.getCores(), and state.getGraph()
    AStarSearch.findSchedule(state.getGraph(), command.getProcessors());

    // execute visualisation if indicated
    if (command.toVisualise()) {
      // Note: this is blocking, but we're allowed to use extra threads for JavaFX
      state.setProcessorCount(command.getProcessors());
      state.setThreadCount(command.getCores());
      state.setTaskCount(state.getGraph().vertexSet().size());
      FxApp.launch(FxApp.class);
    }

    // output graph to dot file
    GraphParser.graphToDot(state.getGraph(), command.getOutputFileName());
  }

  private static void logArgs(String[] args) {
    LOGGER.debug("Arguments: {}", (Object) args);
  }

  private static void logCommandInfo(SchedulerCommand command) {
    LOGGER.info("Input file: {}", command.getInputFileName());
    LOGGER.info("Processors: {}", command.getProcessors());
    LOGGER.info("Cores: {}", command.getCores());
    LOGGER.info("Output file: {}", command.getOutputFileName());
    LOGGER.info("Visualise: {}", command.toVisualise());
  }
}
