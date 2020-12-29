package ucm.erikkarl;


public abstract class Either<A, B> {

    @Override
    public abstract String toString();

    private Either() {
    }


    public static final class Left<A, B> extends Either<A, B> {
        private final A left;

        public A getValue() { return left; }

        @Override
        public String toString() {
            return String.valueOf(this.left);
        }

        public Left(A left) {
            this.left = left;
        }
    }


    public static final class Right<A, B> extends Either<A, B> {
        private final B right;

        public B getValue() { return right; }

        @Override
        public String toString() {
            return String.valueOf(this.right);
        }

        public Right(B right) {
            this.right = right;
        }
    }
}
