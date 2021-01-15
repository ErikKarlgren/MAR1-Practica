package ucm.erikkarl.tests;

import ucm.erikkarl.cli.GraphCLI;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public final class BigTestCreator {
    // Lista de enteros: 1, 2, ..., 9, 10, 20, ..., 90, 100, 200...
    private static final List<Integer> nodesNumberList;

    static {
        nodesNumberList = new LinkedList<>();
        for (int n = 1; n < 10000; n += 100) {
            nodesNumberList.add(n);
        }
    }

    private BigTestCreator() {
    }

    public static void create(String fileName) {
        try (var file = new BufferedWriter(new FileWriter(fileName))) {
            for (Integer n : nodesNumberList) {
                var graph = new RandomGraphGenerator(n).createGraph();
                file.write(TestManager.graphToString(graph));
                GraphCLI.printMessage("Created graph with " + n + " nodes");
            }
        } catch (IOException e) {
            System.err.println("Error: could not create big test.");
        }
    }

}
