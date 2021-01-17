package ucm.erikkarl.exercise4;

/**
 * Para marcar un nodo segun si se ha terminado de visitar o no o si ni siquiera se ha visitado.
 */
public enum Color {
    /**
     * Nodo sin visitar.
     */
    WHITE,
    /**
     * Nodo que no se ha terminado de visitar.
     */
    GRAY,
    /**
     * Nodo terminado de visitar al igual que todos sus nodos adyacentes.
     */
    BLACK
}
