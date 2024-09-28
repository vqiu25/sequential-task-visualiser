package org.se306;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.se306.domain.Task;
public class GraphTester {

  public static void assertGraphEquals(Graph<Task, DefaultWeightedEdge> expected,
      Graph<Task, DefaultWeightedEdge> actual) {

    // Check the number of vertices
    assertEquals(expected.vertexSet().size(), actual.vertexSet().size());

    // Arrange vertices in order of ID
    List<Task> expectedVertices = new ArrayList<>(expected.vertexSet());
    List<Task> actualVertices = new ArrayList<>(actual.vertexSet());
    expectedVertices.sort((a, b) -> a.getId().compareTo(b.getId()));
    actualVertices.sort((a, b) -> a.getId().compareTo(b.getId()));

    // Check each vertex
    for (int i = 0; i < expectedVertices.size(); i++) {
      Task expectedVertex = expectedVertices.get(i);
      Task actualVertex = actualVertices.get(i);
      assertEquals(expectedVertex.getId(), actualVertex.getId()); // ID
      assertEquals(expectedVertex.getTaskLength(), actualVertex.getTaskLength()); // TaskLength
      assertEquals(expectedVertex.getStartTime(), actualVertex.getStartTime()); // StartTime
      assertEquals(expectedVertex.getProcessor(), actualVertex.getProcessor()); // Processor
    }

    // Check the number of edges
    assertEquals(expected.edgeSet().size(), actual.edgeSet().size());

    // Arrange edges in order of toString
    List<DefaultWeightedEdge> expectedEdges = new ArrayList<>(expected.edgeSet());
    List<DefaultWeightedEdge> actualEdges = new ArrayList<>(actual.edgeSet());
    expectedEdges.sort((a, b) -> a.toString().compareTo(b.toString()));
    actualEdges.sort((a, b) -> a.toString().compareTo(b.toString()));

    // Check each edge
    for (int i = 0; i < expectedEdges.size(); i++) {
      DefaultWeightedEdge expectedEdge = expectedEdges.get(i);
      DefaultWeightedEdge actualEdge = actualEdges.get(i);
      assertEquals(expected.getEdgeSource(expectedEdge), actual.getEdgeSource(actualEdge)); // Source
      assertEquals(expected.getEdgeTarget(expectedEdge), actual.getEdgeTarget(actualEdge)); // Target
      assertEquals(expected.getEdgeWeight(expectedEdge), actual.getEdgeWeight(actualEdge)); // Weight
    }
  }
}
