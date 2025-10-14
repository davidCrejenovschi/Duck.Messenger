package tasks;
import entities.*;
import sorters.AbstractSorter;
import strategies.forrace.RaceSolverStrategy;

public class SolveDucksTask extends Task {

    private final RaceContext raceContext;
    private final AbstractSorter sorter;
    private final RaceSolverStrategy solver;
    private Duck[] ducks;
    private Lane[] lanes;
    private Duck[] chosenDucks;

    public SolveDucksTask(String taskId_init, String description_init, RaceContext race_context_init, AbstractSorter sorter_init, RaceSolverStrategy race_solver_init) {

        super(taskId_init, description_init);
        this.raceContext = race_context_init;
        this.sorter = sorter_init;
        this.solver = race_solver_init;
    }

    @Override
    public void execute() {

        Duck[] ducks = raceContext.getDucks();
        Lane[] lanes = raceContext.getLanes();
        Duck[] chosenDucks = new Duck[lanes.length];

        sorter.sort(ducks, ducks.length);
        double bestTime = solver.solve(ducks, lanes, chosenDucks);
        raceContext.setTime(bestTime);
        raceContext.setChosenDucks(chosenDucks);
    }
}