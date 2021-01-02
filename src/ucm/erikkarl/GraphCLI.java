// GraphCLI.java
package ucm.erikkarl;

import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


public final class GraphCLI {
    private static Graph<Integer> graph = new Graph<>();
    private static final Scanner stdin = new Scanner(System.in);

    private static void createTest() {
        System.out.print("Number of random tests: ");
        int numTests = readNumber();

        System.out.print("Number of nodes per graph: ");
        int numNodes = readNumber();
        var testCreator = new RandomTestCasesCreator(numTests, numNodes);

        System.out.print("Name of file: ");
        var fileName = readFileName();

        testCreator.createTests(fileName);
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

    public static void showIntroduction() {
        System.out.println("This program allows you to create a graph manually, read test cases from a file," +
                " and even create random test files.");
        System.out.println("Write \"" + Command.HELP + "\" for help.\n");
    }

    private static void showHelp() {
        for (Command c : Command.values()) {
            System.out.println(c.toString() + "\n\t" + c.getHelp() + "\n");
        }
    }

    private static void solve() {
        var initTime = System.nanoTime();
        System.out.println(Exercise4.solve(graph));
        var finalTime = System.nanoTime();
        System.out.println("Elapsed time: " + ((finalTime - initTime) / 1000000.0) + " ms");
    }

    private static void runTest() {
        System.out.print("Name of file: ");
        var fileName = stdin.nextLine();

        if (fileName != null && new File(fileName).exists()) {
            var results = TestCasesManager.runTest(fileName);
            System.out.println("Total number of cases: " + results.getNumberOfCases());
            System.out.println("Medium elapsed time per graph: " + results.mediumElapsedTimePerCase() + " ms");
            System.out.println("Total elapsed time: " + results.getTotalElapsedTime() + " ms");
        } else {
            var currentPath = System.getProperty("user.dir");
            System.err.println("File does not exist in " + currentPath);
        }
    }

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

    private static void resetGraph() {
        graph = new Graph<>();
    }

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

                /*if (!line.isBlank()){
                    nodes = formatToNodesList.apply(line);
                    validInput = true;
                }
                else {
                    System.err.println("Invalid input: write a list of numbers.");
                }*/
            } catch (NumberFormatException e) {
                System.err.println("Invalid input: write a list of numbers.");
            }
        }
        return nodes;
    }

    private static void addEdges() {
        System.out.print("Node: ");
        var node = readNumber();
        System.out.print("Adjacent nodes: ");
        var nodes = readNodesList();
        graph.addEdges(node, nodes);
    }

    private static void addNode() {
        System.out.print("Node: ");
        var node = readNumber();
        graph.addNode(node);
    }

    private GraphCLI() {
    }


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
