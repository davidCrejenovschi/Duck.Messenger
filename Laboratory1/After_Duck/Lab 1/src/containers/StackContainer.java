package containers;
import tasks.Task;

public class StackContainer extends AbstractContainer {


    @Override
    public Task remove(){

        if(nr==-1){
            return null;
        }

        Task task = tasks[nr];
        tasks[nr] = null;
        nr--;
        return task;

    }

    @Override
    public void add(Task task) {

        tasks[++nr] = task;

    }

}
