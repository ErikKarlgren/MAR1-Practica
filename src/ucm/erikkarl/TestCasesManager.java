package ucm.erikkarl;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * Clase encargada de leer ficheros de test, crear grafos a partir de ellos
 * y hallar el orden topologico o las componentes fuertemente conexas de cada uno
 * segun proceda. Guarda las soluciones en un fichero al igual que algunos datos
 * de su ejecucion.
 */
public final class TestCasesManager {
    /**
     * Clase que encapsula datos sobre la ejecucion de un test. Incluye el tiempo de ejecucion,
     * el numero de casos, el fichero donde se guardan los resultados y las soluciones obtenidas
     * con cada grafo.<p>
     * Los tiempos se miden en nanosegundos para poder calcular los milisegundos con decimales si
     * asi lo desea el usuario.
     */
    public static final class TestCaseResults {
        private final float totalElapsedTime;
        private final int numberOfCases;
        private final List<Result> results;
        private String fileResultsName;

        private TestCaseResults(float totalElapsedTime, int numberOfCases, List<Result> results) {
            this.totalElapsedTime = totalElapsedTime;
            this.numberOfCases = numberOfCases;
            this.results = results;
        }

        /**
         * @return Tiempo medio de ejecucion de cada test en nanosegundos.
         */
        public final float mediumElapsedTimePerCase() {
            return this.numberOfCases > 0 ? this.totalElapsedTime / (long) this.numberOfCases : 0f;
        }

        public final float getTotalElapsedTime() {
            return this.totalElapsedTime;
        }

        public final int getNumberOfCases() {
            return this.numberOfCases;
        }

        public final List<Result> getResults() {
            return this.results;
        }

        public String getFileResultsName() {
            return fileResultsName;
        }

        public void setFileResultsName(String fileResultsName) {
            this.fileResultsName = fileResultsName;
        }

        /**
         * Contiene el tiempo necesario para calcular la solucion
         * del algoritmo y la solucion misma representada como una
         * cadena de texto.
         */
        public static final class Result {
            public final double timeElapsed;
            public final String resultAsString;

            private Result(float time, String str) {
                timeElapsed = time;
                resultAsString = str;
            }
        }
    }
    public static final String START_OF_TEST = "graph:";

    private TestCasesManager() {
    }

    /**
     * Lee el test del fichero con nombre <code>fileName</code>, ejecuta el test y
     * devuelve los resultados.
     * Si el fichero no existe devuelve un <code>null</code>.
     */
    public static TestCaseResults runTest(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            var results = executeTests(readTestsFile(file));
            writeTestResults(results, file);
            return results;
        } else {
            return null;
        }
    }

    /**
     * Ejecuta el algoritmo sobre los grafos de la lista <code>graphs</code>.
     */
    private static TestCaseResults executeTests(List<Graph<Integer>> graphs) {
        long totalElapsedTime = 0;
        List<TestCaseResults.Result> results = new LinkedList<>();

        for (Graph<Integer> graph : graphs) {
            long startTime = System.nanoTime();
            var solution = Exercise4.solve(graph);
            long elapsedTime = System.nanoTime() - startTime;
            double elapsedTimeInMilliseconds = elapsedTime / 1000000.0;
            totalElapsedTime += elapsedTime;
            results.add(new TestCaseResults.Result((float) elapsedTimeInMilliseconds, solution.toString()));
        }

        return new TestCaseResults((float) (totalElapsedTime / 1000000.0), graphs.size(), results);
    }

    /**
     * Escribe los resultados del test en un fichero nuevo cuyo nombre se forma a partir del
     * de <code>originalFile</code>.
     */
    private static void writeTestResults(TestCaseResults testResults, File originalFile) {
        UnaryOperator<String> withoutExtension = (String fileName) -> {
            int pos = fileName.lastIndexOf(".");
            if (pos > 0 && pos < (fileName.length() - 1)) {
                fileName = fileName.substring(0, pos);
            }
            return fileName;
        };
        String resultsFileName = withoutExtension.apply(originalFile.getName()) + "-result.txt";

        try (var resultsFile = new FileWriter(resultsFileName)) {
            var resultsStrBuilder = new StringBuilder();
            for (TestCaseResults.Result result : testResults.results) {
                resultsStrBuilder.append("Time: ").append(result.timeElapsed).append(" ms\n");
                resultsStrBuilder.append("Result: ").append(result.resultAsString).append("\n\n");
            }
            var prelude = "# Total elapsed time: " + testResults.getTotalElapsedTime() + " ms\n" +
                    "# Total number of cases: " + testResults.getNumberOfCases() + '\n' +
                    "# Medium elapsed time per case: " + testResults.mediumElapsedTimePerCase() + " ms\n\n";

            resultsFile.write(prelude + resultsStrBuilder.toString());
            testResults.setFileResultsName(resultsFileName);
        } catch (IOException e) {
            System.err.println("Error: cannot write results to " + resultsFileName);
        }
    }

    /**
     * Lee un fichero de test y crea una lista de grafos.
     */
    private static List<Graph<Integer>> readTestsFile(File file) {
        var graphs = new LinkedList<Graph<Integer>>();
        var testCases = new LinkedList<List<String>>();
        testCases.add(new LinkedList<>());

        try (var br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().equals(START_OF_TEST))
                    testCases.getLast().add(line.trim());
                else
                    testCases.add(new LinkedList<>());
            }
        } catch (IOException e) {
            System.err.println("Error while reading tests file");
        }
        testCases.removeFirst();  // the first element is an empty string
        for (List<String> testLine : testCases) {
            graphs.add(createGraph(testLine));
        }
        return graphs;
    }

    /**
     * Crea un grafo a partir de una lista de lineas de texto que lo
     * representa.
     */
    private static Graph<Integer> createGraph(List<String> testCaseLines) {
        var graph = new Graph<Integer>();
        var iterator = testCaseLines.iterator();

        while (iterator.hasNext()) {
            var node = Integer.valueOf(iterator.next());
            var adjacentsString = iterator.next();
            var adjacents = new LinkedList<Integer>();

            if (!adjacentsString.equals(""))
                adjacents = List.of(adjacentsString.split(" "))
                        .stream()
                        .map(Integer::valueOf)
                        .collect(Collectors.toCollection(LinkedList::new));

            graph.addEdges(node, adjacents);
        }
        return graph;
    }
}
