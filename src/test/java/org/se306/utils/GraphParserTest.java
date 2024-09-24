package org.se306.utils;

import org.junit.jupiter.api.Test;

public class GraphParserTest {

  @Test
  public void testDotToGraph() {
    GraphParser.dotToGraph("dot/Nodes_7_OutTree.dot");
  }
}
