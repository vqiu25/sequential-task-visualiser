package org.se306.domain;

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
