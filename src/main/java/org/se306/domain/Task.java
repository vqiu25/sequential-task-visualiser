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

  public void setProcessor(int processor) {
    this.processor = processor;
  }

  public int getFinishTime() {
    return startTime + taskLength;
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
    Task other = (Task) obj;
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
