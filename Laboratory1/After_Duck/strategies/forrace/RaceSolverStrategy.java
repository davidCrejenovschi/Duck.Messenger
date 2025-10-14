package strategies.forrace;
import entities.Duck;
import entities.Lane;

public interface RaceSolverStrategy {
    double solve(Duck[] ducks, Lane[] lanes, Duck[] chosenDucks);
}
