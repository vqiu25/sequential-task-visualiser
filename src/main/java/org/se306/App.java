package org.se306;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.se306.domain.Task;
import org.se306.utils.GraphParser;
import org.slf4j.Logger;

/** Hello world! */
public class App {

  private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(App.class);

  public static void main(String[] args) {
    LOGGER.info("Arguments: {}", (Object) args);

    // SchedulerCommand command = new SchedulerCommand();
    // CommandLine cmdLine = new CommandLine(command);
    // if (cmdLine.execute(args) != 0) {
    //   return;
    // }

    // LOGGER.info("Input file: {}", command.getInputFileName());
    // LOGGER.info("Processors: {}", command.getProcessors());
    // LOGGER.info("Cores: {}", command.getCores());
    // LOGGER.info("Output file: {}", command.getOutputFileName());
    // LOGGER.info("Visualise: {}", command.toVisualise());

    // if (command.toVisualise()) {
    //   FxApp.launch(FxApp.class);
    // }

    Graph<Task, DefaultWeightedEdge> graph = GraphParser.dotToGraph("dot/Nodes_7_OutTree.dot");
    GraphParser.graphToDot(graph, "App.dot");
  }
}
