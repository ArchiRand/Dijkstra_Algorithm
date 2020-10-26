package main.java.com.gmail.archirand;

import main.java.com.gmail.archirand.model.Edge;
import main.java.com.gmail.archirand.model.Node;
import lombok.NonNull;

import java.util.*;
import java.util.stream.Collectors;

public class ShortestWay {

    private Node start;
    private Node finish;
    private Integer distance;
    private List<Node> nodesFromStartToFinish;
    private final static Map<Node, Node> childToParent = new HashMap<>();
    private final static LinkedList<Node> haveToProcess = new LinkedList<>();

    private ShortestWay() {
    }

    private ShortestWay(Node start, Node finish, List<Node> nodesFromStartToFinish) {
        this.start = start;
        this.finish = finish;
        this.distance = finish.getCost();
        this.nodesFromStartToFinish = nodesFromStartToFinish;
    }

    public static ShortestWay find(@NonNull Node start, @NonNull Node finish) {
        childToParent.clear();
        haveToProcess.clear();
        start.setCost(0);
        processGraph(start);
        return shortestWay(start, finish);
    }

    private static void processGraph(Node start) {
        Node node = start;
        while (existUnprocessedNodes(node)) {
            List<Edge> edges = node.getEdges();
            while (existUnprocessedEdges(edges)) {
                Optional<Edge> optionalEdge = unprocessedShortestEdge(node);
                if (optionalEdge.isPresent()) {
                    Edge unprocessedShortestEdge = optionalEdge.get();
                    List<Node> neighbors = unprocessedShortestEdge.getNodes();
                    Node parent = node;
                    neighbors.forEach(neighbour -> {
                        if (!parent.equals(neighbour)) {
                            int distanceToNode = calculateDistance(parent, unprocessedShortestEdge);
                            if (distanceToNode < neighbour.getCost()) {
                                neighbour.setCost(distanceToNode);
                                childToParent.put(neighbour, parent);
                            }
                            haveToProcess.add(neighbour);
                        }
                    });
                    unprocessedShortestEdge.setProcessed(true);
                }
            }
            node.setProcessed(true);
            node = haveToProcess.poll();
        }
    }

    private static boolean existUnprocessedNodes(Node node) {
        return Objects.nonNull(node) && !node.isProcessed();
    }

    private static boolean existUnprocessedEdges(List<Edge> edges) {
        return Objects.nonNull(edges) && edges.stream().anyMatch(edge -> !edge.isProcessed());
    }

    private static Optional<Edge> unprocessedShortestEdge(Node node) {
        return node.getEdges()
                .stream()
                .filter(edge -> !edge.isProcessed())
                .min(Comparator.comparing(Edge::getWeight));
    }

    private static int calculateDistance(Node parent, Edge edge) {
        return parent.getCost() + edge.getWeight();
    }

    private static ShortestWay shortestWay(Node start, Node finish) {
        if (Objects.nonNull(childToParent.get(finish))) {
            return new ShortestWay(start, finish, getPathFromStartToFinish(finish));
        }
        return new ShortestWay();
    }

    private static List<Node> getPathFromStartToFinish(Node finish) {
        LinkedList<Node> pathFromStartToFinish = new LinkedList<>();
        pathFromStartToFinish.add(finish);
        Node node = childToParent.get(finish);
        while (Objects.nonNull(node)) {
            pathFromStartToFinish.addFirst(node);
            node = childToParent.get(node);
        }
        return pathFromStartToFinish;
    }

    @Override
    public String toString() {
        if (Objects.isNull(distance) && Objects.isNull(nodesFromStartToFinish)) {
            return "Shortest way not found";
        }
        return "Shortest way from " + start.getName() + " to " + finish.getName() + " is " + distance + ".\n"
                + "It's include " + extractNodeName();
    }

    private String extractNodeName() {
        return nodesFromStartToFinish.stream()
                .map(Node::getName)
                .collect(Collectors.joining(", ", "[", "]"));
    }

}
