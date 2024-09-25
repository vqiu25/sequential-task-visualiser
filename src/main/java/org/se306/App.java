package org.se306;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.se306.domain.Task;
import org.se306.utils.GraphParser;
import org.se306.utils.SchedulerCommand;
import org.slf4j.Logger;
import picocli.CommandLine;

/** Hello world! */
public class App {

  private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(App.class);

  public static void main(String[] args) {
    SchedulerCommand command = new SchedulerCommand();
    CommandLine cmdLine = new CommandLine(new SchedulerCommand());
    if (cmdLine.execute(args) != 0) {
      return;
    }

    LOGGER.info("Input file: {}", command.getInputFileName());
    LOGGER.info("Processors: {}", command.getProcessors());
    LOGGER.info("Cores: {}", command.getCores());
    LOGGER.info("Output file: {}", command.getOutputFileName());
    LOGGER.info("Visualise: {}", command.checkVisualise());

    Graph<Task, DefaultWeightedEdge> graph = GraphParser.dotToGraph("dot/Nodes_7_OutTree.dot");
    GraphParser.graphToDot(graph, "test/App.dot");
  }
}
