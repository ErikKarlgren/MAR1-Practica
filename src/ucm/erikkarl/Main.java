package ucm.erikkarl;

import ucm.erikkarl.cli.GraphCLI;

public final class Main {
    public static void main(String[] args) {
        try{
            GraphCLI.showIntroduction();
            while (true) {
                GraphCLI.readCommand();
            }
        } catch (Exception e){
            System.err.println("Execution aborted");
        }
        
    }
}
