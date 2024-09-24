package org.se306.domain;

public class Task {

  private final String id;
  private final int weight;
  private int startTime = -1; // -1 means not scheduled
  private int processor = -1; // -1 means not scheduled

  public Task(String id, int weight) {
    this.id = id;
    this.weight = weight;
  }

}
