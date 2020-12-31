package ucm.erikkarl;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public final class RandomTestCasesCreator {
    private static final Random random = new Random();

    private final int numberOfCases;
    private final int nodesPerGraph;
    private final List<Integer> nodesRange;

    public final void createTests(String fileName) {
        var stringBuilder = new StringBuilder();

        try (var file = new FileWriter(fileName)) {
            for (int i = 0; i < numberOfCases; i++) {
                this.createRandomTest(stringBuilder);
            }
            file.write(stringBuilder.toString());
        } catch (IOException e) {
            System.err.println("Either the file cannot be created or opened or it is a directory.");
        }
    }

    private void createRandomTest(StringBuilder text) {
        for (Integer i : nodesRange) {
            var adjacentNodesText = new StringBuilder();
            var adjacentNodes = randomQuantityOfRandomNumbers(nodesRange);
            adjacentNodes.forEach(x -> adjacentNodesText.append(x).append(" "));

            text.append(i).append("\n");
            text.append(adjacentNodesText.toString().trim()).append("\n");
        }
        text.append("\n");
    }

    private List<Integer> randomQuantityOfRandomNumbers(List<Integer> range) {
        var usedNumbers = new LinkedList<Integer>();
        var unusedNumbers = new LinkedList<>(range);
        Collections.shuffle(unusedNumbers, random);

        int maxQuantity = Math.max(1, (int) (Math.log(nodesPerGraph) / 2));
        int quantity = random.nextInt(maxQuantity + 1);

        for (int i = 0; i < quantity; i++) {
            usedNumbers.add(unusedNumbers.pop());
        }
        assert !usedNumbers.isEmpty();
        return usedNumbers;
    }

    public RandomTestCasesCreator(int numberOfCases, int nodesPerGraph) {
        this.numberOfCases = numberOfCases;
        this.nodesPerGraph = nodesPerGraph;
        this.nodesRange = IntStream.rangeClosed(1, nodesPerGraph).boxed().collect(Collectors.toUnmodifiableList());
    }
}
