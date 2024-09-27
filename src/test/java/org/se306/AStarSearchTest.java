package org.se306;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.traverse.TopologicalOrderIterator;
import org.junit.jupiter.api.Test;
import org.se306.helpers.AStarSearch;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class AStarSearchTest {

    // Heuristic always returns 0 (this basically makes it  Dijkstra's algorithm) for testing purposes
    private AStarSearch.Heuristic<String> zeroHeuristic = (node, goal) -> 0.0;

    @Test
    public void testNoPathExists() {
        // Create a directed, weighted graph
        SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);

        // Add vertices
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addVertex("D");

        // Add edges with weights
        graph.setEdgeWeight(graph.addEdge("A", "B"), 3);
        graph.setEdgeWeight(graph.addEdge("A", "C"), 3);

        // No edge connecting B or C to D

        // Define task execution times (node weights)
        Map<String, Double> taskExecutionTimes = new HashMap<>();
        taskExecutionTimes.put("A", 2.0);
        taskExecutionTimes.put("B", 2.0);
        taskExecutionTimes.put("C", 2.0);
        taskExecutionTimes.put("D", 3.0);

        // Create an A* search instance with a zero heuristic
        AStarSearch<String, DefaultWeightedEdge> aStarSearch = new AStarSearch<>(graph, zeroHeuristic, taskExecutionTimes);

        // Find the shortest path from A to D, which doesn't exist
        var path = aStarSearch.findPath("A", "D");

        // Expected no path
        assertEquals("[]", path.toString(), "There should be no path from A to D");
        System.out.println("Shortest Path from A to D: " + path);
    }

    @Test
    public void testExampleInput() {
        // Create a directed, weighted graph
        SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);

        // Add vertices
        graph.addVertex("0");
        graph.addVertex("1");
        graph.addVertex("2");
        graph.addVertex("3");
        graph.addVertex("4");
        graph.addVertex("5");
        graph.addVertex("6");
        graph.addVertex("7");
        graph.addVertex("8");
        graph.addVertex("9");
        graph.addVertex("10");

        // Add edges with weights
        graph.setEdgeWeight(graph.addEdge("0", "1"), 9);
        graph.setEdgeWeight(graph.addEdge("0", "2"), 7);
        graph.setEdgeWeight(graph.addEdge("0", "3"), 4);
        graph.setEdgeWeight(graph.addEdge("1", "4"), 10);
        graph.setEdgeWeight(graph.addEdge("1", "5"), 7);
        graph.setEdgeWeight(graph.addEdge("1", "6"), 5);
        graph.setEdgeWeight(graph.addEdge("2", "7"), 5);
        graph.setEdgeWeight(graph.addEdge("2", "8"), 3);
        graph.setEdgeWeight(graph.addEdge("2", "9"), 10);
        graph.setEdgeWeight(graph.addEdge("3", "10"), 4);

        // Define task execution times (node weights)
        Map<String, Double> taskExecutionTimes = new HashMap<>();
        taskExecutionTimes.put("0", 50.0);
        taskExecutionTimes.put("1", 70.0);
        taskExecutionTimes.put("2", 90.0);
        taskExecutionTimes.put("3", 100.0);
        taskExecutionTimes.put("4", 40.0);
        taskExecutionTimes.put("5", 20.0);
        taskExecutionTimes.put("6", 100.0);
        taskExecutionTimes.put("7", 80.0);
        taskExecutionTimes.put("8", 50.0);
        taskExecutionTimes.put("9", 20.0);
        taskExecutionTimes.put("10", 20.0);

        // Use zero heuristic for this test case
        AStarSearch.Heuristic<String> heuristic = (node, goal) -> 0.0;

        // Create an A* search instance
        AStarSearch<String, DefaultWeightedEdge> aStarSearch = new AStarSearch<>(graph, heuristic, taskExecutionTimes);

        // Test the path from node "0" to "10"
        var path = aStarSearch.findPath("0", "10");

        // Expected path: 0 -> 3 -> 10
        assertEquals("[0, 3, 10]", path.toString(), "Shortest path from 0 to 10 should be [0, 3, 10]");
        System.out.println("Shortest Path from 0 to 10: " + path);
    }


    @Test
    public void testExampleInput2() {
        // Create a directed, weighted graph
        SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);

        // Add vertices
        graph.addVertex("0");
        graph.addVertex("1");
        graph.addVertex("2");
        graph.addVertex("3");
        graph.addVertex("4");
        graph.addVertex("5");
        graph.addVertex("6");
        graph.addVertex("7");

        // Define task execution times (node weights)
        Map<String, Double> taskExecutionTimes = new HashMap<>();
        taskExecutionTimes.put("0", 35.0);
        taskExecutionTimes.put("1", 88.0);
        taskExecutionTimes.put("2", 176.0);
        taskExecutionTimes.put("3", 159.0);
        taskExecutionTimes.put("4", 176.0);
        taskExecutionTimes.put("5", 141.0);
        taskExecutionTimes.put("6", 141.0);
        taskExecutionTimes.put("7", 53.0);

        // Add edges with weights
        graph.setEdgeWeight(graph.addEdge("0", "1"), 3);
        graph.setEdgeWeight(graph.addEdge("0", "2"), 9);
        graph.setEdgeWeight(graph.addEdge("0", "3"), 7);
        graph.setEdgeWeight(graph.addEdge("0", "4"), 5);
        graph.setEdgeWeight(graph.addEdge("0", "6"), 4);
        graph.setEdgeWeight(graph.addEdge("0", "7"), 9);
        graph.setEdgeWeight(graph.addEdge("1", "4"), 10);
        graph.setEdgeWeight(graph.addEdge("1", "7"), 6);
        graph.setEdgeWeight(graph.addEdge("2", "4"), 8);
        graph.setEdgeWeight(graph.addEdge("2", "5"), 6);
        graph.setEdgeWeight(graph.addEdge("2", "7"), 3);
        graph.setEdgeWeight(graph.addEdge("3", "5"), 5);
        graph.setEdgeWeight(graph.addEdge("3", "6"), 8);
        graph.setEdgeWeight(graph.addEdge("4", "6"), 2);
        graph.setEdgeWeight(graph.addEdge("5", "7"), 4);
        graph.setEdgeWeight(graph.addEdge("6", "7"), 8);

        // Use zero heuristic for this test case
        AStarSearch.Heuristic<String> heuristic = (node, goal) -> 0.0;

        // Create an A* search instance
        AStarSearch<String, DefaultWeightedEdge> aStarSearch = new AStarSearch<>(graph, heuristic, taskExecutionTimes);

        // Test the path from node "0" to "5"
        var path = aStarSearch.findPath("0", "5");

        // Expected path: [0, 3, 5]
        assertEquals("[0, 3, 5]", path.toString(), "Shortest path from 0 to 5 should be [0, 3, 5]");
        System.out.println("Shortest Path from 0 to 5: " + path);
    }


    @Test
    public void testHeuristicInfluence() {
        // Create a directed, weighted graph
        Graph<String, DefaultWeightedEdge> graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);

        // Add remaining vertices
        graph.addVertex("0");
        graph.addVertex("1");
        graph.addVertex("2");
        graph.addVertex("3");
        graph.addVertex("4");
        graph.addVertex("5");

        // Add edges with weights
        graph.setEdgeWeight(graph.addEdge("0", "1"), 1);  // 0 -> 1 (Communication cost = 1)
        graph.setEdgeWeight(graph.addEdge("0", "2"), 8);  // 0 -> 2 (Communication cost = 8)
        graph.setEdgeWeight(graph.addEdge("0", "3"), 3);  // 0 -> 3 (Communication cost = 3)
        graph.setEdgeWeight(graph.addEdge("1", "3"), 1);  // 1 -> 3 (Communication cost = 1)
        graph.setEdgeWeight(graph.addEdge("2", "3"), 1);  // 2 -> 3 (Communication cost = 1)
        graph.setEdgeWeight(graph.addEdge("3", "4"), 4);  // 3 -> 4 (Communication cost = 4)
        graph.setEdgeWeight(graph.addEdge("3", "5"), 2);  // 3 -> 5 (Communication cost = 2)
        graph.setEdgeWeight(graph.addEdge("4", "5"), 1);  // 4 -> 5 (Communication cost = 1)

        // Define task execution times
        Map<String, Double> taskExecutionTimes = new HashMap<>();
        taskExecutionTimes.put("0", 2.0);
        taskExecutionTimes.put("1", 10.0);
        taskExecutionTimes.put("2", 3.0);
        taskExecutionTimes.put("3", 5.0);
        taskExecutionTimes.put("4", 1.0);
        taskExecutionTimes.put("5", 2.0);

        // Measure time for heuristic with a constant 0 for testing
        AStarSearch.Heuristic<String> zeroHeuristic = (node, goal) -> 0.0;
        AStarSearch<String, DefaultWeightedEdge> aStarWithZeroHeuristic = new AStarSearch<>(graph, zeroHeuristic, taskExecutionTimes);

        //Time with no Heuristic
        long startTimeZeroHeuristic = System.nanoTime();
        List<String> pathZeroHeuristic = aStarWithZeroHeuristic.findPath("0", "5");
        long endTimeZeroHeuristic = System.nanoTime();

        long durationZeroHeuristic = endTimeZeroHeuristic - startTimeZeroHeuristic;

        // Ensure the result is the correct output that we know is true
        assertEquals(Arrays.asList("0", "3", "5"), pathZeroHeuristic, "Path with zero heuristic should be optimal");

        // Measure time for custom heuristic here using the goal's execution time as a test
        AStarSearch.Heuristic<String> customHeuristic = (node, goal) -> taskExecutionTimes.getOrDefault(goal, 0.0);
        AStarSearch<String, DefaultWeightedEdge> aStarWithCustomHeuristic = new AStarSearch<>(graph, customHeuristic, taskExecutionTimes);

        //Time with Custom Heuristic
        long startTimeCustomHeuristic = System.nanoTime();  // Start timer
        List<String> pathCustomHeuristic = aStarWithCustomHeuristic.findPath("0", "5");
        long endTimeCustomHeuristic = System.nanoTime();    // End timer

        long durationCustomHeuristic = endTimeCustomHeuristic - startTimeCustomHeuristic;

        // Ensure the result is the same with the custom heuristic
        assertEquals(Arrays.asList("0", "3", "5"), pathCustomHeuristic, "Path with custom heuristic should be optimal");

        // Output the execution times for comparison
        System.out.println("Execution time with Zero Heuristic (ns): " + durationZeroHeuristic);
        System.out.println("Execution time with Custom Heuristic (ns): " + durationCustomHeuristic);

        // Won't always be true though also since these graph inputs are so small sometimes it's not more efficent
        assertTrue(durationCustomHeuristic <= durationZeroHeuristic, "Custom heuristic should be faster or equal in performance");
    }


    @Test
    public void testOptimalTaskOrder1() {
        Graph<String, DefaultWeightedEdge> graph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);

        graph.addVertex("0");
        graph.addVertex("1");
        graph.addVertex("2");
        graph.addVertex("3");
        graph.addVertex("4");
        graph.addVertex("5");
        graph.addVertex("6");
        graph.addVertex("7");
        graph.addVertex("8");
        graph.addVertex("9");

        graph.setEdgeWeight(graph.addEdge("0", "3"), 34);
        graph.setEdgeWeight(graph.addEdge("0", "4"), 24);
        graph.setEdgeWeight(graph.addEdge("0", "9"), 44);
        graph.setEdgeWeight(graph.addEdge("1", "2"), 48);
        graph.setEdgeWeight(graph.addEdge("1", "5"), 19);
        graph.setEdgeWeight(graph.addEdge("1", "6"), 39);
        graph.setEdgeWeight(graph.addEdge("2", "3"), 10);
        graph.setEdgeWeight(graph.addEdge("2", "7"), 48);
        graph.setEdgeWeight(graph.addEdge("2", "8"), 48);
        graph.setEdgeWeight(graph.addEdge("4", "6"), 10);
        graph.setEdgeWeight(graph.addEdge("4", "7"), 48);
        graph.setEdgeWeight(graph.addEdge("4", "8"), 48);
        graph.setEdgeWeight(graph.addEdge("4", "9"), 39);
        graph.setEdgeWeight(graph.addEdge("6", "7"), 15);
        graph.setEdgeWeight(graph.addEdge("6", "8"), 39);
        graph.setEdgeWeight(graph.addEdge("6", "9"), 29);
        graph.setEdgeWeight(graph.addEdge("7", "8"), 15);
        graph.setEdgeWeight(graph.addEdge("7", "9"), 34);
        graph.setEdgeWeight(graph.addEdge("8", "9"), 39);

        Map<String, Double> taskExecutionTimes = new HashMap<>();
        taskExecutionTimes.put("0", 6.0);
        taskExecutionTimes.put("1", 5.0);
        taskExecutionTimes.put("2", 5.0);
        taskExecutionTimes.put("3", 10.0);
        taskExecutionTimes.put("4", 3.0);
        taskExecutionTimes.put("5", 7.0);
        taskExecutionTimes.put("6", 8.0);
        taskExecutionTimes.put("7", 3.0);
        taskExecutionTimes.put("8", 8.0);
        taskExecutionTimes.put("9", 8.0);

        AStarSearch.Heuristic<String> zeroHeuristic = (node, goal) -> 0.0;

        AStarSearch<String, DefaultWeightedEdge> aStarSearch = new AStarSearch<>(graph, zeroHeuristic, taskExecutionTimes);

        List<String> optimalOrder = aStarSearch.findOptimalTaskOrder();

        assertNotNull(optimalOrder);
        System.out.println("Optimal Task Execution Order: " + optimalOrder);
    }

    @Test
    public void testOptimalTaskOrder2() {
        Graph<String, DefaultWeightedEdge> graph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);

        graph.addVertex("0");
        graph.addVertex("1");
        graph.addVertex("2");
        graph.addVertex("3");
        graph.addVertex("4");
        graph.addVertex("5");
        graph.addVertex("6");
        graph.addVertex("7");
        graph.addVertex("8");
        graph.addVertex("9");
        graph.addVertex("10");

        graph.setEdgeWeight(graph.addEdge("0", "1"), 9);
        graph.setEdgeWeight(graph.addEdge("0", "2"), 7);
        graph.setEdgeWeight(graph.addEdge("0", "3"), 4);
        graph.setEdgeWeight(graph.addEdge("1", "4"), 10);
        graph.setEdgeWeight(graph.addEdge("1", "5"), 7);
        graph.setEdgeWeight(graph.addEdge("1", "6"), 5);
        graph.setEdgeWeight(graph.addEdge("2", "7"), 5);
        graph.setEdgeWeight(graph.addEdge("2", "8"), 3);
        graph.setEdgeWeight(graph.addEdge("2", "9"), 10);
        graph.setEdgeWeight(graph.addEdge("3", "10"), 4);

        Map<String, Double> taskExecutionTimes = new HashMap<>();
        taskExecutionTimes.put("0", 50.0);
        taskExecutionTimes.put("1", 70.0);
        taskExecutionTimes.put("2", 90.0);
        taskExecutionTimes.put("3", 100.0);
        taskExecutionTimes.put("4", 40.0);
        taskExecutionTimes.put("5", 20.0);
        taskExecutionTimes.put("6", 100.0);
        taskExecutionTimes.put("7", 80.0);
        taskExecutionTimes.put("8", 50.0);
        taskExecutionTimes.put("9", 20.0);
        taskExecutionTimes.put("10", 20.0);

        AStarSearch.Heuristic<String> zeroHeuristic = (node, goal) -> 0.0;

        AStarSearch<String, DefaultWeightedEdge> aStarSearch = new AStarSearch<>(graph, zeroHeuristic, taskExecutionTimes);

        List<String> optimalOrder = aStarSearch.findOptimalTaskOrder();

        assertNotNull(optimalOrder);
        System.out.println("Optimal Task Execution Order: " + optimalOrder);
    }

    @Test
    public void testOptimalTaskOrder3() {
        Graph<String, DefaultWeightedEdge> graph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);

        graph.addVertex("0");
        graph.addVertex("1");
        graph.addVertex("2");
        graph.addVertex("3");
        graph.addVertex("4");
        graph.addVertex("5");
        graph.addVertex("6");
        graph.addVertex("7");
        graph.addVertex("8");

        graph.setEdgeWeight(graph.addEdge("0", "2"), 51);
        graph.setEdgeWeight(graph.addEdge("0", "3"), 22);
        graph.setEdgeWeight(graph.addEdge("0", "4"), 44);
        graph.setEdgeWeight(graph.addEdge("2", "6"), 59);
        graph.setEdgeWeight(graph.addEdge("2", "7"), 15);
        graph.setEdgeWeight(graph.addEdge("2", "8"), 59);
        graph.setEdgeWeight(graph.addEdge("3", "1"), 59);
        graph.setEdgeWeight(graph.addEdge("4", "1"), 66);
        graph.setEdgeWeight(graph.addEdge("5", "1"), 37);
        graph.setEdgeWeight(graph.addEdge("6", "5"), 22);
        graph.setEdgeWeight(graph.addEdge("7", "5"), 59);
        graph.setEdgeWeight(graph.addEdge("8", "5"), 59);

        Map<String, Double> taskExecutionTimes = new HashMap<>();
        taskExecutionTimes.put("0", 10.0);
        taskExecutionTimes.put("1", 7.0);
        taskExecutionTimes.put("2", 6.0);
        taskExecutionTimes.put("3", 7.0);
        taskExecutionTimes.put("4", 5.0);
        taskExecutionTimes.put("5", 9.0);
        taskExecutionTimes.put("6", 2.0);
        taskExecutionTimes.put("7", 2.0);
        taskExecutionTimes.put("8", 7.0);

        AStarSearch.Heuristic<String> zeroHeuristic = (node, goal) -> 0.0;

        AStarSearch<String, DefaultWeightedEdge> aStarSearch = new AStarSearch<>(graph, zeroHeuristic, taskExecutionTimes);

        List<String> optimalOrder = aStarSearch.findOptimalTaskOrder();

        assertNotNull(optimalOrder);
        System.out.println("Optimal Task Execution Order: " + optimalOrder);
    }

}
