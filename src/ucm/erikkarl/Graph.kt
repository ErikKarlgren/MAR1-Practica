package ucm.erikkarl


/**
 * Class that represents a graph.
 *
 * Uses a map internally so each node has a corresponding list of
 * adjacent nodes.
 */
class Graph<T> : Iterable<Map.Entry<T, Set<T>>> {
    /**
     * Maps all nodes to their respective adjacent nodes. A node may have no adjacent nodes.
     */
    private var graph: MutableMap<T, MutableSet<T>> = mutableMapOf()

    fun addNode(node: T) {
        if (!graph.containsKey(node)) {
            graph[node] = mutableSetOf()
        }
    }

    fun addEdges(node: T, adjacents: List<T>) {
        addNode(node)
        adjacents.asSequence().forEach { graph.putIfAbsent(it, mutableSetOf()) }
        graph[node]!!.addAll(adjacents)
    }

    fun adjacentNodes(node: T): Set<T>{
        if (graph.containsKey(node))
            return graph[node]!!
        else
            throw NoSuchFieldException("This node does not exist in this graph.")
    }

    override fun iterator(): Iterator<Map.Entry<T, Set<T>>> {
        return graph.iterator()
    }

}