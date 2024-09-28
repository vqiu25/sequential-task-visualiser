package org.se306.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.se306.AppTest;
import org.se306.domain.Task;

/** Tests GraphParser by reading in dot file then writing it back out and checking output */
public class GraphParserTest {

  @Test
  public void testOutTreeFile() throws IOException, URISyntaxException {
    testGraphParser("utils/input/Nodes_7_OutTree.dot");
  }

  @Test
  public void testRandomFile() throws IOException, URISyntaxException {
    testGraphParser("utils/input/Nodes_8_Random.dot");
  }

  @Test
  public void testSeriesParallelFile() throws IOException, URISyntaxException {
    testGraphParser("utils/input/Nodes_9_SeriesParallel.dot");
  }

  @Test
  public void testRandom2File() throws IOException, URISyntaxException {
    testGraphParser("utils/input/Nodes_10_Random.dot");
  }

  @Test
  public void testOutTree2File() throws IOException, URISyntaxException {
    testGraphParser("utils/input/Nodes_11_OutTree.dot");
  }

  private void testGraphParser(String dotFileUrl) throws IOException, URISyntaxException {
    URI expectedPath = AppTest.class.getResource(dotFileUrl.replace("input/", "expected/")).toURI();
    String testPath = "out/" + dotFileUrl;

    // Read
    InputStream dotFileInputStream = AppTest.class.getResourceAsStream(dotFileUrl);
    Graph<Task, DefaultWeightedEdge> graph = GraphParser.dotToGraph(dotFileInputStream);

    // Write
    GraphParser.graphToDot(graph, testPath);

    // Compare (ignoring whitespace)
    String expected = Files.readString(Paths.get(expectedPath)).replaceAll("\\s", "");
    String actual = Files.readString(Paths.get(testPath)).replaceAll("\\s", "");
    assertEquals(expected, actual);
  }
}
