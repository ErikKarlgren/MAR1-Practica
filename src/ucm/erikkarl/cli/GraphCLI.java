package ucm.erikkarl.cli;

import ucm.erikkarl.tests.BigTestCreator;
import ucm.erikkarl.tests.RandomTestCreator;
import ucm.erikkarl.tests.TestManager;

/**
 * Interfaz por consola que permite modificar un grafo y consultar el orden topologico de sus nodos
 * o sus componentes conexas segun proceda. Tambien permite hacer esto ultimo con grafos especificados
 * por ficheros de texto que siguen un formato concreto o incluso crear un fichero del estilo.
 */
public final class GraphCLI {


    private GraphCLI() {
    }

    /**
     * Crea un fichero de test con grafos aleatorios.
     * Pide al usuario especificar el numero de grafos, el numero de nodos
     * de cada uno y el nombre de un fichero donde guardar los datos del test.
     */
    static void createRandomTest() {
        System.out.print("Number of random tests: ");
        int numTests = CLIReader.readNumber();

        System.out.print("Number of nodes per graph: ");
        int numNodes = CLIReader.readNumber();
        var testCreator = new RandomTestCreator(numTests, numNodes);

        System.out.print("Name of file: ");
        var fileName = CLIReader.readFileName();

        testCreator.createTestFile(fileName);
        System.out.println("Test created in file " + fileName + '.');
    }

    public static void readCommand() {
        System.out.print(">>> ");
        var optionalCommand = CLIReader.readCommand();
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
        System.out.println("This program allows you to read and create random test files to calculate a graph's " +
                "topological order (if there are no loops) or their strongly connected components (if there's at least " +
                "one loop).");
        System.out.println("Write \"" + Command.HELP + "\" for help.\n");
    }

    /**
     * Muestra los comandos disponibles y su uso.
     */
    static void showHelp() {
        for (Command c : Command.values()) {
            System.out.println("> " + c.toString() + "\n" + c.getHelp() + "\n");
        }
    }

    /**
     * Pide al usuario el nombre de un fichero de test y delega la lectura del fichero
     * y la ejecucion del test. Despues, imprime algunos datos sobre los resultados por
     * consola. Estos datos mas la solucion
     */
    static void runTest() {
        System.out.print("Name of file: ");
        var fileName = CLIReader.readFileName();
        System.out.println("Executing test...");

        long startTime = System.currentTimeMillis();
        var resultsOpt = TestManager.runTest(fileName);
        long finalTime = System.currentTimeMillis();

        if (resultsOpt.isPresent()) {
            var results = resultsOpt.get();
            System.out.println("Total number of cases: " + results.getNumberOfCases());
            System.out.println("Medium elapsed time per graph: " + results.mediumElapsedTimePerCase() + " ms");
            System.out.println("Total medium elapsed time: " + results.getTotalMediumElapsedTime() + " ms");
            System.out.println("Actual elapsed time: " + (finalTime - startTime) + " ms");
            System.out.println("Solutions saved to " + results.getSolutionsFileName());
            System.out.println("Execution times saved to " + results.getExecutionTimeFileName());
        } else {
            var currentPath = System.getProperty("user.dir");
            System.err.println("File does not exist in " + currentPath);
        }
    }


    /**
     * Pide al usuario el nombre de un fichero de test y crea un "big test": un test
     * donde cada grafo tiene un numero distinto de nodos y en orden creciente hasta
     * llegar a los miles de nodos por grafo.
     */
    static void createBigTest() {
        long startTime;
        long finalTime;
        String fileName;

        System.out.print("Name of file: ");
        fileName = CLIReader.readFileName();
        System.out.printf("Creating big test in %s...%n", fileName);

        startTime = System.currentTimeMillis();
        BigTestCreator.create(fileName);
        finalTime = System.currentTimeMillis();

        System.out.printf("File finished writing! Time required: %d ms%n", (finalTime - startTime));
    }

    public static void printMessage(String msg) {
        System.out.println(msg);
    }
}
