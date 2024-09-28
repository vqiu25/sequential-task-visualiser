package org.se306;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.se306.domain.Task;

public class GraphTester {

  public static void assertEquals(Graph<Task, DefaultWeightedEdge> expected,
      Graph<Task, DefaultWeightedEdge> actual) {
    org.junit.jupiter.api.Assertions.assertEquals(expected.vertexSet().size(), actual.vertexSet().size());

    // Check each vertex
    List<Task> expectedVertices = new ArrayList<>(expected.vertexSet());
    List<Task> actualVertices = new ArrayList<>(actual.vertexSet());
    expectedVertices.sort((a, b) -> a.getId().compareTo(b.getId()));
    actualVertices.sort((a, b) -> a.getId().compareTo(b.getId()));

    for (int i = 0; i < expectedVertices.size(); i++) {
      Task expectedVertex = expectedVertices.get(i);
      Task actualVertex = actualVertices.get(i);

      org.junit.jupiter.api.Assertions.assertEquals(expectedVertex.getId(), actualVertex.getId());
      org.junit.jupiter.api.Assertions.assertEquals(expectedVertex.getTaskLength(), actualVertex.getTaskLength());
      org.junit.jupiter.api.Assertions.assertEquals(expectedVertex.getStartTime(), actualVertex.getStartTime());
      org.junit.jupiter.api.Assertions.assertEquals(expectedVertex.getProcessor(), actualVertex.getProcessor());
    }
  }
}
