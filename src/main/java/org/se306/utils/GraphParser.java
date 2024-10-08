package org.se306.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.AttributeType;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.ImportException;
import org.jgrapht.nio.dot.DOTExporter;
import org.jgrapht.nio.dot.DOTImporter;
import org.se306.domain.IOTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GraphParser {

  private static final Logger LOGGER = LoggerFactory.getLogger(GraphParser.class);

  /**
   * Reads a dot file and returns a graph
   *
   * @param dotFileInputStream The InputStream opened from the dot file
   * @return The JGraphT SimpleDirectedWeightedGraph
   */
  public static Graph<IOTask, DefaultWeightedEdge> dotToGraph(InputStream dotFileInputStream) {
    Graph<IOTask, DefaultWeightedEdge> graph =
        new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
    DOTImporter<IOTask, DefaultWeightedEdge> importer = new DOTImporter<>();

    // How to read vertex attributes: Weight
    importer.setVertexWithAttributesFactory(
        (id, attributes) -> {
          int weight = Integer.parseInt(attributes.get("Weight").getValue());
          return new IOTask(id, weight);
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

  /**
   * Writes a graph to a dot file
   *
   * @param graph The JGraphT graph
   * @param dotFileUrl The String URL of the dot file, relative to the root directory
   */
  public static void graphToDot(Graph<IOTask, DefaultWeightedEdge> graph, String dotFileUrl) {
    DOTExporter<IOTask, DefaultWeightedEdge> exporter = new DOTExporter<>(IOTask::getId);

    // Vertex attributes with specified types
    exporter.setVertexAttributeProvider(
        task -> {
          Map<String, Attribute> attributes = new LinkedHashMap<>();
          attributes.put(
              "Weight",
              new DefaultAttribute<>(task.getTaskLength() + ",", AttributeType.IDENTIFIER));
          attributes.put(
              "Start", new DefaultAttribute<>(task.getStartTime() + ",", AttributeType.IDENTIFIER));
          attributes.put(
              "Processor", new DefaultAttribute<>(task.getProcessor(), AttributeType.IDENTIFIER));
          return attributes;
        });

    // Edge attributes with specified types
    exporter.setEdgeAttributeProvider(
        edge ->
            Map.of(
                "Weight",
                new DefaultAttribute<>((int) graph.getEdgeWeight(edge), AttributeType.IDENTIFIER)));

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

  /**
   * Creates a file if it does not exist, and if the url contains a parent directory, creates the
   * directory if it does not exist
   *
   * @param dotFileUrl the URL of the file e.g. dot/graph.dot, or just graph.dot
   */
  private static void createFileIfNotExists(String dotFileUrl) {
    try {
      Path dir = Paths.get(dotFileUrl).getParent();
      if (dir != null) {
        Files.createDirectories(dir);
      }

      Path file = Paths.get(dotFileUrl);
      if (!Files.exists(file)) {
        Files.createFile(file);
      }
    } catch (IOException e) {
      LOGGER.error("Error creating file", e);
      throw new RuntimeException(e);
    }
  }
}
