package ucm.erikkarl;


import java.util.*;

/**
 * <b>Enunciado:</b> <p>
 * Implementar o Java o en C++ un algoritmo, que dado un grafo dirigido,
 * detecte si tiene o no ciclos. En caso de ser aciclico, ha de listar
 * sus vertices en orden topologico. Si hay mas de uno posible, los
 * puede listar en cualquiera de ellos. En caso de ser ciclico, ha de
 * listar sus componentes fuertemente conexas como conjuntos de vertices.
 * <p>
 * <b>Funcionamiento:</b><p>
 * Empieza calculando el orden topologico de los nodos de un grafo. Si no
 * encuentra ningun bucle devuelve una lista que lo represente. En caso
 * contrario calcula sus componentes conexas mediante el algoritmo de
 * Kosaraju.
 */
public final class Exercise4 {

    private Exercise4() {
    }

    /**
     * Devuelve una lista de los nodos del grafo en orden topologico si este no tiene
     * bucles. En caso contrario, devuelve una lista con sus componentes fuertemente
     * conexas.
     *
     * @param graph Grafo sobre el cual se aplica el algoritmo.
     * @return
     */
    public static List<Either<Integer, List<Integer>>> solve(Graph<Integer> graph) {
        try {
            return topologicalOrdering(graph);
        } catch (IllegalArgumentException var4) {
            return kosarajusAlgorithm(graph);
        }
    }

    /**
     * Crea un mapa de nodos a colores. Se usa para indicar si un nodo esta sin visitar,
     * se esta visitando o se ha visitado este y todos sus adyacentes.
     */
    private static Map<Integer, Color> initColors(Graph<Integer> graph) {
        Map<Integer, Color> colors = new LinkedHashMap<>();

        for (Map.Entry<Integer, Set<Integer>> entry : graph)
            colors.put(entry.getKey(), Color.WHITE);

        return colors;
    }

    /**
     * Devuelve el primer nodo que queda sin visitar siguiendo el orden definido por
     * el mapa <code>colors</code>.
     */
    private static Optional<Integer> firstUnvisitedNode(Map<Integer, Color> colors) {
        var node = colors.entrySet()
                .stream()
                .filter(x -> x.getValue() == Color.WHITE)
                .findFirst();
        return node.map(Map.Entry::getKey);
    }

    /**
     * Devuelve una lista con los nodos del grafo en orden topologico. Si encuentra un bucle no puede haber
     * ordenamiento topologico asi que lanza una excepcion (que es lanzada por
     * {@link #topologicalDFSVisit(Graph, int, Map, LinkedList)}).
     * <p>
     * Su coste es lineal en el numero de nodos (n) y el de aristas (a), es decir, O(n+a).
     */
    private static LinkedList<Either<Integer, List<Integer>>> topologicalOrdering(Graph<Integer> graph) {
        var result = new LinkedList<Either<Integer, List<Integer>>>();
        var colors = initColors(graph);

        while (colors.values().stream().anyMatch(color -> color == Color.WHITE)) {
            Integer node = firstUnvisitedNode(colors).get();
            topologicalDFSVisit(graph, node, colors, result);
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
    private static void topologicalDFSVisit(Graph<Integer> graph, int node, Map<Integer, Color> colors,
                                            LinkedList<Either<Integer, List<Integer>>> topologicalOrder) {
        colors.put(node, Color.GRAY);

        for (Integer adj : graph.adjacentNodes(node)) {
            switch (colors.get(adj)) {
                case WHITE -> topologicalDFSVisit(graph, adj, colors, topologicalOrder);
                case GRAY -> throw new IllegalArgumentException("There's a loop, so there's no topological order");
                case BLACK -> {
                }
            }
        }

        colors.put(node, Color.BLACK);
        topologicalOrder.addFirst(new Either.Left<>(node));
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

    /**
     * Para marcar un nodo segun si se ha terminado de visitar o no o si ni siquiera se ha visitado.
     */
    public enum Color {
        /**
         * Nodo sin visitar.
         */
        WHITE,
        /**
         * Nodo que no se ha terminado de visitar.
         */
        GRAY,
        /**
         * Nodo terminado de visitar al igual que todos sus nodos adyacentes.
         */
        BLACK
    }
}
