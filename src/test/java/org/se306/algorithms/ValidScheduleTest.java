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
    Graph<Task, DefaultWeightedEdge> actual =
        GraphParser.dotToGraph(getClass().getResourceAsStream("input/Graph2.dot"));
    ValidSchedule.findValidSchedule(actual, 5);

    Graph<Task, DefaultWeightedEdge> expected =
        GraphTester.dotToGraphAllAttributes(
            getClass().getResourceAsStream("expected/Graph2_5.dot"));

    GraphTester.assertGraphEquals(expected, actual);
  }

  @Test
  public void testGraph1Processors1() {
    Graph<Task, DefaultWeightedEdge> actual =
        GraphParser.dotToGraph(getClass().getResourceAsStream("input/Graph1.dot"));
    ValidSchedule.findValidSchedule(actual, 1);

    Graph<Task, DefaultWeightedEdge> expected =
        GraphTester.dotToGraphAllAttributes(
            getClass().getResourceAsStream("expected/Graph1_1.dot"));

    GraphTester.assertGraphEquals(expected, actual);
  }

  @Test
  public void testGraph1Processors2() {
    Graph<Task, DefaultWeightedEdge> actual =
        GraphParser.dotToGraph(getClass().getResourceAsStream("input/Graph1.dot"));
    ValidSchedule.findValidSchedule(actual, 2);

    Graph<Task, DefaultWeightedEdge> expected =
        GraphTester.dotToGraphAllAttributes(
            getClass().getResourceAsStream("expected/Graph1_2.dot"));

    GraphTester.assertGraphEquals(expected, actual);
  }

  @Test
  public void testGraph1Processors3() {
    Graph<Task, DefaultWeightedEdge> actual =
        GraphParser.dotToGraph(getClass().getResourceAsStream("input/Graph1.dot"));
    ValidSchedule.findValidSchedule(actual, 3);

    Graph<Task, DefaultWeightedEdge> expected =
        GraphTester.dotToGraphAllAttributes(
            getClass().getResourceAsStream("expected/Graph1_3.dot"));

    GraphTester.assertGraphEquals(expected, actual);
  }
}
