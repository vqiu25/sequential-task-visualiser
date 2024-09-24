package org.se306.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.nio.dot.DOTExporter;
import org.jgrapht.nio.dot.DOTImporter;
import org.se306.App;
import org.se306.domain.Task;

public class GraphParser {

  public static Graph<Task, DefaultWeightedEdge> dotToGraph(String dotFileUrl) {
    Graph<Task, DefaultWeightedEdge> graph = new SimpleGraph<>(DefaultWeightedEdge.class);
    DOTImporter<Task, DefaultWeightedEdge> importer = new DOTImporter<>();

    importer.setVertexWithAttributesFactory(
        (id, attributes) -> {
          int weight = Integer.parseInt(attributes.get("Weight").getValue());
          return new Task(id, weight);
        });

    try (InputStream dotFileInputStream = App.class.getResourceAsStream(dotFileUrl)) {
      importer.importGraph(graph, dotFileInputStream);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return graph;
  }

  public static void graphToDot(Graph<Task, DefaultWeightedEdge> graph, String dotFileUrl) {
    DOTExporter<Task, DefaultWeightedEdge> exporter = new DOTExporter<>();

    // exporter.setVertexAttributeProvider(
    //     task -> {
    //       String weight = Integer.toString(task.getWeight());
    //       return Map.of("Weight", weight);
    //     });

    // Create file if it does not exist
    try {
      Files.createDirectories(Paths.get(dotFileUrl).getParent());
      Files.createFile(Paths.get(dotFileUrl));
    } catch (IOException e) {
      e.printStackTrace();
    }

    try (OutputStream dotFileOutputStream = Files.newOutputStream(Paths.get(dotFileUrl))) {
      exporter.exportGraph(graph, dotFileOutputStream);
      dotFileOutputStream.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
