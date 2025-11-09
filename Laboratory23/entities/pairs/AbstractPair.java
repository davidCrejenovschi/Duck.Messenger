package entities.pairs;

public abstract class AbstractPair<L, R> implements Pair<L, R> {

    public  L left;
    public  R right;

    @Override
    public L getLeft() { return left; }
    @Override
    public R getRight() { return right; }
}
