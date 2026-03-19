package entities.dtos;

import entities.messages.Message;
import entities.users.AbstractUser;
import java.util.Optional;
import java.util.Set;

public class MessageDTO {

    private AbstractUser sender;
    private Set<AbstractUser> recipient;
    private String content;
    private Message quotedMessage;


    public Optional<AbstractUser> sender() {
        return Optional.ofNullable(sender);
    }
    public void setSender(AbstractUser sender) {
        this.sender = sender;
    }

    public Optional<Set<AbstractUser>> recipient() {
        return Optional.ofNullable(recipient);
    }
    public void setRecipient(Set<AbstractUser> recipient) {
        this.recipient = recipient;
    }

    public Optional<String> content() {
        return Optional.ofNullable(content);
    }
    public void setContent(String content) {
        this.content = content;
    }

    public Optional<Message> quotedMessage() {
        return Optional.ofNullable(quotedMessage);
    }
    public void setQuotedMessage(Message quotedMessage) {
        this.quotedMessage = quotedMessage;
    }

}
