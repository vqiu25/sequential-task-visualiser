package org.se306.algorithms;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.jupiter.api.Test;
import org.se306.GraphTester;
import org.se306.domain.IOTask;
import org.se306.utils.GraphParser;

public class ListScheduleTest {

  @Test
  public void testGraph2Processors5() {
    Graph<IOTask, DefaultWeightedEdge> actual =
        GraphParser.dotToGraph(getClass().getResourceAsStream("listschedule/input/Graph2.dot"));
    ListSchedule.findValidSchedule(actual, 5);

    Graph<IOTask, DefaultWeightedEdge> expected =
        GraphTester.dotToGraphAllAttributes(
            getClass().getResourceAsStream("listschedule/expected/Graph2_5.dot"));

    GraphTester.assertGraphEquals(expected, actual);
  }

  @Test
  public void testGraph1Processors1() {
    Graph<IOTask, DefaultWeightedEdge> actual =
        GraphParser.dotToGraph(getClass().getResourceAsStream("listschedule/input/Graph1.dot"));
    ListSchedule.findValidSchedule(actual, 1);

    Graph<IOTask, DefaultWeightedEdge> expected =
        GraphTester.dotToGraphAllAttributes(
            getClass().getResourceAsStream("listschedule/expected/Graph1_1.dot"));

    GraphTester.assertGraphEquals(expected, actual);
  }

  @Test
  public void testGraph1Processors2() {
    Graph<IOTask, DefaultWeightedEdge> actual =
        GraphParser.dotToGraph(getClass().getResourceAsStream("listschedule/input/Graph1.dot"));
    ListSchedule.findValidSchedule(actual, 2);

    Graph<IOTask, DefaultWeightedEdge> expected =
        GraphTester.dotToGraphAllAttributes(
            getClass().getResourceAsStream("listschedule/expected/Graph1_2.dot"));

    GraphTester.assertGraphEquals(expected, actual);
  }

  @Test
  public void testGraph1Processors3() {
    Graph<IOTask, DefaultWeightedEdge> actual =
        GraphParser.dotToGraph(getClass().getResourceAsStream("listschedule/input/Graph1.dot"));
    ListSchedule.findValidSchedule(actual, 3);

    Graph<IOTask, DefaultWeightedEdge> expected =
        GraphTester.dotToGraphAllAttributes(
            getClass().getResourceAsStream("listschedule/expected/Graph1_3.dot"));

    GraphTester.assertGraphEquals(expected, actual);
  }
}
