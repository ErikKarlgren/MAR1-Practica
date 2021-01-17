package ucm.erikkarl.tests;

import ucm.erikkarl.graph.RandomGraphGenerator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Clase que crea tests aleatorios. El usuario especifica el numero de grafos
 * y el numero de nodos que tiene cada uno.
 * <p>
 * El coste para crear el test esta en el orden de O(k*n^2), siendo
 * n el numero de nodos del grafo y k el numero de casos.
 */
public final class RandomTestCreator {
    private static final Random RANDOM = new Random();
    private final int numberOfCases;
    private final int nodesPerGraph;
    /**
     * Rango de valores que puede tener un nodo.
     */
    private final List<Integer> nodesRange;

    /**
     * Crea un RandomTestCasesCreator que crea ficheros de test con un numero
     * de casos (o grafos) cada uno con un numero especifico de nodos.
     */
    public RandomTestCreator(int numberOfCases, int nodesPerGraph) {
        this.numberOfCases = numberOfCases;
        this.nodesPerGraph = nodesPerGraph;
        this.nodesRange = IntStream.rangeClosed(1, nodesPerGraph).boxed().collect(Collectors.toUnmodifiableList());
    }

    /**
     * Crea un fichero de test de nombre <code>fileName</code>.
     */
    public final void createTestFile(String fileName) {
        try (var file = new BufferedWriter(new FileWriter(fileName))) {
            for (int i = 0; i < numberOfCases; i++) {
                var graph = new RandomGraphGenerator(nodesPerGraph).createGraph();
                file.write(TestManager.graphToString(graph));
            }
        } catch (IOException e) {
            System.err.println("Either the file cannot be created or opened or it is a directory.");
        }
    }
}
