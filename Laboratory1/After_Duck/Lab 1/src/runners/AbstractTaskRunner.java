package runners;
import tasks.Task;

public abstract class AbstractTaskRunner implements TaskRunner {

    TaskRunner taskRunner;

    public AbstractTaskRunner(TaskRunner taskRunner_init){
        taskRunner = taskRunner_init;
    }

    @Override
    public void executeOneTask() {
        taskRunner.executeOneTask();
    }

    @Override
    public void executeAll() {
        taskRunner.executeAll();
    }

    @Override
    public void addTask(Task t) {
        taskRunner.addTask(t);
    }

    @Override
    public boolean hasTask() {
        return taskRunner.hasTask();
    }
}
