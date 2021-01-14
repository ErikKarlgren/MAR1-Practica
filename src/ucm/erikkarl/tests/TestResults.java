package ucm.erikkarl.tests;

import java.util.List;

/**
 * Clase que encapsula datos sobre la ejecucion de un test. Incluye el tiempo de ejecucion,
 * el numero de casos, el fichero donde se guardan los resultados y las soluciones obtenidas
 * con cada grafo.<p>
 * Los tiempos se miden en nanosegundos para poder calcular los milisegundos con decimales si
 * asi lo desea el usuario.
 */
public final class TestResults {
    private final float totalElapsedTime;
    private final int numberOfCases;
    private final List<Result> results;
    private String fileResultsName;

    TestResults(float totalElapsedTime, int numberOfCases, List<Result> results) {
        this.totalElapsedTime = totalElapsedTime;
        this.numberOfCases = numberOfCases;
        this.results = results;
    }

    /**
     * @return Tiempo medio de ejecucion de cada test en nanosegundos.
     */
    public final float mediumElapsedTimePerCase() {
        return this.numberOfCases > 0 ? this.totalElapsedTime / (long) this.numberOfCases : 0f;
    }

    public final float getTotalElapsedTime() {
        return this.totalElapsedTime;
    }

    public final int getNumberOfCases() {
        return this.numberOfCases;
    }

    public final List<Result> getResults() {
        return this.results;
    }

    public String getFileResultsName() {
        return fileResultsName;
    }

    public void setFileResultsName(String fileResultsName) {
        this.fileResultsName = fileResultsName;
    }

    /**
     * Contiene el tiempo necesario para calcular la solucion
     * del algoritmo y la solucion misma representada como una
     * cadena de texto.
     */
    public static final class Result {
        private final double meanTimeElapsed;
        private final String resultAsString;

        public Result(float time, String str) {
            meanTimeElapsed = time;
            resultAsString = str;
        }

        public double getMeanTimeElapsed() {
            return meanTimeElapsed;
        }

        public String getResultAsString() {
            return resultAsString;
        }
    }
}
