package ucm.erikkarl.exercise4;


import ucm.erikkarl.Either;
import ucm.erikkarl.graph.Graph;

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
            return TopologicalOrdering.solve(graph);
        } catch (IllegalArgumentException var4) {
            return KosarajusAlgorithm.solve(graph);
        }
    }

    /**
     * Crea un mapa de nodos a colores. Se usa para indicar si un nodo esta sin visitar,
     * se esta visitando o se ha visitado este y todos sus adyacentes.
     */
    static Map<Integer, Color> initColors(Graph<Integer> graph) {
        Map<Integer, Color> colors = new LinkedHashMap<>();

        for (Map.Entry<Integer, Set<Integer>> entry : graph)
            colors.put(entry.getKey(), Color.WHITE);

        return colors;
    }

    /**
     * Devuelve el primer nodo que queda sin visitar siguiendo el orden definido por
     * el mapa <code>colors</code>.
     */
    static Optional<Integer> firstUnvisitedNode(Map<Integer, Color> colors) {
        var node = colors.entrySet()
                .stream()
                .filter(x -> x.getValue() == Color.WHITE)
                .findFirst();
        return node.map(Map.Entry::getKey);
    }
}
