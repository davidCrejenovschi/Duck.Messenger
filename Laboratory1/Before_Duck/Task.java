import java.util.Arrays;
import java.util.Objects;

abstract public class Task{

    String taskId;
    String description;

    public Task(String taskId_init, String description_init){
        taskId = taskId_init;
        description = description_init;
    }

    public void setTaskId(String taskId_new){
        taskId = taskId_new;
    }

    public void setDescription(String description_new){
        description = description_new;
    }

    abstract void execute();

    @Override
    public String toString() {

        return super.toString();
    }

    @Override
    public boolean equals(Object obj) {

        if(this == obj) return true; // the object is equal to itself
        if(obj == null) return false; // // check for null to avoid NullPointerException
        if(getClass() != obj.getClass()) return false; // ensure obj is an instance of the same class
        Task other = (Task)obj; // cast so we can access obj attributes
        return  taskId.equals(other.taskId) && description.equals(other.description); // equal if attributes match

    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId, description);
    }

}

class MessageTask extends Task{

    Message message;

    public MessageTask(String taskId_init, String description_init, Message message_new){
        message = message_new;
        super(taskId_init, description_init);
    }

    @Override
    public void execute() {
        System.out.println(message);
    }

    @Override
    public String toString() {
        return "id="+taskId+
                "|description="+description+
                "|message="+message.getBody()+
                "|from="+message.getTheSender()+
                "|to="+message.getTheReceiver()+
                "|date="+message.getTime();
    }
}

class SortingTask extends Task{

    int[] numbers;
    AbstractSorter sorter;

    public SortingTask(String  taskId_init, String description_init, int[] numbers_inti, AbstractSorter sorter_init){

        super(taskId_init, description_init);
        numbers = numbers_inti;
        sorter = sorter_init;

    }

    @Override
    public void execute() {

        sorter.sort(numbers);
        for (int number : numbers) {
            System.out.print(number + " ");
        }
    }

    @Override
    public String toString() {
        return "Sorted numbers: " + Arrays.toString(numbers);
    }
}