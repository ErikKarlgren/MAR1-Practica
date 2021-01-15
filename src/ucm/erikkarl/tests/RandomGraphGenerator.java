package ucm.erikkarl.tests;

import ucm.erikkarl.Graph;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Crea un grafo aleatorio con un numero de nodos determinado.
 * Esta clase deberia funcionar mas o menos bien para grafos grandes, aunque
 * el coste de creacion esta en el orden de O(n^2) si n es el numero de nodos.
 */
public final class RandomGraphGenerator {
    private static final Random RANDOM = new Random();

    private final Integer nodesInGraph;
    private final List<Integer> range;

    RandomGraphGenerator(int nodes) {
        assert nodes >= 0;
        nodesInGraph = nodes;
        range = IntStream.rangeClosed(1, nodesInGraph).boxed().collect(Collectors.toUnmodifiableList());
    }

    Graph<Integer> createGraph() {
        var graph = new Graph<Integer>();
        var nodes = createUnusedNodesList();

        for (Integer i : range) {
            graph.addEdges(i, randomQuantityOfRandomNodes(i, nodes));
        }
        return graph;
    }

    private List<Integer> randomQuantityOfRandomNodes(Integer node, LinkedList<Integer> unusedNodes) {
        var usedNodes = new LinkedList<Integer>();

        // Creates good enough test files for small number of nodes, so don't bother changing this...
        var log2ofNodesInGraph = Math.log(nodesInGraph) / Math.log(2);
        int maxQuantity = Math.max(1, (int) (log2ofNodesInGraph));
        if (maxQuantity == 1 && nodesInGraph > 1) maxQuantity = 2;
        double prob = (1.0 / log2ofNodesInGraph) * 2;
        int quantity = RANDOM.nextInt(maxQuantity) / (RANDOM.nextDouble() < prob ? 1 : 2);

        for (int i = 0; i < quantity; i++) { // O(n)
            if (unusedNodes.isEmpty())
                unusedNodes = createUnusedNodesList();
            var n = unusedNodes.pop();
            if (n != node.intValue())
                usedNodes.add(n);
        }
        assert !usedNodes.isEmpty();
        return usedNodes;
    }

    // intento de optimizar el funcionamiento: asi no creamos una lista nueva para cada nodo
    private LinkedList<Integer> createUnusedNodesList() {
        var unusedNodes = new LinkedList<>(range);
        Collections.shuffle(unusedNodes, RANDOM);
        return unusedNodes;
    }
}
