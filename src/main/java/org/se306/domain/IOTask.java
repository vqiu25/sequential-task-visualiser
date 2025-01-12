package org.se306.domain;

/**
 * Represents a task in the TASK GRAPH. Each task has an id and a length.
 *
 * <p>This is created from the input file. Start time and processor are not modified by the A*
 * algorithm, except at the very end in order to output the schedule as a file.
 *
 * @see org.se306.domain.StateTask In contrast, StateTask is used by the A* algorithm to keep track
 *     of the schedule within each state in the STATE TREE and is modified during the A* execution.
 */
public class IOTask {

  private final String id;
  private final int taskLength;

  // --- The following fields are for outputting as a file only ---
  // -1 means unassigned
  private int startTime = -1;
  private int processor = -1; // 1-indexed

  // --- The following fields are used for caching only ---
  // -1 means unassigned
  private int bottomLevel = -1;

  public IOTask(String id, int taskLength) {
    this.id = id;
    this.taskLength = taskLength;
  }

  // Constructor for testing purposes
  public IOTask(String id, int taskLength, int startTime, int processor) {
    this.id = id;
    this.taskLength = taskLength;
    this.startTime = startTime;
    this.processor = processor;
  }

  public String getId() {
    return id;
  }

  public int getTaskLength() {
    return taskLength;
  }

  public int getStartTime() {
    return startTime;
  }

  public void setStartTime(int startTime) {
    this.startTime = startTime;
  }

  public int getProcessor() {
    return processor;
  }

  public int getEndTime() {
    return startTime + taskLength;
  }

  public void setProcessor(int processor) {
    this.processor = processor;
  }

  public int getBottomLevel() {
    return bottomLevel;
  }

  public void setBottomLevel(int bottomLevel) {
    this.bottomLevel = bottomLevel;
  }

  @Override
  public String toString() {
    return id;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    IOTask other = (IOTask) obj;
    if (id == null) {
      return false;
    }
    return id.equals(other.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}
