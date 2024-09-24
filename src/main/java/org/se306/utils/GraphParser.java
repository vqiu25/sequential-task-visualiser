package org.se306.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.dot.DOTExporter;
import org.jgrapht.nio.dot.DOTImporter;
import org.se306.App;
import org.se306.domain.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GraphParser {

  private static final Logger LOGGER = LoggerFactory.getLogger(GraphParser.class);

  public static Graph<Task, DefaultWeightedEdge> dotToGraph(String dotFileUrl) {
    Graph<Task, DefaultWeightedEdge> graph = new SimpleGraph<>(DefaultWeightedEdge.class);
    DOTImporter<Task, DefaultWeightedEdge> importer = new DOTImporter<>();

    // Set how to read vertex attributes
    importer.setVertexWithAttributesFactory(
        (id, attributes) -> {
          int weight = Integer.parseInt(attributes.get("Weight").getValue());
          return new Task(id, weight);
        });

    // Read from file
    try (InputStream dotFileInputStream = App.class.getResourceAsStream(dotFileUrl)) {
      importer.importGraph(graph, dotFileInputStream);
    } catch (IOException e) {
      LOGGER.error("Error reading dot file", e);
      throw new RuntimeException(e);
    }

    return graph;
  }

  public static void graphToDot(Graph<Task, DefaultWeightedEdge> graph, String dotFileUrl) {
    DOTExporter<Task, DefaultWeightedEdge> exporter = new DOTExporter<>();

    // Set how to write vertex attributes
    exporter.setVertexAttributeProvider(
        task -> {
          return Map.of("Weight", DefaultAttribute.createAttribute(task.getWeight()));
        });

    // Write to file
    createFileIfNotExists(dotFileUrl);
    try (OutputStream dotFileOutputStream = Files.newOutputStream(Paths.get(dotFileUrl))) {
      exporter.exportGraph(graph, dotFileOutputStream);
      dotFileOutputStream.flush();
    } catch (IOException e) {
      LOGGER.error("Error writing dot file", e);
      throw new RuntimeException(e);
    }
  }

  private static void createFileIfNotExists(String dotFileUrl) {
    try {
      Files.createDirectories(Paths.get(dotFileUrl).getParent());
      if (!Files.exists(Paths.get(dotFileUrl))) {
        Files.createFile(Paths.get(dotFileUrl));
      }
    } catch (IOException e) {
      LOGGER.error("Error creating file", e);
      throw new RuntimeException(e);
    }
  }
}
