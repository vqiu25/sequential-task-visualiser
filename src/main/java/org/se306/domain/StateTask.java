package org.se306.domain;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

/**
 * Represents a task in the STATE TREE. Each StateTask points to an IOTask but
 * stores the start time and processor itself to keep track of the schedule
 * individually for each state. Once created by A*, it is never modified.
 *
 * @see org.se306.domain.IOTask In contrast, IOTask is used to represent the
 *      task graph and is the source of truth for the task's length and
 *      predecessors/successors.
 */
public class StateTask {

  private final IOTask ioTask;
  private final int processor; // 0-indexed
  private final int startTime;

  public StateTask(IOTask ioTask, int processor, int startTime) {
    this.ioTask = ioTask;
    this.processor = processor;
    this.startTime = startTime;
  }

  public int getProcessor() {
    return processor;
  }

  public int getStartTime() {
    return startTime;
  }

  public int getDuration() {
    return ioTask.getTaskLength();
  }

  public int getEndTime() {
    return startTime + getDuration();
  }

  public int getBottomLevel(Graph<IOTask, DefaultWeightedEdge> graph) {
    return ioTask.getBottomLevel(graph);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((ioTask == null) ? 0 : ioTask.hashCode());
    result = prime * result + processor;
    result = prime * result + startTime;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    StateTask other = (StateTask) obj;
    if (ioTask == null) {
      if (other.ioTask != null)
        return false;
    } else if (!ioTask.equals(other.ioTask))
      return false;
    if (processor != other.processor)
      return false;
    if (startTime != other.startTime)
      return false;
    return true;
  }

}
