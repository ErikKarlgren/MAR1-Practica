package ucm.erikkarl;


import java.util.*;

/**
 * Implementar o Java o en C++ un algoritmo, que dado un grafo dirigido,
 * detecte si tiene o no ciclos. En caso de ser aciclico, ha de listar
 * sus vertices en orden topologico. Si hay mas de uno posible, los
 * puede listar en cualquiera de ellos. En caso de ser cıclico, ha de
 * listar sus componentes fuertemente conexas como conjuntos de vertices.
 */
public final class Exercise4 {

    private Exercise4() {
    }

    public enum Color {
        WHITE,
        GRAY,
        BLACK
    }

    public static List<Either<Integer, List<Integer>>> solve(Graph<Integer> graph) {
        try {
            return topologicalOrdering(graph);
        } catch (IllegalArgumentException var4) {
            return kosarajusAlgorithm(graph);
        }
    }

    private static Map<Integer, Color> initColors(Graph<Integer> graph) {
        Map<Integer, Color> colors = new LinkedHashMap<>();

        for (Map.Entry<Integer, Set<Integer>> entry : graph)
            colors.put(entry.getKey(), Color.WHITE);

        return colors;
    }

    private static Optional<Integer> firstUnvisitedNode(Map<Integer, Color> colors){
        var node = colors.entrySet()
                .stream()
                .filter(x -> x.getValue() == Color.WHITE)
                .findFirst();
        return node.map(Map.Entry::getKey);
    }

    private static LinkedList<Either<Integer, List<Integer>>> topologicalOrdering(Graph<Integer> graph) {
        var result = new LinkedList<Either<Integer, List<Integer>>>();
        var colors = initColors(graph);

        while (colors.values().stream().anyMatch(color -> color == Color.WHITE)) {
            Integer node = firstUnvisitedNode(colors).get();
            topologicalDFSVisit(graph, node, colors, result);
        }
        return result;
    }

    private static void topologicalDFSVisit(Graph<Integer> graph, int node, Map<Integer, Color> colors, LinkedList<Either<Integer, List<Integer>>> topologicalOrder) {
        colors.put(node, Color.GRAY);

        for (Integer adj : graph.adjacentNodes(node)) {
            switch (colors.get(adj)) {
                case WHITE -> topologicalDFSVisit(graph, adj, colors, topologicalOrder);
                case GRAY -> throw new IllegalArgumentException("There's a loop, so there's no topological order");
                case BLACK -> {}
            }
        }

        colors.put(node, Color.BLACK);
        topologicalOrder.addFirst(new Either.Left<>(node));
    }

    private static LinkedList<Either<Integer, List<Integer>>> kosarajusAlgorithm(Graph<Integer> graph) {
        var result = new LinkedList<Either<Integer, List<Integer>>>();
        var colors = initColors(graph);
        var nodesOrderedByFinishTime = new LinkedList<Integer>();

        // 1er recorrido del grafo
        while (colors.values().stream().anyMatch(color -> color == Color.WHITE)) {
            Integer node = firstUnvisitedNode(colors).get();
            kosarajusDFSVisit(graph, node, colors, nodesOrderedByFinishTime);
        }

        // 2do recorrido del grafo
        var transpose = transposeGraph(graph);
        colors = initColors(graph);

        while (!nodesOrderedByFinishTime.isEmpty()) {
            var node = nodesOrderedByFinishTime.pop();
            if (colors.get(node) == Color.WHITE) {
                var component = new LinkedList<Integer>();
                kosarajusDFSVisit(transpose, node, colors, component);
                if (component.size() > 1)
                    result.add(new Either.Right<>(component));
                else
                    result.add(new Either.Left<>(component.getFirst()));
                //result.add(new Either.Right<>(component));
            }
        }
        return result;
    }

    private static void kosarajusDFSVisit(Graph<Integer> graph, int node, Map<Integer, Color> colors, LinkedList<Integer> nodesList) {
        colors.put(node, Exercise4.Color.GRAY);

        for (Integer adj : graph.adjacentNodes(node)) {
            if (colors.get(adj) == Color.WHITE) {
                kosarajusDFSVisit(graph, adj, colors, nodesList);
            }
        }
        colors.put(node, Color.BLACK);
        nodesList.push(node);
    }

    private static Graph<Integer> transposeGraph(Graph<Integer> graph) {
        var transpose = new Graph<Integer>();

        for (Map.Entry<Integer, Set<Integer>> entry : graph) {
            var node = entry.getKey();
            var adjacents = entry.getValue();
            // Nodes with no other adjacent nodes still exist in the transpose graph
            transpose.addNode(node);
            for (Integer adj : adjacents) {
                transpose.addEdges(adj, List.of(node));
            }
        }
        return transpose;
    }
}
