package org.se306.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.dot.DOTExporter;
import org.jgrapht.nio.dot.DOTImporter;
import org.se306.App;
import org.se306.domain.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GraphParser {

  private static final Logger LOGGER = LoggerFactory.getLogger(GraphParser.class);

  /**
   * Reads a dot file and returns a graph
   *
   * @param dotFileUrl The URL of the dot file, relative to the resources/org/se306 folder
   * @return The JGraphT SimpleDirectedWeightedGraph
   */
  public static Graph<Task, DefaultWeightedEdge> dotToGraph(String dotFileUrl) {
    Graph<Task, DefaultWeightedEdge> graph =
        new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
    DOTImporter<Task, DefaultWeightedEdge> importer = new DOTImporter<>();

    // How to read vertex attributes: Weight
    importer.setVertexWithAttributesFactory(
        (id, attributes) -> {
          int weight = Integer.parseInt(attributes.get("Weight").getValue());
          return new Task(id, weight);
        });

    // How to read edge attributes: Weight
    importer.setEdgeWithAttributesFactory(
        (attributes) -> {
          int weight = Integer.parseInt(attributes.get("Weight").getValue());
          DefaultWeightedEdge edge = new DefaultWeightedEdge();
          graph.setEdgeWeight(edge, weight);
          return edge;
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

  /**
   * Writes a graph to a dot file
   *
   * @param graph The JGraphT graph
   * @param dotFileUrl The URL of the dot file, relative to the resources/org/se306 folder
   */
  public static void graphToDot(Graph<Task, DefaultWeightedEdge> graph, String dotFileUrl) {
    DOTExporter<Task, DefaultWeightedEdge> exporter = new DOTExporter<>();

    // How to write vertex attributes: Weight
    exporter.setVertexAttributeProvider(
        task -> {
          return Map.of("Weight", DefaultAttribute.createAttribute(task.getTaskLength()));
        });

    // How to write edge attributes: Weight
    exporter.setEdgeAttributeProvider(
        edge -> {
          return Map.of(
              "Weight", DefaultAttribute.createAttribute((int) graph.getEdgeWeight(edge)));
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
