package main.java.com.gmail.archirand;

import main.java.com.gmail.archirand.model.Edge;
import main.java.com.gmail.archirand.model.Node;

public class Main {

    public static void main(String[] args) {

        Node minsk = new Node("Minsk");
        Node zaslavl = new Node("Zaslavl'");
        Node sluck = new Node("Sluck");
        Node dzerzhinsk = new Node("Dzerzhinsk");
        Node grodno = new Node("Grodno");
        Node lida = new Node("Lida");

        linkNodes(minsk, sluck, 104);
        linkNodes(minsk, zaslavl, 27);
        linkNodes(zaslavl, dzerzhinsk, 42);
        linkNodes(zaslavl, grodno, 260);
        linkNodes(sluck, dzerzhinsk, 98);
        linkNodes(sluck, grodno, 307);
        linkNodes(dzerzhinsk, lida, 173);
        linkNodes(grodno, lida, 113);

        ShortestWay shortestWay = ShortestWay.find(sluck, zaslavl);

        System.out.println(shortestWay);
    }

    private static void linkNodes(Node firstNode, Node secondNode, Integer weight) {
        Edge edge = new Edge(weight);
        firstNode.getEdges().add(edge);
        secondNode.getEdges().add(edge);
        edge.getNodes().add(firstNode);
        edge.getNodes().add(secondNode);
    }
}
