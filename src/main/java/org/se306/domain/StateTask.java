package org.se306.domain;

public class StateTask {
  public int processor;
  public int startTime;
  public int duration;

  public StateTask(int processor, int startTime, int duration) {
    this.processor = processor;
    this.startTime = startTime;
    this.duration = duration;
  }
}
