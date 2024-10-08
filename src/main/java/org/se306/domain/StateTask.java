package org.se306.domain;

public class StateTask {

  private final int processor;
  private final int startTime;
  private final int duration;

  public StateTask(int processor, int startTime, int duration) {
    this.processor = processor;
    this.startTime = startTime;
    this.duration = duration;
  }

  public int getProcessor() {
    return processor;
  }

  public int getStartTime() {
    return startTime;
  }

  public int getDuration() {
    return duration;
  }

  public int getEndTime() {
    return startTime + duration;
  }

}
