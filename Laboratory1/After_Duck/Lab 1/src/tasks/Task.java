package tasks;

import java.util.Objects;

abstract public class Task{

    private final String taskId;
    private final String description;

    public Task(String taskId_init, String description_init){
        taskId = taskId_init;
        description = description_init;
    }

    public String getTaskId(){
        return taskId;
    }

    public String getDescription(){
        return description;
    }

    public abstract void execute();

    @Override
    public String toString() {

        return taskId +" "+ description;
    }

    @Override
    public boolean equals(Object obj) {

        if(this == obj) return true;
        if(obj == null) return false;
        if(getClass() != obj.getClass()) return false;
        Task other = (Task)obj;
        return  taskId.equals(other.taskId) && description.equals(other.description);

    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId, description);
    }

}