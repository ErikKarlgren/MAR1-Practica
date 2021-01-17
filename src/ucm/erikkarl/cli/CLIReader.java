package ucm.erikkarl.cli;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

final class CLIReader {
    /**
     * Scanner que lee de la entrada estandar.
     */
    static final Scanner stdin = new Scanner(System.in);

    private CLIReader() {
    }

    /*
        public static void readCommand() {
        System.out.print(">>> ");
        String commandText = stdin.nextLine();
        var optionalCommand = Arrays.stream(Command.values()).filter(x -> x.toString().equals(commandText)).findFirst();
        if (optionalCommand.isPresent())
            optionalCommand.get().execute();
        else {
            System.err.println("Error: this command doesn't exist.");
            System.err.println("Write " + Command.HELP.toString() + " to see the commands available.");
        }
    }
     */

    static Optional<Command> readCommand() {
        String commandText = stdin.nextLine();
        return Arrays.stream(Command.values()).filter(x -> x.toString().equals(commandText)).findFirst();
    }

    /**
     * Lee el nombre de un fichero por consola. No tiene por que existir pues esta funcion
     * puede ser usada para leer el nombre de un fichero a crear.
     */
    static String readFileName() {
        String line = "";
        boolean validInput = false;

        while (!validInput) {
            line = stdin.nextLine();
            if (!line.isBlank() && !line.contains(" "))
                validInput = true;
            else {
                System.err.println("Invalid input: file name shouldn't be blank nor have whitespaces.");
            }
        }
        return line.trim();
    }

    /**
     * Lee un solo entero por consola.
     */
    static int readNumber() {
        String line;
        int number = 0;
        boolean validInput = false;

        while (!validInput) {
            line = stdin.nextLine();
            try {
                number = Integer.parseInt(line);
                validInput = true;

            } catch (NumberFormatException e) {
                System.err.println("Invalid input: write a number.");
            }
        }

        return number;
    }

    /**
     * Lee una lista de nodos por consola. Todos deben ser enteros separados por espacios.
     */
    static List<Integer> readNodesList() {
        Function<String, List<Integer>> formatToNodesList = (String x) ->
                Arrays.stream(x.split(" ")).map(Integer::valueOf).collect(Collectors.toList());
        String line;
        List<Integer> nodes = new LinkedList<>();
        boolean validInput = false;

        while (!validInput) {
            line = stdin.nextLine();
            try {
                nodes = formatToNodesList.apply(line);
                validInput = true;
            } catch (NumberFormatException e) {
                System.err.println("Invalid input: write a list of numbers.");
            }
        }
        return nodes;
    }
}
