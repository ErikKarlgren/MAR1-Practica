package ucm.erikkarl;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;


public final class TestCasesManager {
    private TestCasesManager() {
    }

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
        } catch (IOException e) {
            System.err.println("Error: cannot write results to " + resultsFileName);
        }
    }

    private static List<Graph<Integer>> readTestsFile(File file) {
        var graphs = new LinkedList<Graph<Integer>>();
        var testCases = new LinkedList<List<String>>();
        testCases.add(new LinkedList<>());

        try (var br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().equals(RandomTestCasesCreator.START_OF_TEST))
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

    public static final class TestCaseResults {
        private final float totalElapsedTime;
        private final int numberOfCases;
        private final List<Result> results;

        private TestCaseResults(float totalElapsedTime, int numberOfCases, List<Result> results) {
            this.totalElapsedTime = totalElapsedTime;
            this.numberOfCases = numberOfCases;
            this.results = results;
        }

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

        public static final class Result {
            public final double timeElapsed;
            public final String resultAsString;

            private Result(float time, String str) {
                timeElapsed = time;
                resultAsString = str;
            }
        }

    }
}
