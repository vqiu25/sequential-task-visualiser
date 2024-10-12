package org.se306.helpers;

import java.util.HashMap;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.se306.domain.IOTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IOTaskMap {

  private static final Logger LOGGER = LoggerFactory.getLogger(IOTaskMap.class);
  private static IOTaskMap instance;

  private final Map<String, IOTask> idToIOTask = new HashMap<>();

  private IOTaskMap(Graph<IOTask, DefaultWeightedEdge> graph) {
    for (IOTask task : graph.vertexSet()) {
      addIOTask(task);
    }
  }

  /** Called at start of A* algorithm */
  public static void initialise(Graph<IOTask, DefaultWeightedEdge> graph) {
    if (instance != null) {
      LOGGER.warn("TaskMap already initialised");
    }
    instance = new IOTaskMap(graph);
  }

  public static IOTaskMap getInstance() {
    if (instance == null) {
      LOGGER.error("TaskMap not initialised");
      throw new IllegalStateException("TaskMap not initialised");
    }
    return instance;
  }

  private void addIOTask(IOTask task) {
    idToIOTask.put(task.getId(), task);
  }

  public IOTask getIOTask(String id) {
    return idToIOTask.get(id);
  }

}
