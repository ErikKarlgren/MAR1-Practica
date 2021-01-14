package ucm.erikkarl.tests;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static ucm.erikkarl.tests.TestManager.START_OF_TEST;

/**
 * Clase que crea tests aleatorios. El usuario especifica el numero de grafos
 * y el numero de nodos que tiene cada uno.
 * <p>
 * El coste para crear el test esta en el orden de O(k*n^2), siendo
 * n el numero de nodos del grafo y k el numero de casos.
 */
public final class RandomTestCasesCreator {
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
    public RandomTestCasesCreator(int numberOfCases, int nodesPerGraph) {
        this.numberOfCases = numberOfCases;
        this.nodesPerGraph = nodesPerGraph;
        this.nodesRange = IntStream.rangeClosed(1, nodesPerGraph).boxed().collect(Collectors.toUnmodifiableList());
    }

    /**
     * Crea un fichero de test de nombre <code>fileName</code>.
     */
    public final void createTestFile(String fileName) {
        var stringBuilder = new StringBuilder();

        try (var file = new FileWriter(fileName)) {
            for (int i = 0; i < numberOfCases; i++) {
                stringBuilder.append(START_OF_TEST).append('\n');
                addRandomGraphAsStringTo(stringBuilder);
            }
            String text = stringBuilder.toString();
            file.write(text);

        } catch (IOException e) {
            System.err.println("Either the file cannot be created or opened or it is a directory.");
        }
    }

    /**
     * Crea la representacion de un grafo aleatorio y lo añade al StringBuilder <code>text</code>.
     * Coste cuadratico en funcion del numero de nodos del grafo.
     */
    private void addRandomGraphAsStringTo(StringBuilder text) {
        for (Integer i : nodesRange) { // O(n^2)
            var adjacentNodesText = new StringBuilder();
            var adjacentNodes = randomQuantityOfRandomNumbers(i, nodesRange); // O(n)
            adjacentNodes.forEach(x -> adjacentNodesText.append(x).append(" "));

            text.append(i).append("\n");
            text.append(adjacentNodesText.toString().trim()).append("\n");
        }
    }

    /**
     * Crea una lista de enteros aleatorios excluyendo <code>node</code> que esta en el
     * rango <code>range</code>. Coste lineal en funcion de la longitud de <code>range</code>,
     * que coincide con el numero de vertices que tienen los grafos creados por este objeto (n).
     */
    private List<Integer> randomQuantityOfRandomNumbers(Integer node, List<Integer> range) {
        var usedNumbers = new LinkedList<Integer>();
        var unusedNumbers = new LinkedList<>(range); // O(n)
        unusedNumbers.remove(node); // to avoid single-node-loops
        Collections.shuffle(unusedNumbers, RANDOM); // O(n)

        // Creates good enough test files for small number of nodes, so don't bother changing this...
        var log2ofNodesPerGraph = Math.log(nodesPerGraph) / Math.log(2);
        int maxQuantity = Math.max(1, (int) (log2ofNodesPerGraph));
        if (maxQuantity == 1 && nodesPerGraph > 1) maxQuantity = 2;
        double prob = (1.0 / log2ofNodesPerGraph) * 2;
        int quantity = RANDOM.nextInt(maxQuantity) / (RANDOM.nextDouble() < prob ? 1 : 2);

        for (int i = 0; i < quantity; i++) { // O(n)
            usedNumbers.add(unusedNumbers.pop());
        }
        assert !usedNumbers.isEmpty();
        return usedNumbers;
    }
}
