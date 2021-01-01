package ucm.erikkarl

import java.io.File
import java.util.*
import kotlin.system.measureNanoTime

typealias Component = Either<Int, List<Int>>

/**
 * Reads test files, executes them and writes the results in another file
 */
object TestCasesManager {

    fun nanoToMilliseconds(n: Long): Double {
        return n / 1000000.0
    }

    data class TestCaseResults(val totalElapsedTime: Long,
                               val numberOfCases: Int,
                               val results: List<Pair<String, Long>>) {

        fun mediumElapsedTimePerCase(): Long {
            return if (numberOfCases > 0) totalElapsedTime / numberOfCases else 0
        }
    }

    fun runTest(fileName: String): TestCaseResults? {
        val file = File(fileName)
        return if (file.exists()) {
            val results = executeTests(readTestsFile(file))
            writeTestResults(results, file)
            results
        } else
            null
    }

    private fun executeTests(graphs: List<Graph<Int>>): TestCaseResults {
        var totalElapsedTime: Long = 0
        val results = mutableListOf<Pair<String, Long>>()

        for (graph in graphs) {
            var solution: List<Component> = listOf()
            val testElapsedTime = measureNanoTime { solution = Exercise4.solve(graph) }
            totalElapsedTime += testElapsedTime
            results.add(Pair(solution.toString(), testElapsedTime))
        }

        return TestCaseResults(totalElapsedTime, graphs.size, results)
    }

    private fun writeTestResults(testResults: TestCaseResults, originalFile: File) {
        val resultsFileName = originalFile.nameWithoutExtension + "-result.txt"
        val resultsFile = File(resultsFileName)

        val prelude = StringBuilder()
        prelude.append("# Total elapsed time: ${nanoToMilliseconds(testResults.totalElapsedTime)} ms\n")
        prelude.append("# Total number of cases: ${testResults.numberOfCases}\n")
        prelude.append("# Medium elapsed time per case: ${nanoToMilliseconds(testResults.mediumElapsedTimePerCase())} ms\n\n")

        val resultsText = StringBuilder()
        for ((result, time) in testResults.results) {
            resultsText.append("Time: ${nanoToMilliseconds(time)} ms\n")
            resultsText.append("Result: $result\n\n")
        }
        resultsFile.writeText(prelude.toString() + resultsText.toString())
    }

    /**
    Formato del fichero:
    nodo1.1
    adyacentes1.1
    nodo1.2
    adyacentes1.2
    ...
    nodo1.x
    adyacentes1.x

    nodo2.1
    adyacentes2.1
    nodo2.2
    adyacentes2.2
    ...
    nodo2.y
    adyacentes2.y

    ...
     */
    private fun readTestsFile(file: File): List<Graph<Int>> {
        val fileLines = file.readLines()
        val testCases = LinkedList<MutableList<String>>()
        val graphs = mutableListOf<Graph<Int>>()

        // Groups lines by each test/graph
        testCases.add(mutableListOf())
        for (line in fileLines) {
            if (!line.trim().equals(RandomTestCasesCreator.START_OF_TEST)) {
                testCases.last.add(line.trim())
            } else {
                testCases.add(mutableListOf())
            }
        }

        // the first element is an empty string
        testCases.removeFirst()
        // Creates the graphs
        for (test in testCases) {
            graphs.add(createGraph(test))
        }
        return graphs
    }

    private fun createGraph(testCaseLines: List<String>): Graph<Int> {
        val graph = Graph<Int>()
        val iterator = testCaseLines.iterator()

        while (iterator.hasNext()) {
            val node = iterator.next().toInt()
            val adjacentsString = iterator.next()
            var adjacents = listOf<Int>()

            if(!adjacentsString.equals(""))
                adjacents = adjacentsString.split(' ').map { it.toInt() }

            graph.addEdges(node, adjacents)
        }
        return graph
    }
}