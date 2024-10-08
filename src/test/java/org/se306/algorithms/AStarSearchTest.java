package org.se306.algorithms;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.jupiter.api.Test;
import org.se306.GraphTester;
import org.se306.domain.IOTask;
import org.se306.utils.GraphParser;

public class AStarSearchTest {

  @Test
  public void testGraph1_1() {
    Graph<IOTask, DefaultWeightedEdge> actual = GraphParser
        .dotToGraph(getClass().getResourceAsStream("astarsearch/input/Graph1.dot"));
    AStarSearch.findSchedule(actual, 1);

    Graph<IOTask, DefaultWeightedEdge> expected = GraphTester.dotToGraphAllAttributes(
        getClass().getResourceAsStream("astarsearch/expected/Graph1_1.dot"));

    GraphTester.assertGraphEquals(expected, actual);
  }

  @Test
  public void testGraph1_2() {
    Graph<IOTask, DefaultWeightedEdge> actual = GraphParser
        .dotToGraph(getClass().getResourceAsStream("astarsearch/input/Graph1.dot"));
    AStarSearch.findSchedule(actual, 2);

    Graph<IOTask, DefaultWeightedEdge> expected = GraphTester.dotToGraphAllAttributes(
        getClass().getResourceAsStream("astarsearch/expected/Graph1_2.dot"));

    GraphTester.assertGraphEquals(expected, actual);
  }

  @Test
  public void testGraph1_3() {
    Graph<IOTask, DefaultWeightedEdge> actual = GraphParser
        .dotToGraph(getClass().getResourceAsStream("astarsearch/input/Graph1.dot"));
    AStarSearch.findSchedule(actual, 3);

    Graph<IOTask, DefaultWeightedEdge> expected = GraphTester.dotToGraphAllAttributes(
        getClass().getResourceAsStream("astarsearch/expected/Graph1_3.dot"));

    GraphTester.assertGraphEquals(expected, actual);
  }

  @Test
  public void testGraph2_1() {
    Graph<IOTask, DefaultWeightedEdge> actual = GraphParser
        .dotToGraph(getClass().getResourceAsStream("astarsearch/input/Graph2.dot"));
    AStarSearch.findSchedule(actual, 1);

    Graph<IOTask, DefaultWeightedEdge> expected = GraphTester.dotToGraphAllAttributes(
        getClass().getResourceAsStream("astarsearch/expected/Graph2_1.dot"));

    GraphTester.assertGraphEquals(expected, actual);
  }

  @Test
  public void testGraph2_2() {
    Graph<IOTask, DefaultWeightedEdge> actual = GraphParser
        .dotToGraph(getClass().getResourceAsStream("astarsearch/input/Graph2.dot"));
    AStarSearch.findSchedule(actual, 2);

    Graph<IOTask, DefaultWeightedEdge> expected = GraphTester.dotToGraphAllAttributes(
        getClass().getResourceAsStream("astarsearch/expected/Graph2_2.dot"));

    GraphTester.assertGraphEquals(expected, actual);
  }

  @Test
  public void testGraph2_3() {
    Graph<IOTask, DefaultWeightedEdge> actual = GraphParser
        .dotToGraph(getClass().getResourceAsStream("astarsearch/input/Graph2.dot"));
    AStarSearch.findSchedule(actual, 3);

    Graph<IOTask, DefaultWeightedEdge> expected = GraphTester.dotToGraphAllAttributes(
        getClass().getResourceAsStream("astarsearch/expected/Graph2_3.dot"));

    GraphTester.assertGraphEquals(expected, actual);
  }

  @Test
  public void testGraph3_1() {
    Graph<IOTask, DefaultWeightedEdge> actual = GraphParser
        .dotToGraph(getClass().getResourceAsStream("astarsearch/input/Graph3.dot"));
    AStarSearch.findSchedule(actual, 1);

    Graph<IOTask, DefaultWeightedEdge> expected = GraphTester.dotToGraphAllAttributes(
        getClass().getResourceAsStream("astarsearch/expected/Graph3_1.dot"));

    GraphTester.assertGraphEquals(expected, actual);
  }

  @Test
  public void testGraph3_2() {
    Graph<IOTask, DefaultWeightedEdge> actual = GraphParser
        .dotToGraph(getClass().getResourceAsStream("astarsearch/input/Graph3.dot"));
    AStarSearch.findSchedule(actual, 2);

    Graph<IOTask, DefaultWeightedEdge> expected = GraphTester.dotToGraphAllAttributes(
        getClass().getResourceAsStream("astarsearch/expected/Graph3_2.dot"));

    GraphTester.assertGraphEquals(expected, actual);
  }

  @Test
  public void testGraph3_3() {
    Graph<IOTask, DefaultWeightedEdge> actual = GraphParser
        .dotToGraph(getClass().getResourceAsStream("astarsearch/input/Graph3.dot"));
    AStarSearch.findSchedule(actual, 3);

    Graph<IOTask, DefaultWeightedEdge> expected = GraphTester.dotToGraphAllAttributes(
        getClass().getResourceAsStream("astarsearch/expected/Graph3_3.dot"));

    GraphTester.assertGraphEquals(expected, actual);
  }

  @Test
  public void testGraph3_4() {
    Graph<IOTask, DefaultWeightedEdge> actual = GraphParser
        .dotToGraph(getClass().getResourceAsStream("astarsearch/input/Graph3.dot"));
    AStarSearch.findSchedule(actual, 4);

    Graph<IOTask, DefaultWeightedEdge> expected = GraphTester.dotToGraphAllAttributes(
        getClass().getResourceAsStream("astarsearch/expected/Graph3_4.dot"));

    GraphTester.assertGraphEquals(expected, actual);
  }

  @Test
  public void testGraph3_5() {
    Graph<IOTask, DefaultWeightedEdge> actual = GraphParser
        .dotToGraph(getClass().getResourceAsStream("astarsearch/input/Graph3.dot"));
    AStarSearch.findSchedule(actual, 5);

    Graph<IOTask, DefaultWeightedEdge> expected = GraphTester.dotToGraphAllAttributes(
        getClass().getResourceAsStream("astarsearch/expected/Graph3_5.dot"));

    GraphTester.assertGraphEquals(expected, actual);
  }
}
