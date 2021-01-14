package ucm.erikkarl.cli;

/**
 * Clase para representar un comando. Cada comando tiene un mensaje propio de ayuda que se puede
 * consultar con el comando "help".
 */
enum Command {
    ADD_NODE {
        public String getHelp() {
            return "Adds a node to the graph without any adjacent nodes.";
        }

        public void execute() {
            GraphCLI.addNode();
        }
    },
    ADD_EDGES {
        public String getHelp() {
            return "Creates directed edges that go from a source node to other nodes. Neither the source" +
                    " nor the destiny nodes have to exist already in the graph. The destiny nodes must have a space" +
                    " between them.";
        }

        public void execute() {
            GraphCLI.addEdges();
        }
    },
    RESET {
        public String getHelp() {
            return "Resets the graph. It will have no nodes.";
        }

        public void execute() {
            GraphCLI.resetGraph();
        }
    },
    HELP {
        public String getHelp() {
            return "Shows this message.";
        }

        public void execute() {
            GraphCLI.showHelp();
        }
    },
    SHOW {
        public String getHelp() {
            return "Shows the nodes and its adjacent nodes in the graph.";
        }

        public void execute() {
            GraphCLI.showGraph();
        }
    },
    SOLVE {
        public String getHelp() {
            return "(Ejercicio 4) Si el grafo es aciclico, lista los vertices en orden" +
                    " topologico. Si es ciclico, lista sus componentes fuertemente" +
                    " conexas como conjuntos de vertices (Kosaraju).";
        }

        public void execute() {
            GraphCLI.solve();
        }
    },
    TEST {
        public String getHelp() {
            return "Reads a file with the data for several graphs and uses 'solve' with" +
                    " all of them.";
        }

        public void execute() {
            GraphCLI.runTest();
        }
    },
    CREATE_TEST {
        public String getHelp() {
            return "Creates a random test file. The user has to specify the number of cases," +
                    " the number of nodes per graph and the name of the file.";
        }

        public void execute() {
            GraphCLI.createRandomTest();
        }
    };

    public abstract String getHelp();

    public abstract void execute();

    @Override
    public final String toString() {
        return this.name().replace('_', ' ').toLowerCase();
    }
}
