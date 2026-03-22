package entities.messages;
import entities.users.AbstractUser;
import java.time.LocalDateTime;

public class ReplyMessage extends Message {

    private final Message quotedMessage;

    public ReplyMessage(AbstractUser sender, String content, Message messageToReplyTo) {
        super(sender, content);
        if (messageToReplyTo == null) {
            throw new IllegalArgumentException("You must reply to a specific message.");
        }
        this.quotedMessage = messageToReplyTo;
    }

    public ReplyMessage(long id, AbstractUser sender, String content, LocalDateTime timestamp, Message quotedMessage) {
        super(id, sender, content, timestamp);
        this.quotedMessage = quotedMessage;
    }

    public Message getQuotedMessage() {
        return quotedMessage;
    }
}