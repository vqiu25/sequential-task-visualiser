package org.se306;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.nio.ImportException;
import org.jgrapht.nio.dot.DOTImporter;
import org.se306.domain.IOTask;
import org.slf4j.Logger;

public class GraphTester {

  private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(GraphTester.class);

  /**
   * Asserts that two graphs are equal. This method compares the vertices and edges of the two
   * graphs.
   *
   * @param expected The expected graph
   * @param actual The actual graph
   */
  public static void assertGraphEquals(
      Graph<IOTask, DefaultWeightedEdge> expected, Graph<IOTask, DefaultWeightedEdge> actual) {

    // Check the number of vertices
    assertEquals(
        expected.vertexSet().size(), actual.vertexSet().size(), "Number of vertices not equal");

    // Arrange vertices in order of ID
    List<IOTask> expectedVertices = new ArrayList<>(expected.vertexSet());
    List<IOTask> actualVertices = new ArrayList<>(actual.vertexSet());
    expectedVertices.sort((a, b) -> a.getId().compareTo(b.getId()));
    actualVertices.sort((a, b) -> a.getId().compareTo(b.getId()));

    // Check each vertex
    for (int i = 0; i < expectedVertices.size(); i++) {
      IOTask expectedVertex = expectedVertices.get(i);
      IOTask actualVertex = actualVertices.get(i);
      assertEquals(
          expectedVertex.getId(),
          actualVertex.getId(),
          "Vertex " + expectedVertex + " IDs not equal");
      assertEquals(
          expectedVertex.getTaskLength(),
          actualVertex.getTaskLength(),
          "Vertex " + expectedVertex + " task lengths not equal");
      assertEquals(
          expectedVertex.getStartTime(),
          actualVertex.getStartTime(),
          "Vertex " + expectedVertex + " start times not equal");
      assertEquals(
          expectedVertex.getProcessor(),
          actualVertex.getProcessor(),
          "Vertex " + expectedVertex + " processors not equal");
    }

    // Check the number of edges
    assertEquals(expected.edgeSet().size(), actual.edgeSet().size(), "Number of edges not equal");

    // Arrange edges in order of toString
    List<DefaultWeightedEdge> expectedEdges = new ArrayList<>(expected.edgeSet());
    List<DefaultWeightedEdge> actualEdges = new ArrayList<>(actual.edgeSet());
    expectedEdges.sort((a, b) -> a.toString().compareTo(b.toString()));
    actualEdges.sort((a, b) -> a.toString().compareTo(b.toString()));

    // Check each edge
    for (int i = 0; i < expectedEdges.size(); i++) {
      DefaultWeightedEdge expectedEdge = expectedEdges.get(i);
      DefaultWeightedEdge actualEdge = actualEdges.get(i);
      assertEquals(
          expected.getEdgeSource(expectedEdge),
          actual.getEdgeSource(actualEdge),
          "Edge " + expectedEdge + " sources not equal");
      assertEquals(
          expected.getEdgeTarget(expectedEdge),
          actual.getEdgeTarget(actualEdge),
          "Edge " + expectedEdge + " targets not equal");
      assertEquals(
          expected.getEdgeWeight(expectedEdge),
          actual.getEdgeWeight(actualEdge),
          "Edge " + expectedEdge + " weights not equal");
    }
  }

  /**
   * Returns the makespan of a task graph (unefficient implementation)
   *
   * @param graph The task graph
   * @return The makespan (latest end time of all tasks)
   */
  public static int getMakespan(Graph<IOTask, DefaultWeightedEdge> graph) {
    int makespan = 0;
    for (IOTask task : graph.vertexSet()) {
      int taskEndTime = task.getEndTime();
      if (taskEndTime > makespan) {
        makespan = taskEndTime;
      }
    }
    return makespan;
  }

  /**
   * Reads a dot file and returns a graph, containing all attributes
   *
   * @param dotFileInputStream The InputStream opened from the dot file
   * @return The JGraphT SimpleDirectedWeightedGraph
   */
  public static Graph<IOTask, DefaultWeightedEdge> dotToGraphAllAttributes(
      InputStream dotFileInputStream) {
    Graph<IOTask, DefaultWeightedEdge> graph =
        new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
    DOTImporter<IOTask, DefaultWeightedEdge> importer = new DOTImporter<>();

    // How to read vertex attributes
    // Testing: get all of the attributes (Weight, Start, Processor)
    importer.setVertexWithAttributesFactory(
        (id, attributes) -> {
          int weight = Integer.parseInt(attributes.get("Weight").getValue());
          int startTime = Integer.parseInt(attributes.get("Start").getValue());
          int processor = Integer.parseInt(attributes.get("Processor").getValue());
          return new IOTask(id, weight, startTime, processor);
        });

    // How to read edge attributes: Weight
    importer.setEdgeWithAttributesFactory(
        (attributes) -> {
          int weight = Integer.parseInt(attributes.get("Weight").getValue());
          DefaultWeightedEdge edge = new DefaultWeightedEdge();
          graph.setEdgeWeight(edge, weight);
          return edge;
        });

    // Store graph
    try {
      importer.importGraph(graph, dotFileInputStream);
    } catch (ImportException e) {
      LOGGER.error("Error storing graph from dot file", e);
      throw new RuntimeException(e);
    }

    return graph;
  }
}
