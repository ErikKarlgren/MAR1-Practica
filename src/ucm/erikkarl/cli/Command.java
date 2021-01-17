package ucm.erikkarl.cli;

/**
 * Clase para representar un comando. Cada comando tiene un mensaje propio de ayuda que se puede
 * consultar con el comando "help".
 */
enum Command {
    HELP {
        public String getHelp() {
            return "Shows this message.";
        }

        public void execute() {
            GraphCLI.showHelp();
        }
    },
    RUN_TEST {
        public String getHelp() {
            return "Reads a file with the data for several graphs and runs the algorithm with" +
                    " all of them.";
        }

        public void execute() {
            GraphCLI.runTest();
        }
    },
    CREATE_TEST {
        public String getHelp() {
            return "Creates a random test file. The user has to specify the number of cases, " +
                    "the number of nodes per graph and the name of the file. This test file isn't " +
                    "made to be run with gnuplot since all the graphs have the same amount of nodes, but " +
                    "to test if the algorithm is working properly for small graphs.";
        }

        public void execute() {
            GraphCLI.createRandomTest();
        }
    },
    BIG_TEST {
        @Override
        public String getHelp() {
            return "Creates a random test file, but each graph has a different size. Starting with a " +
                    "graph with 1 node, each graph has 100 more nodes than the last one until it almost " +
                    "reaches 10000 nodes. This test file is made to be later run with gnuplot.\n";
        }

        @Override
        public void execute() {
            GraphCLI.createBigTest();
        }
    };

    public abstract String getHelp();

    public abstract void execute();

    @Override
    public final String toString() {
        return this.name().replace('_', ' ').toLowerCase();
    }
}
