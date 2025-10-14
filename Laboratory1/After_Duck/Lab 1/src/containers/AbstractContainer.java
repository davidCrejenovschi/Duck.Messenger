package containers;
import tasks.Task;

public abstract class AbstractContainer implements Container {

    Task[] tasks = new Task[100];
    int nr = -1;

    @Override
    public int size(){
        return nr+1;
    }

    @Override
    public boolean isEmpty(){
        return nr != -1;
    }

    @Override
    public abstract Task remove();

    @Override
    public abstract void add(Task task);
}