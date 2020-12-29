package ucm.erikkarl

import ucm.erikkarl.TestCasesManager.nanoToMilliseconds
import java.io.File
import kotlin.reflect.KFunction0
import kotlin.system.measureNanoTime


object GraphCLI {
    private var graph: Graph<Int> = Graph()

    private enum class Command {
        ADD_NODE {
            override fun getHelp(): String {
                return "Adds a node to the graph without any adjacent nodes."
            }

            override fun execute() {
                addNode()
            }
        },
        ADD_EDGES {
            override fun getHelp(): String {
                return "Creates directed edges that go from a source node to other nodes. Neither the source" +
                        " nor the destiny nodes have to exist already in the graph. The destiny nodes must have a space" +
                        " between them."
            }

            override fun execute() {
                addEdges()
            }
        },
        RESET {
            override fun getHelp(): String {
                return "Resets the graph. It will have no nodes."
            }

            override fun execute() {
                resetGraph()
            }
        },
        HELP {
            override fun getHelp(): String {
                return "Shows this message."
            }

            override fun execute() {
                showHelp()
            }
        },
        SHOW {
            override fun getHelp(): String {
                return "Shows the nodes and its adjacent nodes in the graph."
            }

            override fun execute() {
                showGraph()
            }
        },
        SOLVE {
            override fun getHelp(): String {
                return "(Ejercicio 4) Si el grafo es aciclico, lista los vertices en orden" +
                        " topologico. Si es ciclico, lista sus componentes fuertemente" +
                        " conexas como conjuntos de vertices (Kosaraju)."
            }

            override fun execute() {
                solve()
            }
        },
        TEST {
            override fun getHelp(): String {
                return "Reads a file with the data for several graphs and uses 'solve' with" +
                        " all of them"
            }

            override fun execute() {
                runTest()
            }
        },
        CREATE_TEST {
            override fun getHelp(): String {
                return "Creates a random test file. The user has to specify the number of cases," +
                        " the number of nodes per graph and the name of the file."
            }

            override fun execute() {
                createTest()
            }
        };

        abstract fun getHelp(): String
        abstract fun execute()

        fun commName(): String {
            return this.toString().replace('_', ' ').toLowerCase()
        }
    }

    private fun createTest() {
        print("Number of random tests: ")
        val numTests = readNumber()
        print("Number of nodes per graph: ")
        val numNodes = readNumber()
        val testCreator = RandomTestCasesCreator(numTests, numNodes)
        print("Name of file: ")
        val fileName = readFileName()
        testCreator.createTests(fileName)
        println("Test created in file $fileName.")
    }

    fun readCommand() {
        print(">>> ")
        val commandText: String = readLine()!!
        val command: Command? = Command.values().asSequence().find { it.commName().equals(commandText) }
        command?.execute() ?: println("Wrong command. Write ${Command.HELP.commName()} to see the commands available.")
    }

    fun showIntroduction() {
        println("This program allows you to create a graph manually or read test cases from a file.")
        println("Please, refer to the commands below to start using this program.")
        println()
        showHelp()
    }

    private fun showHelp() {
        for (comm in Command.values()) {
            println("${comm.commName()}\n\t${comm.getHelp()}\n")
        }
    }

    private fun solve() {
        val elapsedTime = measureNanoTime { println(Exercise4.solve(graph)) }
        println("Elapsed time: ${elapsedTime / 1000000.0} ms")
    }

    private fun runTest() {
        print("Name of file: ")
        val fileName = readLine()

        if (fileName != null && File(fileName).exists()) {
            val testCaseResults = TestCasesManager.runTest(fileName)!!
            println("Total number of cases: ${testCaseResults.numberOfCases}")
            println("Medium elapsed time per graph: ${nanoToMilliseconds(testCaseResults.mediumElapsedTimePerCase())} ms")
            println("Total elapsed time: ${nanoToMilliseconds(testCaseResults.totalElapsedTime)} ms")
        } else {
            val currentPath = System.getProperty("user.dir")
            System.err.println("File does not exist in $currentPath.")
        }
    }

    private fun showGraph() {
        if (graph.toList().isEmpty()) {
            println("Graph is empty")
        } else {
            for ((node, adjacents) in graph) {
                println("Node: $node \t-> $adjacents")
            }
        }
    }

    private fun resetGraph() {
        graph = Graph()
    }

    private fun readFileName(): String {
        var line: String? = readLine()
        while (line == null || line.isBlank()) {
            System.err.println("Not a valid file name.")
            print("Try again: ")
            line = readLine()
        }
        return line
    }

    private fun readNumber(): Int {
        var line: String? = readLine()
        while (line == null || line.toIntOrNull() == null) {
            System.err.println("Not a number.")
            print(" Try again: ")
            line = readLine()
        }
        return line.toInt()
    }

    private fun readNodesList(): List<Int> {
        var line: String? = readLine()
        val formatToNodesList: (String) -> List<Int?> = { x -> x.split(' ').map { it.toIntOrNull() } }

        while (line == null || formatToNodesList(line).any { it == null }) {
            System.err.println("All nodes have to be numbers.")
            print(" Try again: ")
            line = readLine()
        }
        return formatToNodesList(line) as List<Int>
    }

    private fun addEdges() {
        print("Node: ")
        val node = readNumber()
        print("Adjacent nodes: ")
        val nodes = readNodesList()
        graph.addEdges(node, nodes)
    }

    private fun addNode() {
        print("Node: ")
        val node = readNumber()
        graph.addNode(node)
    }

}

typealias Component = Either<Int, List<Int>>