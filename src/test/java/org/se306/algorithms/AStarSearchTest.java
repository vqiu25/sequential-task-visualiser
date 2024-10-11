package org.se306.algorithms;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.jupiter.api.Test;
import org.se306.GraphTester;
import org.se306.domain.IOTask;
import org.se306.utils.GraphParser;

public class AStarSearchTest {

  // -- Auto-generated test cases using our algorithm --
  // Can't be sure they're correct, but they'll flag if our output changes unexpectedly

  // Commented out as they're autogenerated - update once the output order is more stable

  // @Test
  // public void testGraph1_1() {
  //   Graph<IOTask, DefaultWeightedEdge> actual = GraphParser
  //       .dotToGraph(getClass().getResourceAsStream("astarsearch/input/Graph1.dot"));
  //   AStarSearch.findSchedule(actual, 1);

  //   Graph<IOTask, DefaultWeightedEdge> expected = GraphTester.dotToGraphAllAttributes(
  //       getClass().getResourceAsStream("astarsearch/expected/Graph1_1.dot"));

  //   GraphTester.assertGraphEquals(expected, actual);
  // }

  // @Test
  // public void testGraph1_2() {
  //   Graph<IOTask, DefaultWeightedEdge> actual = GraphParser
  //       .dotToGraph(getClass().getResourceAsStream("astarsearch/input/Graph1.dot"));
  //   AStarSearch.findSchedule(actual, 2);

  //   Graph<IOTask, DefaultWeightedEdge> expected = GraphTester.dotToGraphAllAttributes(
  //       getClass().getResourceAsStream("astarsearch/expected/Graph1_2.dot"));

  //   GraphTester.assertGraphEquals(expected, actual);
  // }

  // @Test
  // public void testGraph1_3() {
  //   Graph<IOTask, DefaultWeightedEdge> actual = GraphParser
  //       .dotToGraph(getClass().getResourceAsStream("astarsearch/input/Graph1.dot"));
  //   AStarSearch.findSchedule(actual, 3);

  //   Graph<IOTask, DefaultWeightedEdge> expected = GraphTester.dotToGraphAllAttributes(
  //       getClass().getResourceAsStream("astarsearch/expected/Graph1_3.dot"));

  //   GraphTester.assertGraphEquals(expected, actual);
  // }

  // @Test
  // public void testGraph2_1() {
  //   Graph<IOTask, DefaultWeightedEdge> actual = GraphParser
  //       .dotToGraph(getClass().getResourceAsStream("astarsearch/input/Graph2.dot"));
  //   AStarSearch.findSchedule(actual, 1);

  //   Graph<IOTask, DefaultWeightedEdge> expected = GraphTester.dotToGraphAllAttributes(
  //       getClass().getResourceAsStream("astarsearch/expected/Graph2_1.dot"));

  //   GraphTester.assertGraphEquals(expected, actual);
  // }

  // @Test
  // public void testGraph2_2() {
  //   Graph<IOTask, DefaultWeightedEdge> actual = GraphParser
  //       .dotToGraph(getClass().getResourceAsStream("astarsearch/input/Graph2.dot"));
  //   AStarSearch.findSchedule(actual, 2);

  //   Graph<IOTask, DefaultWeightedEdge> expected = GraphTester.dotToGraphAllAttributes(
  //       getClass().getResourceAsStream("astarsearch/expected/Graph2_2.dot"));

  //   GraphTester.assertGraphEquals(expected, actual);
  // }

  // @Test
  // public void testGraph2_3() {
  //   Graph<IOTask, DefaultWeightedEdge> actual = GraphParser
  //       .dotToGraph(getClass().getResourceAsStream("astarsearch/input/Graph2.dot"));
  //   AStarSearch.findSchedule(actual, 3);

  //   Graph<IOTask, DefaultWeightedEdge> expected = GraphTester.dotToGraphAllAttributes(
  //       getClass().getResourceAsStream("astarsearch/expected/Graph2_3.dot"));

  //   GraphTester.assertGraphEquals(expected, actual);
  // }

  // @Test
  // public void testGraph3_1() {
  //   Graph<IOTask, DefaultWeightedEdge> actual = GraphParser
  //       .dotToGraph(getClass().getResourceAsStream("astarsearch/input/Graph3.dot"));
  //   AStarSearch.findSchedule(actual, 1);

  //   Graph<IOTask, DefaultWeightedEdge> expected = GraphTester.dotToGraphAllAttributes(
  //       getClass().getResourceAsStream("astarsearch/expected/Graph3_1.dot"));

  //   GraphTester.assertGraphEquals(expected, actual);
  // }

  // @Test
  // public void testGraph3_2() {
  //   Graph<IOTask, DefaultWeightedEdge> actual = GraphParser
  //       .dotToGraph(getClass().getResourceAsStream("astarsearch/input/Graph3.dot"));
  //   AStarSearch.findSchedule(actual, 2);

  //   Graph<IOTask, DefaultWeightedEdge> expected = GraphTester.dotToGraphAllAttributes(
  //       getClass().getResourceAsStream("astarsearch/expected/Graph3_2.dot"));

  //   GraphTester.assertGraphEquals(expected, actual);
  // }

  // @Test
  // public void testGraph3_3() {
  //   Graph<IOTask, DefaultWeightedEdge> actual = GraphParser
  //       .dotToGraph(getClass().getResourceAsStream("astarsearch/input/Graph3.dot"));
  //   AStarSearch.findSchedule(actual, 3);

  //   Graph<IOTask, DefaultWeightedEdge> expected = GraphTester.dotToGraphAllAttributes(
  //       getClass().getResourceAsStream("astarsearch/expected/Graph3_3.dot"));

  //   GraphTester.assertGraphEquals(expected, actual);
  // }

  // @Test
  // public void testGraph3_4() {
  //   Graph<IOTask, DefaultWeightedEdge> actual = GraphParser
  //       .dotToGraph(getClass().getResourceAsStream("astarsearch/input/Graph3.dot"));
  //   AStarSearch.findSchedule(actual, 4);

  //   Graph<IOTask, DefaultWeightedEdge> expected = GraphTester.dotToGraphAllAttributes(
  //       getClass().getResourceAsStream("astarsearch/expected/Graph3_4.dot"));

  //   GraphTester.assertGraphEquals(expected, actual);
  // }

  // @Test
  // public void testGraph3_5() {
  //   Graph<IOTask, DefaultWeightedEdge> actual = GraphParser
  //       .dotToGraph(getClass().getResourceAsStream("astarsearch/input/Graph3.dot"));
  //   AStarSearch.findSchedule(actual, 5);

  //   Graph<IOTask, DefaultWeightedEdge> expected = GraphTester.dotToGraphAllAttributes(
  //       getClass().getResourceAsStream("astarsearch/expected/Graph3_5.dot"));

  //   GraphTester.assertGraphEquals(expected, actual);
  // }

  // --- These tests are actually verfied by Oliver and are correct ---

  @Test
  public void testGraph7_2() {
    Graph<IOTask, DefaultWeightedEdge> graph =
        GraphParser.dotToGraph(
            getClass().getResourceAsStream("astarsearch/input/Nodes_7_OutTree.dot"));
    AStarSearch.findSchedule(graph, 2);

    int makespan = GraphTester.getMakespan(graph);
    assertEquals(28, makespan);
  }

  @Test
  public void testGraph7_4() {
    Graph<IOTask, DefaultWeightedEdge> graph =
        GraphParser.dotToGraph(
            getClass().getResourceAsStream("astarsearch/input/Nodes_7_OutTree.dot"));
    AStarSearch.findSchedule(graph, 4);

    int makespan = GraphTester.getMakespan(graph);
    assertEquals(22, makespan);
  }

  @Test
  public void testGraph8_2() {
    Graph<IOTask, DefaultWeightedEdge> graph =
        GraphParser.dotToGraph(
            getClass().getResourceAsStream("astarsearch/input/Nodes_8_Random.dot"));
    AStarSearch.findSchedule(graph, 2);

    int makespan = GraphTester.getMakespan(graph);
    assertEquals(581, makespan);
  }

  @Test
  public void testGraph8_4() {
    Graph<IOTask, DefaultWeightedEdge> graph =
        GraphParser.dotToGraph(
            getClass().getResourceAsStream("astarsearch/input/Nodes_8_Random.dot"));
    AStarSearch.findSchedule(graph, 4);

    int makespan = GraphTester.getMakespan(graph);
    assertEquals(581, makespan);
  }

  @Test
  public void testGraph9_2() {
    Graph<IOTask, DefaultWeightedEdge> graph =
        GraphParser.dotToGraph(
            getClass().getResourceAsStream("astarsearch/input/Nodes_9_SeriesParallel.dot"));
    AStarSearch.findSchedule(graph, 2);

    int makespan = GraphTester.getMakespan(graph);
    assertEquals(55, makespan);
  }

  @Test
  public void testGraph9_4() {
    Graph<IOTask, DefaultWeightedEdge> graph =
        GraphParser.dotToGraph(
            getClass().getResourceAsStream("astarsearch/input/Nodes_9_SeriesParallel.dot"));
    AStarSearch.findSchedule(graph, 4);

    int makespan = GraphTester.getMakespan(graph);
    assertEquals(55, makespan);
  }

  @Test
  public void testGraph10_2() {
    Graph<IOTask, DefaultWeightedEdge> graph =
        GraphParser.dotToGraph(
            getClass().getResourceAsStream("astarsearch/input/Nodes_10_Random.dot"));
    AStarSearch.findSchedule(graph, 2);

    int makespan = GraphTester.getMakespan(graph);
    assertEquals(50, makespan);
  }

  @Test
  public void testGraph10_4() {
    Graph<IOTask, DefaultWeightedEdge> graph =
        GraphParser.dotToGraph(
            getClass().getResourceAsStream("astarsearch/input/Nodes_10_Random.dot"));
    AStarSearch.findSchedule(graph, 4);

    int makespan = GraphTester.getMakespan(graph);
    assertEquals(50, makespan);
  }

  @Test
  public void testGraph11_2() {
    Graph<IOTask, DefaultWeightedEdge> graph =
        GraphParser.dotToGraph(
            getClass().getResourceAsStream("astarsearch/input/Nodes_11_OutTree.dot"));
    AStarSearch.findSchedule(graph, 2);

    int makespan = GraphTester.getMakespan(graph);
    assertEquals(350, makespan);
  }

  @Test
  public void testGraph11_4() {
    Graph<IOTask, DefaultWeightedEdge> graph =
        GraphParser.dotToGraph(
            getClass().getResourceAsStream("astarsearch/input/Nodes_11_OutTree.dot"));
    AStarSearch.findSchedule(graph, 4);

    int makespan = GraphTester.getMakespan(graph);
    assertEquals(227, makespan);
  }
}