package runners;
import strategies.forcontainers.Strategy;
import tasks.Task;
import containers.Container;
import factories.Factory;
import factories.TaskContainerFactory;

public class StrategyTaskRunner implements TaskRunner {

    final private Container container;

    public StrategyTaskRunner(Strategy strategy) {

        Factory factory = TaskContainerFactory.getInstance();
        container = factory.createContainer(strategy);
    }

    @Override
    public void executeOneTask() {

        if(container.isEmpty()){
            Task task = container.remove();
            task.execute();
        }
    }

    @Override
    public void executeAll() {

        while(container.isEmpty()){
            Task task = container.remove();
            task.execute();
        }
    }

    @Override
    public void addTask(Task t) {
        container.add(t);
    }

    @Override
    public boolean hasTask() {
        return container.isEmpty();
    }

}
