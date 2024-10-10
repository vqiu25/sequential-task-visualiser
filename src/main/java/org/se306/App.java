package org.se306;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
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
    state.setCommand(command);
    try (InputStream inputStream = new FileInputStream(command.getInputFileName())) {
      state.setGraph(GraphParser.dotToGraph(inputStream));
    } catch (IOException e) {
      LOGGER.error("Error reading input file", e);
      return;
    }

    // execute scheduler in visualisation mode if indicated
    if (command.toVisualise()) {
      LOGGER.info("Visualisation starting...");
      state.setRunning(false); // user starts the algorithm using the 'play' button instead
      new Thread(() -> FxApp.launch(FxApp.class)).start();
    } else {
      state.setRunning(true);
    }

    LOGGER.info("Scheduler running...");
    Instant startTime = Instant.now();

    // TODO: implement so that the algorithm only runs when the user sets app state running to true
    // TODO: implement parallelisation (use command.getCores() to determine number of threads)
    AStarSearch.findSchedule(state.getGraph(), command.getProcessors());

    Instant endTime = Instant.now();
    Duration duration = Duration.between(startTime, endTime);
    LOGGER.info("Scheduler finished in {}ms", duration.toMillis());

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
