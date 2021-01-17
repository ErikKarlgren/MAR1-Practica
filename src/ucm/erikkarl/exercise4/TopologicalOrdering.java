package ucm.erikkarl.exercise4;

import ucm.erikkarl.Either;
import ucm.erikkarl.graph.Graph;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TopologicalOrdering {
    private TopologicalOrdering() {
    }

    /**
     * Devuelve una lista con los nodos del grafo en orden topologico. Si encuentra un bucle no puede haber
     * ordenamiento topologico asi que lanza una excepcion (que es lanzada por
     * {@link #dfsVisit(Graph, int, Map, LinkedList)}).
     * <p>
     * Su coste es lineal en el numero de nodos (n) y el de aristas (a), es decir, O(n+a).
     */
    static LinkedList<Either<Integer, List<Integer>>> solve(Graph<Integer> graph) {
        var result = new LinkedList<Either<Integer, List<Integer>>>();
        var colors = Exercise4.initColors(graph);

        while (colors.values().stream().anyMatch(color -> color == Color.WHITE)) {
            Integer node = Exercise4.firstUnvisitedNode(colors).get();
            dfsVisit(graph, node, colors, result);
        }
        return result;
    }

    /**
     * Funcion para visitar los nodos de un grafo <code>graph</code> en profundidad e ir creando
     * una lista con sus nodos en orden topologico. Si encuentra un bucle lanza una excepcion.
     *
     * @param graph            Grafo del que se calcula el orden topologico de sus nodos.
     * @param node             Proximo nodo a visitar.
     * @param colors           Mapa para marcar los nodos sin visitar, los que se estan visitando y aquellos
     *                         cuyos hijos ya se han visitado.
     * @param topologicalOrder Lista de nodos siguiendo su orden topologico.
     */
    private static void dfsVisit(Graph<Integer> graph,
                                 int node,
                                 Map<Integer, Color> colors,
                                 LinkedList<Either<Integer, List<Integer>>> topologicalOrder) {
        colors.put(node, Color.GRAY);

        for (Integer adj : graph.adjacentNodes(node)) {
            switch (colors.get(adj)) {
                case WHITE -> dfsVisit(graph, adj, colors, topologicalOrder);
                case GRAY -> throw new IllegalArgumentException("There's a loop, so there's no topological order");
                default -> { // BLACK
                }
            }
        }

        colors.put(node, Color.BLACK);
        topologicalOrder.addFirst(new Either.Left<>(node));
    }
}
