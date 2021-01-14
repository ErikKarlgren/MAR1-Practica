package ucm.erikkarl.tests;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

final class TestResultsWriter {
    private TestResultsWriter() {
    }

    /**
     * Escribe los resultados del test en un fichero nuevo cuyo nombre se forma a partir del
     * de <code>originalFile</code>.
     */
    static void writeSolutionsToFile(TestResults testResults, File originalFile) {
        String solutionsFileName = ridOfExtension(originalFile.getName()) + "-result.txt";

        try (var solutionsFile = new FileWriter(solutionsFileName)) {
            var resultsStrBuilder = new StringBuilder();
            for (TestResults.Result result : testResults.getResults()) {
                resultsStrBuilder.append("Time: ").append(result.getMeanTimeElapsed()).append(" ms\n");
                resultsStrBuilder.append("Result: ").append(result.getResultAsString()).append("\n\n");
            }
            solutionsFile.write(prelude(testResults) + resultsStrBuilder.toString());
            testResults.setSolutionsFileName(solutionsFileName);

        } catch (IOException e) {
            System.err.println("Error: cannot write results to " + solutionsFileName);
        }
    }

    /**
     * Escribe en una columna el numero de nodos de cada grafo y en otra el tiempo de ejecucion
     * del algoritmo. El fichero deberia poder ser leido por gnuplot.
     */
    static void writeExecutionTimeToFile(TestResults results, File originalFile) {
        String executionTimeFileName = ridOfExtension(originalFile.getName()) + "-times.txt";

        try (var executionTimeFile = new FileWriter(executionTimeFileName)) {
            executionTimeFile.write("# Nodes\t Time\n");

            for (TestResults.Result result : results.getResults()) {
                var line = String.format("%d\t %f\n",
                        result.getGraphNodesNumber(),
                        result.getMeanTimeElapsed());
                executionTimeFile.write(line);
            }
            results.setExecutionTimeFileName(executionTimeFileName);
        } catch (IOException e) {
            System.err.println("Error: cannot write results to " + executionTimeFileName);
        }
    }

    private static String ridOfExtension(String fileName) {
        int pos = fileName.lastIndexOf(".");
        if (pos > 0 && pos < (fileName.length() - 1)) {
            fileName = fileName.substring(0, pos);
        }
        return fileName;
    }

    private static String prelude(TestResults results) {
        String preludeString = """
                # Total elapsed time: %f ms
                # Total number of cases: %d
                # Medium elapsed time per case: %f ms
                """;
        return String.format(preludeString,
                results.getTotalMediumElapsedTime(),
                results.getNumberOfCases(),
                results.mediumElapsedTimePerCase());
    }
}
