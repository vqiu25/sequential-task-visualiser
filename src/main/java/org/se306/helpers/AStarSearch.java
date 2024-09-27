package org.se306.helpers;

import org.jgrapht.Graph;

import java.util.*;

public class AStarSearch<V, E> {

    // Graph, heuristic, and task execution times
    private Graph<V, E> graph;
    private Heuristic<V> heuristic;
    private Map<V, Double> taskExecutionTime;

    // Constructor to initiate everything for our A*Search
    public AStarSearch(Graph<V, E> graph, Heuristic<V> heuristic, Map<V, Double> taskExecutionTime) {
        this.graph = graph;
        this.heuristic = heuristic;
        this.taskExecutionTime = taskExecutionTime;  // Assign node execution times
    }

    // This method returns the shortest path from the "start" node to the "goal" node
    public List<V> findPath(V start, V goal) {
        Set<V> closedSet = new HashSet<>();  // Set of explored nodes
        PriorityQueue<Node<V>> openSet = new PriorityQueue<>(Comparator.comparingDouble(n -> n.fScore));
        //This allows us to reconstruct the path later by knowing where it originated
        Map<V, V> cameFrom = new HashMap<>();  // To reconstruct the path
        //The path cost
        Map<V, Double> gScore = new HashMap<>();
        gScore.put(start, 0.0);
        //The fScore is the A* accounting for both path cost and heuristic
        Map<V, Double> fScore = new HashMap<>();  // Estimated total cost (g + h)
        fScore.put(start, heuristic.estimate(start, goal));

        openSet.add(new Node<>(start, fScore.get(start)));

        while (!openSet.isEmpty()) {
            V current = openSet.poll().vertex;

            if (current.equals(goal)) {
                return reconstructPath(cameFrom, current, start);  // Goal reached
            }

            closedSet.add(current);

            for (E edge : graph.outgoingEdgesOf(current)) {
                V neighbor = graph.getEdgeTarget(edge);

                //skip already explored nodes
                if (closedSet.contains(neighbor)) {
                    continue;
                }

                // Calculate tentativeGScore as: g(current) + edge weight + node weight of neighbor
                double tentativeGScore = gScore.getOrDefault(current, Double.POSITIVE_INFINITY)
                        + graph.getEdgeWeight(edge)  // Communication cost
                        + taskExecutionTime.getOrDefault(neighbor, 0.0);  // Node execution time

                // If this path to neighbor is shorter, update path cost and fScore
                if (!gScore.containsKey(neighbor) || tentativeGScore < gScore.get(neighbor)) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeGScore);
                    fScore.put(neighbor, tentativeGScore + heuristic.estimate(neighbor, goal));
                    openSet.add(new Node<>(neighbor, fScore.get(neighbor)));
                }
            }
        }
        //If no path exists then return an empty list
        return Collections.emptyList();
    }

    //This method uses the logic of A* to find the "optimal" task order (depends on a lot of factors though)
    public List<V> findOptimalTaskOrder() {
        // Perform A* on the graph to find the best task order following dependencies
        PriorityQueue<Node<V>> openSet = new PriorityQueue<>(Comparator.comparingDouble(n -> n.fScore));
        Set<V> closedSet = new HashSet<>();
        Map<V, V> cameFrom = new HashMap<>();
        Map<V, Double> gScore = new HashMap<>();

        //For testing purposes
        int nodesExpanded = 0;

        // We will start from tasks with no incoming dependencies (root)
        for (V startTask : graph.vertexSet()) {
            if (graph.incomingEdgesOf(startTask).isEmpty()) {
                gScore.put(startTask, 0.0);
                openSet.add(new Node<>(startTask, heuristic.estimate(startTask, null)));
            }
        }

        List<V> taskOrder = new ArrayList<>();
        while (!openSet.isEmpty()) {
            V currentTask = openSet.poll().vertex;

            if (closedSet.contains(currentTask)) continue;
            closedSet.add(currentTask);
            taskOrder.add(currentTask);
            nodesExpanded++;

            // Expand neighbors (outgoing tasks)
            for (E edge : graph.outgoingEdgesOf(currentTask)) {
                V neighbor = graph.getEdgeTarget(edge);

                // Check if all predecessors of neighbor have been scheduled
                boolean allPredecessorsScheduled = true;
                for (E incomingEdge : graph.incomingEdgesOf(neighbor)) {
                    V predecessor = graph.getEdgeSource(incomingEdge);
                    if (!closedSet.contains(predecessor)) {
                        allPredecessorsScheduled = false;
                        break;
                    }
                }
                //If not skip for now
                if (!allPredecessorsScheduled) {
                    continue;
                }
                //account for communication cost and weight as well
                double tentativeGScore = gScore.getOrDefault(currentTask, Double.POSITIVE_INFINITY)
                        + graph.getEdgeWeight(edge)
                        + taskExecutionTime.getOrDefault(neighbor, 0.0);

                if (!gScore.containsKey(neighbor) || tentativeGScore < gScore.get(neighbor)) {
                    cameFrom.put(neighbor, currentTask);
                    gScore.put(neighbor, tentativeGScore);
                    openSet.add(new Node<>(neighbor, tentativeGScore + heuristic.estimate(neighbor, null)));
                }
            }
        }
        System.out.println("Nodes Expanded: " + nodesExpanded);
        //Return task order based on the A* logic
        return taskOrder;
    }


    // Helper method to reconstruct the path from the start node to the goal node
    private List<V> reconstructPath(Map<V, V> cameFrom, V current, V start) {
        List<V> path = new ArrayList<>();
        while (cameFrom.containsKey(current)) {
            path.add(current);
            current = cameFrom.get(current);
        }
        path.add(start);
        Collections.reverse(path);
        return path;
    }

    // Node class for priority queue
    private static class Node<V> {
        V vertex;
        double fScore;

        Node(V vertex, double fScore) {
            this.vertex = vertex;
            this.fScore = fScore;
        }
    }

    // Heuristic interface where we can make our A* more efficent with the right heursitic
    // Must be careful with this though
    public interface Heuristic<V> {
        double estimate(V node, V goal);
    }
}
