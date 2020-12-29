package ucm.erikkarl;

public final class Main {
    public static void main(String[] args) {
        GraphCLI.showIntroduction();
        while(true) {
            GraphCLI.readCommand();
        }
    }
}
