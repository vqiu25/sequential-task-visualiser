package org.se306.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.se306.domain.Task;

/** Tests GraphParser by reading in dot file then writing it back out and checking output */
public class GraphParserTest {

  @Test
  public void testOutTreeFile() throws IOException, URISyntaxException {
    String expected =
        """
        digraph G {
          0 [Weight=1];
          1 [Weight=2];
          2 [Weight=3];
          3 [Weight=4];
          4 [Weight=5];
          5 [Weight=6];
          6 [Weight=7];
          0 -> 1 [];
          0 -> 2 [];
          1 -> 3 [];
          1 -> 4 [];
          2 -> 5 [];
          2 -> 6 [];
        }
        """; // Temp

    testGraphParser("dot/Nodes_7_OutTree.dot", expected);
  }

  private void testGraphParser(String dotFileUrl, String expected) throws IOException {
    String testPath = "test/" + dotFileUrl;

    // Read
    Graph<Task, DefaultWeightedEdge> graph = GraphParser.dotToGraph(dotFileUrl);

    // Write
    GraphParser.graphToDot(graph, testPath);

    // Compare
    String actual = Files.readString(Paths.get(testPath));
    assertEquals(expected, actual);
  }
}
