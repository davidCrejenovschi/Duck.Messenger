
public interface Container {

    Task remove();
    void add(Task task);
    int size();
    boolean isEmpty();

}

abstract class AbstractContainer implements Container {

    int[] tasks;
    int nr;

    @Override
    public int size(){
        return nr+1;
    }

    @Override
    public boolean isEmpty(){
        return nr==-1;
    }

    @Override
    public abstract Task remove();

    @Override
    public abstract void add(Task task);
}

class StackContainer extends AbstractContainer{

    Task[] tasks = new Task[10];
    int nr=-1;

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

    @Override
    public int size() {
        return nr+1;
    }

    @Override
    public boolean isEmpty() {
        return nr==-1;
    }
}

class QueueContainer extends AbstractContainer{

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
        return nr==-1;
    }
}