package ucm.erikkarl.tests;

import ucm.erikkarl.graph.Graph;

import java.io.File;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Clase encargada de leer ficheros de test, crear grafos a partir de ellos
 * y hallar el orden topologico o las componentes fuertemente conexas de cada uno
 * segun proceda. Guarda las soluciones en un fichero al igual que algunos datos
 * de su ejecucion.
 */
public final class TestManager {
    public static final String START_OF_GRAPH = "graph:";

    private TestManager() {
    }

    /**
     * Lee el test del fichero con nombre <code>fileName</code>, ejecuta el test y
     * devuelve los resultados en un <code>Optional</code>. Si el fichero
     * no existe devuelve un <code>Optional</code> vacio.
     */
    public static Optional<TestResults> runTest(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            TestResults results = TestRunner.run(TestFileParser.parse(file));
            TestResultsWriter.writeSolutionsToFile(results, file);
            TestResultsWriter.writeExecutionTimeToFile(results, file);
            return Optional.of(results);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Devuelve una representacion como cadena de texto de un grafo.
     */
    public static String graphToString(Graph<Integer> graph) {
        var sb = new StringBuilder();
        sb.append(TestManager.START_OF_GRAPH).append('\n');

        for (Map.Entry<Integer, Set<Integer>> entry : graph) {
            sb.append(entry.getKey()).append('\n');
            for (Integer n : entry.getValue())
                sb.append(n).append(' ');
            sb.append('\n');
        }
        return sb.toString();
    }
}
