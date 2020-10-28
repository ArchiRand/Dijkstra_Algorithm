package main.java.com.gmail.archirand;

import lombok.NonNull;
import main.java.com.gmail.archirand.model.Edge;
import main.java.com.gmail.archirand.model.Node;

import java.util.*;
import java.util.stream.Collectors;

/*
    https://prog-cpp.ru/deikstra/
 */
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
        haveToProcess.add(start);
        start.setCost(0);
        processGraph();
        return shortestWay(start, finish);
    }

    private static void processGraph() {
        while (existUnprocessedNodes()) {
            Node node = haveToProcess.poll();
            if (Objects.nonNull(node)) {
                List<Edge> edges = node.getEdges();
                while (existUnprocessedEdges(edges)) {
                    unprocessedShortestEdge(node).ifPresent(unprocessedShortestEdge -> processEdge(node, unprocessedShortestEdge));
                }
                node.setProcessed(true);
            }
        }
    }

    private static boolean existUnprocessedNodes() {
        return !haveToProcess.isEmpty();
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

    private static void processEdge(Node node, Edge unprocessedShortestEdge) {
        List<Node> neighbors = unprocessedShortestEdge.getNodes();
        neighbors.forEach(neighbour -> {
            if (!node.equals(neighbour)) {
                int distanceToNode = calculateDistance(node, unprocessedShortestEdge);
                if (distanceToNode < neighbour.getCost()) {
                    neighbour.setCost(distanceToNode);
                    childToParent.put(neighbour, node);
                }
                haveToProcess.add(neighbour);
            }
        });
        unprocessedShortestEdge.setProcessed(true);
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
        if (Objects.isNull(distance)) {
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
