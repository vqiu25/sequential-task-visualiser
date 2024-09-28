package org.se306.algorithms;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.jupiter.api.Test;
import org.se306.GraphTester;
import org.se306.domain.Task;
import org.se306.utils.GraphParser;

public class ValidScheduleTest {

  @Test
  public void testGraph2Processors5() {
    Graph<Task, DefaultWeightedEdge> actual = GraphParser
        .dotToGraph(getClass().getResourceAsStream("input/Graph2.dot"));
    ValidSchedule.findValidSchedule(actual, 5);

    Graph<Task, DefaultWeightedEdge> expected = GraphParser
        .dotToGraph(getClass().getResourceAsStream("expected/Graph2_5.dot"), true);

    GraphTester.assertGraphEquals(expected, actual); // Doesn't work yet
  }
}
