package ucm.erikkarl.tests;

import ucm.erikkarl.Exercise4;
import ucm.erikkarl.Graph;

import java.util.LinkedList;
import java.util.List;

final class TestRunner {

    private TestRunner() {
    }

    /**
     * Ejecuta el algoritmo sobre los grafos de la lista <code>graphs</code>.
     */
    static TestResults run(List<Graph<Integer>> graphs) {
        long totalElapsedTime = 0;
        List<TestResults.Result> results = new LinkedList<>();

        for (Graph<Integer> graph : graphs) {
            long startTime = System.nanoTime();
            var solution = Exercise4.solve(graph);
            long elapsedTime = System.nanoTime() - startTime;
            double elapsedTimeInMilliseconds = elapsedTime / 1000000.0;
            totalElapsedTime += elapsedTime;
            results.add(new TestResults.Result((float) elapsedTimeInMilliseconds, solution.toString()));
        }

        return new TestResults((float) (totalElapsedTime / 1000000.0), graphs.size(), results);
    }

}
