package containers;
import tasks.Task;

public class QueueContainer extends AbstractContainer {

    Task[] tasks = new Task[10];
    int nr=-1;

    @Override
    public Task remove(){

        if(nr==-1){
            return null;
        }

        Task task = tasks[0];
        for(int i=0;i<nr;i++){
            tasks[i] = tasks[i+1];
        }
        tasks[nr] = null;
        nr--;
        return task;

    }

    @Override
    public void add(Task task) {

        tasks[++nr] = task;

    }

    @Override
    public int size() {
        return nr+1;
    }

    @Override
    public boolean isEmpty() {
        return nr != -1;
    }
}
