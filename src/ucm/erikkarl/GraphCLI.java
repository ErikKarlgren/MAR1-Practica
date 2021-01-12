// GraphCLI.java
package ucm.erikkarl;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Interfaz por consola que permite modificar un grafo y consultar el orden topologico de sus nodos
 * o sus componentes conexas segun proceda. Tambien permite hacer esto ultimo con grafos especificados
 * por ficheros de texto que siguen un formato concreto o incluso crear un fichero del estilo.
 */
public final class GraphCLI {
    /**
     * Scanner que lee de la entrada estandar.
     */
    private static final Scanner stdin = new Scanner(System.in);
    /**
     * Grafo que se mantiene en memoria el cual puede modificar el usuario.
     */
    private static Graph<Integer> graph = new Graph<>();

    private GraphCLI() {
    }

    /**
     * Crea un fichero de test con grafos aleatorios.
     * Pide al usuario especificar el numero de grafos, el numero de nodos
     * de cada uno y el nombre de un fichero donde guardar los datos del test.
     */
    private static void createTest() {
        System.out.print("Number of random tests: ");
        int numTests = readNumber();

        System.out.print("Number of nodes per graph: ");
        int numNodes = readNumber();
        var testCreator = new RandomTestCasesCreator(numTests, numNodes);

        System.out.print("Name of file: ");
        var fileName = readFileName();

        testCreator.createTestFile(fileName);
        System.out.println("Test created in file " + fileName + '.');
    }

    public static void readCommand() {
        System.out.print(">>> ");
        String commandText = stdin.nextLine();
        var optionalCommand = Arrays.stream(Command.values()).filter(x -> x.toString().equals(commandText)).findFirst();
        if (optionalCommand.isPresent())
            optionalCommand.get().execute();
        else {
            System.err.println("Error: this command doesn't exist.");
            System.err.println("Write " + Command.HELP.toString() + " to see the commands available.");
        }
    }

    /**
     * Muestra una introduccion al programa.
     */
    public static void showIntroduction() {
        System.out.println("This program allows you to create a graph manually, read test cases from a file," +
                " and even create random test files.");
        System.out.println("Write \"" + Command.HELP + "\" for help.\n");
    }

    /**
     * Muestra los comandos disponibles y su uso.
     */
    private static void showHelp() {
        for (Command c : Command.values()) {
            System.out.println("> " + c.toString() + "\n" + c.getHelp() + "\n");
        }
    }

    /**
     * Aplica el algoritmo especificado por el enunciado del ejercicio 4 de la practica
     * sobre el grafo {@link #graph}, es decir, haya el orden topologico de sus nodos
     * si no tiene bucles, y en caso contrario sus componentes fuertemente conexas.
     */
    private static void solve() {
        var initTime = System.nanoTime();
        System.out.println(Exercise4.solve(graph));
        var finalTime = System.nanoTime();
        System.out.println("Elapsed time: " + ((finalTime - initTime) / 1000000.0) + " ms");
    }

    /**
     * Pide al usuario el nombre de un fichero de test y delega la lectura del fichero
     * y la ejecucion del test. Despues, imprime algunos datos sobre los resultados por
     * consola. Estos datos mas la solucion
     */
    private static void runTest() {
        System.out.print("Name of file: ");
        var fileName = readFileName();
        var results = TestCasesManager.runTest(fileName);
        if (results != null) {
            System.out.println("Total number of cases: " + results.getNumberOfCases());
            System.out.println("Medium elapsed time per graph: " + results.mediumElapsedTimePerCase() + " ms");
            System.out.println("Total elapsed time: " + results.getTotalElapsedTime() + " ms");
            System.out.println("Results saved to " + results.getFileResultsName());
        } else {
            var currentPath = System.getProperty("user.dir");
            System.err.println("File does not exist in " + currentPath);
        }
    }

    /**
     * Muestra una representacion del grafo {@link #graph} por consola usando el
     * siguiente formato para cada nodo del grafo.
     * <p>
     * <code>Node: nodo -> [lista de nodos adyacentes]</code>
     * <p>
     */
    private static void showGraph() {
        if (!graph.iterator().hasNext()) {
            // If there are no nodes in the graph, the iterator has no next element
            System.out.println("Graph is empty");
        } else {
            for (Map.Entry<Integer, Set<Integer>> entry : graph) {
                var node = entry.getKey();
                var adjacents = entry.getValue();
                System.out.println("Node: " + node + "\t -> " + adjacents);
            }
        }
    }

    /**
     * El grafo {@link #graph} pasa a estar vacio, sin nodos.
     */
    private static void resetGraph() {
        graph = new Graph<>();
    }

    /**
     * Lee el nombre de un fichero por consola. No tiene por que existir pues esta funcion
     * puede ser usada para leer el nombre de un fichero a crear.
     */
    private static String readFileName() {
        String line = "";
        boolean validInput = false;

        while (!validInput) {
            line = stdin.nextLine();
            if (!line.isBlank() && !line.contains(" "))
                validInput = true;
            else {
                System.err.println("Invalid input: file name shouldn't be blank nor have whitespaces.");
            }
        }
        return line.trim();
    }

    /**
     * Lee un solo entero por consola.
     */
    private static int readNumber() {
        String line;
        int number = 0;
        boolean validInput = false;

        while (!validInput) {
            line = stdin.nextLine();
            try {
                number = Integer.parseInt(line);
                validInput = true;

            } catch (NumberFormatException e) {
                System.err.println("Invalid input: write a number.");
            }
        }

        return number;
    }

    /**
     * Lee una lista de nodos por consola. Todos deben ser enteros separados por espacios.
     */
    private static List<Integer> readNodesList() {
        Function<String, List<Integer>> formatToNodesList = (String x) ->
                Arrays.stream(x.split(" ")).map(Integer::valueOf).collect(Collectors.toList());
        String line;
        List<Integer> nodes = new LinkedList<>();
        boolean validInput = false;

        while (!validInput) {
            line = stdin.nextLine();
            try {
                nodes = formatToNodesList.apply(line);
                validInput = true;
            } catch (NumberFormatException e) {
                System.err.println("Invalid input: write a list of numbers.");
            }
        }
        return nodes;
    }

    /**
     * Pide al usuario un nodo y una lista de nodos adyacentes y
     * modifica el grafo {@link #graph}.
     */
    private static void addEdges() {
        System.out.print("Node: ");
        var node = readNumber();
        System.out.print("Adjacent nodes: ");
        var nodes = readNodesList();
        graph.addEdges(node, nodes);
    }

    /**
     * Pide al usuario un nodo que añadir al grafo {@link #graph}.
     */
    private static void addNode() {
        System.out.print("Node: ");
        var node = readNumber();
        graph.addNode(node);
    }

    /**
     * Clase para representar un comando. Cada comando tiene un mensaje propio de ayuda que se puede
     * consultar con el comando "help".
     */
    private enum Command {
        ADD_NODE {
            public String getHelp() {
                return "Adds a node to the graph without any adjacent nodes.";
            }

            public void execute() {
                addNode();
            }
        },
        ADD_EDGES {
            public String getHelp() {
                return "Creates directed edges that go from a source node to other nodes. Neither the source" +
                        " nor the destiny nodes have to exist already in the graph. The destiny nodes must have a space" +
                        " between them.";
            }

            public void execute() {
                addEdges();
            }
        },
        RESET {
            public String getHelp() {
                return "Resets the graph. It will have no nodes.";
            }

            public void execute() {
                resetGraph();
            }
        },
        HELP {
            public String getHelp() {
                return "Shows this message.";
            }

            public void execute() {
                showHelp();
            }
        },
        SHOW {
            public String getHelp() {
                return "Shows the nodes and its adjacent nodes in the graph.";
            }

            public void execute() {
                showGraph();
            }
        },
        SOLVE {
            public String getHelp() {
                return "(Ejercicio 4) Si el grafo es aciclico, lista los vertices en orden" +
                        " topologico. Si es ciclico, lista sus componentes fuertemente" +
                        " conexas como conjuntos de vertices (Kosaraju).";
            }

            public void execute() {
                solve();
            }
        },
        TEST {
            public String getHelp() {
                return "Reads a file with the data for several graphs and uses 'solve' with" +
                        " all of them.";
            }

            public void execute() {
                runTest();
            }
        },
        CREATE_TEST {
            public String getHelp() {
                return "Creates a random test file. The user has to specify the number of cases," +
                        " the number of nodes per graph and the name of the file.";
            }

            public void execute() {
                createTest();
            }
        };

        public abstract String getHelp();
        public abstract void execute();

        @Override
        public final String toString() {
            return this.name().replace('_', ' ').toLowerCase();
        }
    }
}
