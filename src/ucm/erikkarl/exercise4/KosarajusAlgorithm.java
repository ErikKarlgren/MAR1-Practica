package ucm.erikkarl.exercise4;

import ucm.erikkarl.Either;
import ucm.erikkarl.graph.Graph;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class KosarajusAlgorithm {
    private KosarajusAlgorithm() {
    }

    /**
     * Calcula las componentes conexas del grafo <code>graph</code> usando el algoritmo de Kosaraju.
     * <p>
     * Primero, recorre todo el algoritmo por busqueda en profundidad y va anadiendo los nodos a una lista
     * en el mismo orden que termina de recorrer cada uno.
     * <p>
     * Segundo, calcula el grafo transpuesto de <code>graph</code>, y lo recorre tambien mediante busqueda
     * en profundidad pero empezando por los nodos al final de la lista que se calculo en el primer recorrido.
     * <p>
     * Coste lineal en el numero de vertices (n) y de aristas (a), es decir, O(n+a).
     */
    static LinkedList<Either<Integer, List<Integer>>> solve(Graph<Integer> graph) {
        var result = new LinkedList<Either<Integer, List<Integer>>>();
        var colors = Exercise4.initColors(graph);
        var nodesOrderedByFinishTime = new LinkedList<Integer>();

        // 1er recorrido del grafo
        while (colors.values().stream().anyMatch(color -> color == Color.WHITE)) {
            Integer node = Exercise4.firstUnvisitedNode(colors).get();
            dfsVisit(graph, node, colors, nodesOrderedByFinishTime);
        }

        // 2do recorrido del grafo
        var transpose = transposeGraph(graph);
        colors = Exercise4.initColors(graph);

        while (!nodesOrderedByFinishTime.isEmpty()) {
            var node = nodesOrderedByFinishTime.pop();
            if (colors.get(node) == Color.WHITE) {
                var component = new LinkedList<Integer>();
                dfsVisit(transpose, node, colors, component);
                if (component.size() > 1)
                    result.add(new Either.Right<>(component));
                else
                    result.add(new Either.Left<>(component.getFirst()));
            }
        }
        return result;
    }

    /**
     * Funcion para visitar los nodos de <code>graph</code> en profundidad. Anade a la lista
     * <code>nodesList</code> los nodos que se van recorriendo.
     *
     * @param graph     Grafo que se visita.
     * @param node      Nodo que se esta visitando actualmente.
     * @param colors    Mapa para marcar los nodos visitados.
     * @param nodesList Lista de los nodos visitados.
     */
    private static void dfsVisit(Graph<Integer> graph, int node, Map<Integer, Color> colors, LinkedList<Integer> nodesList) {
        colors.put(node, Color.GRAY);

        for (Integer adj : graph.adjacentNodes(node)) {
            if (colors.get(adj) == Color.WHITE) {
                dfsVisit(graph, adj, colors, nodesList);
            }
        }
        colors.put(node, Color.BLACK);
        nodesList.push(node);
    }

    /**
     * Devuelve un grafo que es el transpuesto de <code>graph</code>.
     */
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
