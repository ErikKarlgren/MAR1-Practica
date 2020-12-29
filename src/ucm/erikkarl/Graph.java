package ucm.erikkarl;

import java.util.*;

/**
 * Class that represents a directed graph.
 *
 * Uses a map internally so each node has a corresponding list of
 * adjacent nodes.
 */
public final class Graph<T> implements Iterable<Map.Entry<T, Set<T>>> {
    private final Map<T, Set<T>> nodeToAdjacentNodes;

    public Graph() {
        this.nodeToAdjacentNodes = new LinkedHashMap<>();
    }

    public final void addNode(T node) {
        if (!this.nodeToAdjacentNodes.containsKey(node)) {
            this.nodeToAdjacentNodes.put(node, new LinkedHashSet<>());
        }
    }

    public final void addEdges(T node, List<T> adjacents) {
        this.addNode(node);
        adjacents.forEach(x -> nodeToAdjacentNodes.putIfAbsent(x, new LinkedHashSet<>()));
        nodeToAdjacentNodes.get(node).addAll(adjacents);
    }

    public final Set<T> adjacentNodes(T node) {
        if (this.nodeToAdjacentNodes.containsKey(node)) {
            return this.nodeToAdjacentNodes.get(node);
        } else {
            throw new NoSuchElementException("This node does not exist in this graph.");
        }
    }

    public Iterator<Map.Entry<T, Set<T>>> iterator() {
        return nodeToAdjacentNodes.entrySet().iterator();
    }
}
