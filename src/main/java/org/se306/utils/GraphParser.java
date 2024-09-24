package org.se306.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.nio.dot.DOTImporter;
import org.se306.App;
import org.se306.domain.Task;

public class GraphParser {

  public static void dotToGraph(String dotFileUrl) {
    Graph<Task, DefaultWeightedEdge> graph = new SimpleGraph<>(DefaultWeightedEdge.class);
    DOTImporter<Task, DefaultWeightedEdge> importer = new DOTImporter<>();

    importer.setVertexWithAttributesFactory(
        (id, attributes) -> {
          int weight = Integer.parseInt(attributes.get("Weight").getValue());
          return new Task(id, weight);
        });

    try (Reader dotFileReader = new InputStreamReader(App.class.getResourceAsStream(dotFileUrl))) {
      importer.importGraph(graph, dotFileReader);
    } catch (IOException e) {
      e.printStackTrace();
    }

    System.out.println(graph);
  }
}
