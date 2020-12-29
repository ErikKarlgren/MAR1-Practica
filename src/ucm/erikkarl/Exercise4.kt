package ucm.erikkarl

import java.util.*


/**
 *  Implementar o Java o en C++ un algoritmo, que dado un grafo dirigido,
 *  detecte si tiene o no ciclos. En caso de ser aciclico, ha de listar
 *  sus vertices en orden topologico. Si hay mas de uno posible, los
 *  puede listar en cualquiera de ellos. En caso de ser cıclico, ha de
 *  listar sus componentes fuertemente conexas como conjuntos de vertices.
 */
object Exercise4 {

    enum class Color {
        WHITE,  // unvisited node
        GRAY,   // visited, but not finished with this node
        BLACK   // visited node
    }

    fun solve(graph: Graph<Int>): List<Component> {
        return try {
            // We consider the graph has no loops. If it actually has, an exception will be raised.
            topologicalOrdering(graph)
        } catch (e: IllegalArgumentException) {
            // The graph has at least one loop. Kosaraju's Algorithm.
            kosarajusAlgorithm(graph)
        }
    }

    private fun initColors(graph: Graph<Int>): MutableMap<Int, Color> {
        val colors = mutableMapOf<Int, Color>()
        for ((node, _) in graph) {
            colors[node] = Color.WHITE
        }
        return colors
    }

    private fun topologicalOrdering(graph: Graph<Int>): LinkedList<Component> {
        val result = LinkedList<Component>()
        val colors = initColors(graph)

        while (colors.values.any { it == Color.WHITE }) {
            val firstUnvisitedNode: Int = colors.asSequence().find { it.value == Color.WHITE }!!.key
            topologicalDFSVisit(graph, firstUnvisitedNode, colors, result)
        }
        return result
    }

    private fun topologicalDFSVisit(graph: Graph<Int>, node: Int, colors: MutableMap<Int, Color>, topologicalOrder: LinkedList<Component>) {
        colors[node] = Color.GRAY

        for (adj in graph.adjacentNodes(node)) {
            when (colors[adj]) {
                Color.WHITE -> topologicalDFSVisit(graph, adj, colors, topologicalOrder)
                Color.GRAY -> throw IllegalArgumentException("There's a loop, so there's no topological order")
                else -> { /* if BLACK, do nothing */
                }
            }
        }
        colors[node] = Color.BLACK
        topologicalOrder.addFirst(Either.Left(node))
    }


    private fun kosarajusAlgorithm(graph: Graph<Int>): LinkedList<Component> {
        val result = LinkedList<Component>()
        var colors = initColors(graph)
        val nodesOrderedByFinishTime = LinkedList<Int>()

        // 1er recorrido del grafo
        while (colors.values.any { it == Color.WHITE }) {
            val firstUnvisitedNode: Int = colors.asSequence().find { it.value == Color.WHITE }!!.key
            kosarajusDFSVisit(graph, firstUnvisitedNode, colors, nodesOrderedByFinishTime)
        }

        // 2do recorrido del grafo
        val transpose = transposeGraph(graph)
        colors = initColors(graph)

        while (nodesOrderedByFinishTime.isNotEmpty()) {
            val node = nodesOrderedByFinishTime.pop()
            if (colors[node] == Color.WHITE) {
                val component = LinkedList<Int>()
                kosarajusDFSVisit(transpose, node, colors, component)
                if (component.size > 1)
                    result.add(Either.Right(component))
                else
                    result.add(Either.Left(component.first))
            }
        }
        return result
    }

    private fun kosarajusDFSVisit(graph: Graph<Int>, node: Int, colors: MutableMap<Int, Color>, nodesList: LinkedList<Int>) {
        colors[node] = Color.GRAY

        for (adj in graph.adjacentNodes(node)) {
            if (colors[adj] == Color.WHITE) {
                kosarajusDFSVisit(graph, adj, colors, nodesList)
            }
        }
        colors[node] = Color.BLACK
        nodesList.push(node)
    }

    private fun transposeGraph(graph: Graph<Int>): Graph<Int> {
        val transpose = Graph<Int>()
        for ((node, adjacents) in graph) {
            for (adj in adjacents) {
                transpose.addEdges(adj, listOf(node))
            }
        }
        return transpose
    }
}