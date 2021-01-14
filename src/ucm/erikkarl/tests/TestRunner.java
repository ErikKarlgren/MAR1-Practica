package ucm.erikkarl.tests;

import ucm.erikkarl.Either;
import ucm.erikkarl.Exercise4;
import ucm.erikkarl.Graph;

import java.util.LinkedList;
import java.util.List;

final class TestRunner {
    private static final int MS_LIMIT = 10;
    private static final long NS_LIMIT = MS_LIMIT * 1000000L;

    private TestRunner() {
    }

    /**
     * Ejecuta el algoritmo sobre los grafos de la lista <code>graphs</code>.
     */
    static TestResults run(List<Graph<Integer>> graphs) {
        float totalElapsedTimeInMs = 0;
        List<TestResults.Result> results = new LinkedList<>();

        for (Graph<Integer> graph : graphs) {
            var result = runTest(graph);
            results.add(result);
            totalElapsedTimeInMs += result.getMeanTimeElapsed();
        }

        return new TestResults(totalElapsedTimeInMs, graphs.size(), results);
    }

    /**
     * Ejecuta el test sobre el grafo <code>graph</code> y devuelve su resultado.
     */
    private static TestResults.Result runTest(Graph<Integer> graph) {
        List<Either<Integer, List<Integer>>> solution = new LinkedList<>();
        long elapsedTimeInNs = 0;
        int loops = 0;
        int loopsLimit = 3;
        boolean loopsLimitSet = false;

        // Si el test dura menos de 10 ms, se repite 100 veces.
        // Lo hacemos al menos 3 veces.
        while (loops < loopsLimit) {
            long startTime = System.nanoTime();
            solution = Exercise4.solve(graph);
            long finalTime = System.nanoTime();
            elapsedTimeInNs += finalTime - startTime;
            loops++;

            if (!loopsLimitSet && (finalTime - startTime) < NS_LIMIT) {
                loopsLimit = 100;
                loopsLimitSet = true;
            }
        }
        // Calculamos la media de los tiempos de ejecucion del test
        elapsedTimeInNs /= loops;
        float elapsedTimeInMs = (float) (elapsedTimeInNs / 1000000.0);

        return new TestResults.Result(elapsedTimeInMs, solution, graph.getNumberOfNodes());
    }
}
