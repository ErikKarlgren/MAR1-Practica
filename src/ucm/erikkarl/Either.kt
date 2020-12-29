package ucm.erikkarl

sealed class Either<A, B> {
    class Left<A, B>(val left: A) : Either<A, B>() {
        override fun toString(): String {
            return left.toString()
        }
    }

    class Right<A, B>(val right: B) : Either<A, B>() {
        override fun toString(): String {
            return right.toString()
        }
    }

    abstract override fun toString(): String
}