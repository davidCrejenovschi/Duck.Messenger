package entities.pairs;
import entities.pairs.abstracts.AbstractPair;

public class Pair<L, R> extends AbstractPair<L, R> {

    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }
}
