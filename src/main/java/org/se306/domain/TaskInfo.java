package org.se306.domain;

public class TaskInfo {
  public int processor;
  public int startTime;
  public int duration;

  public TaskInfo(int processor, int startTime, int duration) {
    this.processor = processor;
    this.startTime = startTime;
    this.duration = duration;
  }
}
