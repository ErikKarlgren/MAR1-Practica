package ucm.erikkarl.tests;

import ucm.erikkarl.Either;

import java.util.List;

/**
 * Clase que encapsula datos sobre la ejecucion de un test. Incluye el tiempo de ejecucion,
 * el numero de casos, el fichero donde se guardan los resultados y las soluciones obtenidas
 * con cada grafo.<p>
 * Los tiempos se miden en nanosegundos para poder calcular los milisegundos con decimales si
 * asi lo desea el usuario.
 */
public final class TestResults {
    private final float totalMediumElapsedTime;
    private final int numberOfCases;
    private final List<Result> results;
    private String solutionsFileName;
    private String executionTimeFileName;

    TestResults(float totalElapsedTime, int numberOfCases, List<Result> results) {
        this.totalMediumElapsedTime = totalElapsedTime;
        this.numberOfCases = numberOfCases;
        this.results = results;
    }

    /**
     * @return Tiempo medio de ejecucion de cada test en nanosegundos.
     */
    public final float mediumElapsedTimePerCase() {
        return this.numberOfCases > 0 ? this.totalMediumElapsedTime / (long) this.numberOfCases : 0f;
    }

    public final float getTotalMediumElapsedTime() {
        return this.totalMediumElapsedTime;
    }

    public final int getNumberOfCases() {
        return this.numberOfCases;
    }

    public final List<Result> getResults() {
        return this.results;
    }

    public String getSolutionsFileName() {
        return solutionsFileName;
    }

    public void setSolutionsFileName(String solutionsFileName) {
        this.solutionsFileName = solutionsFileName;
    }

    public String getExecutionTimeFileName() {
        return executionTimeFileName;
    }

    public void setExecutionTimeFileName(String executionTimeFileName) {
        this.executionTimeFileName = executionTimeFileName;
    }

    /**
     * Contiene el tiempo necesario para calcular la solucion
     * del algoritmo y la solucion misma representada como una
     * cadena de texto.
     */
    public static final class Result {
        private final double meanTimeElapsed;
        private final List<Either<Integer, List<Integer>>> result;
        private final int graphNodesNumber;
        private final int graphEdgesNumber;

        public Result(float time,
                      List<Either<Integer, List<Integer>>> result,
                      int graphNodesNumber,
                      int graphEdgesNumber) {
            meanTimeElapsed = time;
            this.result = result;
            this.graphNodesNumber = graphNodesNumber;
            this.graphEdgesNumber = graphEdgesNumber;
        }

        public double getMeanTimeElapsed() {
            return meanTimeElapsed;
        }

        public List<Either<Integer, List<Integer>>> getResult() {
            return result;
        }

        public String getResultAsString() {
            return result.toString();
        }

        public int getGraphNodesNumber() {
            return graphNodesNumber;
        }

        public int getGraphEdgesNumber() {
            return graphEdgesNumber;
        }
    }
}
