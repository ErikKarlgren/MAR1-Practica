package ucm.erikkarl;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public final class RandomTestCasesCreator {
    public static final String START_OF_TEST = "graph:";
    private static final Random RANDOM = new Random();
    private final int numberOfCases;
    private final int nodesPerGraph;
    private final List<Integer> nodesRange;

    public RandomTestCasesCreator(int numberOfCases, int nodesPerGraph) {
        this.numberOfCases = numberOfCases;
        this.nodesPerGraph = nodesPerGraph;
        this.nodesRange = IntStream.rangeClosed(1, nodesPerGraph).boxed().collect(Collectors.toUnmodifiableList());
    }

    public final void createTests(String fileName) {
        var stringBuilder = new StringBuilder();

        try (var file = new FileWriter(fileName)) {
            for (int i = 0; i < numberOfCases; i++) {
                stringBuilder.append(START_OF_TEST).append('\n');
                this.createRandomTest(stringBuilder);
            }
            String text = stringBuilder.toString();
            file.write(text);

        } catch (IOException e) {
            System.err.println("Either the file cannot be created or opened or it is a directory.");
        }
    }

    private void createRandomTest(StringBuilder text) {
        for (Integer i : nodesRange) {
            var adjacentNodesText = new StringBuilder();
            var adjacentNodes = randomQuantityOfRandomNumbers(i, nodesRange);
            adjacentNodes.forEach(x -> adjacentNodesText.append(x).append(" "));

            text.append(i).append("\n");
            text.append(adjacentNodesText.toString().trim()).append("\n");
        }
    }

    private List<Integer> randomQuantityOfRandomNumbers(Integer node, List<Integer> range) {
        var usedNumbers = new LinkedList<Integer>();
        var unusedNumbers = new LinkedList<>(range);
        unusedNumbers.remove(node); // to avoid single-node-loops
        Collections.shuffle(unusedNumbers, RANDOM);

        // Creates good enough test files for small number of nodes, so don't bother changing this...
        var log2ofNodesPerGraph = Math.log(nodesPerGraph) / Math.log(2);
        int maxQuantity = Math.max(1, (int) (log2ofNodesPerGraph));
        if (maxQuantity == 1 && nodesPerGraph > 1) maxQuantity = 2;
        double prob = (1.0 / log2ofNodesPerGraph) * 2;
        int quantity = RANDOM.nextInt(maxQuantity) / (RANDOM.nextDouble() < prob ? 1 : 2);

        for (int i = 0; i < quantity; i++) {
            usedNumbers.add(unusedNumbers.pop());
        }
        assert !usedNumbers.isEmpty();
        return usedNumbers;
    }
}
