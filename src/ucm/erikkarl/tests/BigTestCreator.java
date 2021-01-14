package ucm.erikkarl.tests;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

final class BigTestCreator {
    // Lista de enteros: 1, 2, ..., 9, 10, 20, ..., 90, 100, 200...
    private static final List<Integer> nodesNumberList;

    static {
        nodesNumberList = new LinkedList<>();
        for (int n = 1; n < 10000; n *= 10) {
            for (int i = 1; i < 10; i++) {
                nodesNumberList.add(n * i);
            }
        }
    }

    private BigTestCreator() {
    }

    public static void create(String fileName) {
        try (var file = new FileWriter(fileName)) {
            for (Integer n : nodesNumberList) {
                var graph = new RandomGraphGenerator(n).createGraph();
                file.write(TestManager.graphToString(graph));
            }
        } catch (IOException e) {
            System.err.println("Error: could not create big test.");
        }
    }

}
