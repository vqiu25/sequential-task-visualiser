package org.se306.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.jupiter.api.Test;
import org.se306.domain.Task;

/** Tests GraphParser by reading in dot file then writing it back out and checking output */
public class GraphParserTest {

  @Test
  public void testOutTreeFile() throws IOException, URISyntaxException {
    String expected =
        """
        strict digraph G {
          1 [ Weight="5" ];
          2 [ Weight="6" ];
          3 [ Weight="5" ];
          4 [ Weight="6" ];
          5 [ Weight="4" ];
          6 [ Weight="7" ];
          7 [ Weight="7" ];
          1 -> 2 [ Weight="15" ];
          1 -> 3 [ Weight="11" ];
          1 -> 4 [ Weight="11" ];
          2 -> 5 [ Weight="19" ];
          2 -> 6 [ Weight="4" ];
          2 -> 7 [ Weight="21" ];
        }
        """;

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
    System.err.println(actual);
    System.err.println(expected);
    assertEquals(expected.replaceAll("\\s", ""), actual.replaceAll("\\s", "")); // Ignore whitespace
  }
}
