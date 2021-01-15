package ucm.erikkarl.tests;

import ucm.erikkarl.Graph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static ucm.erikkarl.tests.TestManager.START_OF_GRAPH;

final class TestFileParser {
    private TestFileParser() {
    }

    /**
     * Lee un fichero de test y crea una lista de grafos.
     */
    static List<Graph<Integer>> parse(File file) {
        var graphs = new LinkedList<Graph<Integer>>();
        var testCases = new LinkedList<List<String>>();
        testCases.add(new LinkedList<>());

        try (var br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().equals(START_OF_GRAPH))
                    testCases.getLast().add(line.trim());
                else
                    testCases.add(new LinkedList<>());
            }
        } catch (IOException e) {
            System.err.println("Error while reading tests file");
        }
        testCases.removeFirst();  // the first element is an empty string
        for (List<String> testLine : testCases) {
            graphs.add(parseGraph(testLine));
        }
        return graphs;
    }

    /**
     * Crea un grafo a partir de una lista de lineas de texto que lo
     * representa.
     */
    private static Graph<Integer> parseGraph(List<String> testCaseLines) {
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
}
