package ucm.erikkarl;

import java.util.*;

/**
 * Class that represents a directed graph.
 * <p>
 * Uses a map internally so each node has a corresponding list of
 * adjacent nodes.
 */
public final class Graph<T> implements Iterable<Map.Entry<T, Set<T>>> {
    /**
     * Maps each node to a list of adjacent nodes.
     */
    private final Map<T, Set<T>> nodeToAdjacentNodes;

    public Graph() {
        this.nodeToAdjacentNodes = new LinkedHashMap<>();
    }

    public int getNumberOfNodes() {
        return nodeToAdjacentNodes.keySet().size();
    }

    /**
     * Adds a node to the graph with no adjacent nodes.
     *
     * @param node Node to be added.
     */
    public final void addNode(T node) {
        if (!this.nodeToAdjacentNodes.containsKey(node)) {
            this.nodeToAdjacentNodes.put(node, new LinkedHashSet<>());
        }
    }

    /**
     * Makes all the nodes in the list <code>adjacents</code> adjacent to <code>node</code>.
     * There's no need for any of those nodes to exist already in the graph, since they'll
     * be automatically added.
     *
     * @param node      Node to be added.
     * @param adjacents List of adjacent nodes.
     */
    public final void addEdges(T node, List<T> adjacents) {
        this.addNode(node);
        adjacents.forEach(x -> nodeToAdjacentNodes.putIfAbsent(x, new LinkedHashSet<>()));
        nodeToAdjacentNodes.get(node).addAll(adjacents);
    }

    /**
     * Returns a set of all the adjacent nodes to <code>node</code>.
     *
     * @param node Node whose adjacent nodes will be returned.
     * @return Set of nodes adjacent to <code>node</code>.
     */
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
