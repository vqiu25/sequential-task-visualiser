package org.se306.domain;

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
  private final int processor;
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

}
