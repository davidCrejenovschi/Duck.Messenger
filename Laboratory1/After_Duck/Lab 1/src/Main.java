import entities.RaceContext;
import runners.DelayTaskRunner;
import runners.StrategyTaskRunner;
import runners.TaskRunner;
import sorters.AbstractSorter;
import sorters.QuickSort;
import strategies.forcontainers.Strategy;
import strategies.forrace.GreedyBinarySearchSolver;
import strategies.forrace.RaceSolverStrategy;
import tasks.ReadFromFileTask;
import tasks.SolveDucksTask;
import tasks.WriteOnFileTask;
import tests.Test;

void main(String[] args) {

    //Test.testAll(args);

    TaskRunner taskRunner = new StrategyTaskRunner(Strategy.FIFO);
    DelayTaskRunner delayTaskRunner = new DelayTaskRunner(taskRunner);

    RaceContext raceContext = new RaceContext("C:\\Users\\creje\\IdeaProjects\\Lab 1\\src\\ScreenIN.txt", "C:\\Users\\creje\\IdeaProjects\\Lab 1\\src\\ScreenOUT.txt" );

    AbstractSorter abstractSorter = new QuickSort();
    RaceSolverStrategy raceSolverStrategy = new GreedyBinarySearchSolver();

    ReadFromFileTask readFromFileTask = new ReadFromFileTask("", "", raceContext);
    SolveDucksTask solveDucksTask = new SolveDucksTask("", "", raceContext, abstractSorter, raceSolverStrategy);
    WriteOnFileTask  writeOnFileTask = new WriteOnFileTask("", "", raceContext);

    delayTaskRunner.addTask(readFromFileTask);
    delayTaskRunner.addTask(solveDucksTask);
    delayTaskRunner.addTask(writeOnFileTask);

    delayTaskRunner.executeAll();

}
