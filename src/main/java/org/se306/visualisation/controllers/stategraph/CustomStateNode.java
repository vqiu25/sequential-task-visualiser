package org.se306.visualisation.controllers.stategraph;

// Define the custom object to hold state information
public class CustomStateNode {
  private int taskCount;
  private int stateId;
  private Integer parentStateId; // Nullable, for root node

  public CustomStateNode(int taskCount, int stateId, Integer parentStateId) {
    this.taskCount = taskCount;
    this.stateId = stateId;
    this.parentStateId = parentStateId;
  }

  public int getTaskCount() {
    return taskCount;
  }

  public int getStateId() {
    return stateId;
  }

  public Integer getParentStateId() {
    return parentStateId;
  }
}
