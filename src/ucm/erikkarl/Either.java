package ucm.erikkarl;


public abstract class Either<A, B> {

    private Either() {
    }

    @Override
    public abstract String toString();

    public abstract Object getValue();

    public static final class Left<A, B> extends Either<A, B> {
        private final A value;

        public Left(A value) {
            this.value = value;
        }

        @Override
        public A getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(this.value);
        }
    }


    public static final class Right<A, B> extends Either<A, B> {
        private final B value;

        public Right(B value) {
            this.value = value;
        }

        @Override
        public B getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(this.value);
        }
    }
}
