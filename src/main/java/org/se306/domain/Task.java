package org.se306.domain;

public class Task {

  private final String id;
  private final int taskLength;
  private int startTime = -1; // -1 means not scheduled
  private int processor = -1; // -1 means not scheduled

  public Task(String id, int taskLength) {
    this.id = id;
    this.taskLength = taskLength;
  }

  public int getTaskLength() {
    return taskLength;
  }
}
