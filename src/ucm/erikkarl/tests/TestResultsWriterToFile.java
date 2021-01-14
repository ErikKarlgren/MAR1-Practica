package ucm.erikkarl.tests;

import ucm.erikkarl.Graph;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

final class TestResultsWriterToFile {
    private TestResultsWriterToFile() {
    }

    /**
     * Escribe los resultados del test en un fichero nuevo cuyo nombre se forma a partir del
     * de <code>originalFile</code>.
     */
    static void writeSolutions(TestResults testResults, File originalFile) {
        String solutionsFileName = ridOfExtension(originalFile.getName()) + "-result.txt";

        try (var solutionsFile = new FileWriter(solutionsFileName)) {
            var resultsStrBuilder = new StringBuilder();
            for (TestResults.Result result : testResults.getResults()) {
                resultsStrBuilder.append("Time: ").append(result.timeElapsed).append(" ms\n");
                resultsStrBuilder.append("Result: ").append(result.resultAsString).append("\n\n");
            }
            solutionsFile.write(prelude(testResults) + resultsStrBuilder.toString());
            testResults.setFileResultsName(solutionsFileName);
        } catch (IOException e) {
            System.err.println("Error: cannot write results to " + solutionsFileName);
        }
    }

    static void writeExecutionTimeResults(TestResults results, File originalFile){
        String executionTimeFileName = ridOfExtension(originalFile.getName()) + "-times.txt";

        try(var executionTimeFile = new FileWriter(executionTimeFileName)){
            var strBuilder = new StringBuilder();
            strBuilder.append("# Nodes\t Time\n");
            for(TestResults.Result result : results.getResults()){
                //strBuilder.append(result.get)
            }
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
                results.getTotalElapsedTime(),
                results.getNumberOfCases(),
                results.mediumElapsedTimePerCase());
    }
}
