package runners;

public class PrinterTaskRunner extends AbstractTaskRunner {

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
