package org.se306.algorithms;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.jupiter.api.Test;
import org.se306.GraphTester;
import org.se306.domain.Task;
import org.se306.utils.GraphParser;

public class AStarSearchTest {

  @Test
  public void testGraph1_1() {
    Graph<Task, DefaultWeightedEdge> actual = GraphParser
        .dotToGraph(getClass().getResourceAsStream("astarsearch/input/Graph1.dot"));
    AStarSearch.findValidSchedule(actual, 1);

    Graph<Task, DefaultWeightedEdge> expected = GraphTester.dotToGraphAllAttributes(
        getClass().getResourceAsStream("astarsearch/expected/Graph1_1.dot"));

    GraphTester.assertGraphEquals(expected, actual);
  }

  @Test
  public void testGraph2_2() {
    Graph<Task, DefaultWeightedEdge> actual = GraphParser
        .dotToGraph(getClass().getResourceAsStream("astarsearch/input/Graph2.dot"));
    AStarSearch.findValidSchedule(actual, 2);

    Graph<Task, DefaultWeightedEdge> expected = GraphTester.dotToGraphAllAttributes(
        getClass().getResourceAsStream("astarsearch/expected/Graph2_2.dot"));

    GraphTester.assertGraphEquals(expected, actual);
  }

  @Test
  public void testGraph3_3() {
    Graph<Task, DefaultWeightedEdge> actual = GraphParser
        .dotToGraph(getClass().getResourceAsStream("astarsearch/input/Graph3.dot"));
    AStarSearch.findValidSchedule(actual, 3);

    Graph<Task, DefaultWeightedEdge> expected = GraphTester.dotToGraphAllAttributes(
        getClass().getResourceAsStream("astarsearch/expected/Graph3_3.dot"));

    GraphTester.assertGraphEquals(expected, actual);
  }
}
