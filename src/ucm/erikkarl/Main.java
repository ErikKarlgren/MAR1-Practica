package ucm.erikkarl;

import ucm.erikkarl.cli.GraphCLI;

public final class Main {
    public static void main(String[] args) {
        GraphCLI.showIntroduction();
        while (true) {
            GraphCLI.readCommand();
        }
    }
}
