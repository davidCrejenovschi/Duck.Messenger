package runners;

public class DelayTaskRunner extends AbstractTaskRunner {

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
