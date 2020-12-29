package ucm.erikkarl;

public final class Main {
    public static void main(String[] args) {
        GraphCLI.INSTANCE.showIntroduction();
        while(true) {
            GraphCLI.INSTANCE.readCommand();
        }
    }
}
