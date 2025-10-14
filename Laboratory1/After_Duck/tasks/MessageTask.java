package  tasks;
import entities.Message;

public class MessageTask extends Task{

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
        return "id="+getTaskId()+
                "|description="+getDescription()+
                "|message="+message.getBody()+
                "|from="+message.getTheSender()+
                "|to="+message.getTheReceiver()+
                "|date="+message.getTime();
    }
}