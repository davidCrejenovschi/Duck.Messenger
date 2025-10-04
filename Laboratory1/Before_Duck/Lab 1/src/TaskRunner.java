
public interface TaskRunner{

    void executeOneTask();
    void executeAll();
    void addTask(Task t);
    boolean hasTask();
}

abstract class AbstractTaskRunner implements TaskRunner{

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

class PrinterTaskRunner extends AbstractTaskRunner{

    public PrinterTaskRunner(TaskRunner taskRunner){
        super(taskRunner);
    }

    @Override
    public void executeOneTask() {
        taskRunner.executeOneTask();
        System.out.println("Task executed at: " + java.time.LocalTime.now()+"\n");
    }

    @Override
    public void executeAll() {
        while(hasTask()){
            executeOneTask();
        }
    }

}

class DelayTaskRunner extends AbstractTaskRunner{

    public DelayTaskRunner(TaskRunner taskRunner){
        super(taskRunner);
    }

    @Override
    public void executeOneTask() {

        super.executeOneTask();

        try{
            Thread.sleep(3000);

        }catch (InterruptedException e){

            e.printStackTrace();
        }
    }

    @Override
    public void executeAll() {
        while(hasTask()){
            executeOneTask();
        }
    }

}

class StrategyTaskRunner implements TaskRunner{

    private Container container;

    public StrategyTaskRunner(Strategy strategy) {

        Factory factory = TaskContainerFactory.getInstance();
        container = factory.createContainer(strategy);
    }


    @Override
    public void executeOneTask() {

        if(!container.isEmpty()){
            Task task = container.remove();
            task.execute();
        }
    }

    @Override
    public void executeAll() {

        while(!container.isEmpty()){
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
        return !container.isEmpty();
    }

}