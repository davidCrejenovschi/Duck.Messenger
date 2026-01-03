package entities.messages;
import entities.users.AbstractUser;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Message {

    private long id;
    private final AbstractUser sender;
    private final Set<AbstractUser> recipients;
    private final String content;
    private final LocalDateTime timestamp;

    public Message(AbstractUser sender, String content) {
        if (sender == null) {
            throw new IllegalArgumentException("Message must have a sender.");
        }
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Message content cannot be empty.");
        }

        this.sender = sender;
        this.content = content;
        this.recipients = new HashSet<>();
        this.timestamp = LocalDateTime.now();
    }

    public Message(long id, AbstractUser sender, String content, LocalDateTime timestamp) {
        this.id = id;
        this.sender = sender;
        this.content = content;
        this.timestamp = timestamp;
        this.recipients = new HashSet<>();
    }

    public void setId(long id) {
        this.id = id;
    }

    public void addReceiver(AbstractUser receiver) {
        if (receiver == null) {
            throw new IllegalArgumentException("Receiver cannot be null.");
        }
        if (receiver.equals(this.sender)) {
            throw new IllegalArgumentException("You cannot send a message to yourself.");
        }
        this.recipients.add(receiver);
    }

    public void removeReceiver(AbstractUser receiver) {
        if (receiver == null) {
            throw new IllegalArgumentException("Cannot remove a null receiver.");
        }
        this.recipients.remove(receiver);
    }

    public long getId() {
        return id;
    }

    public AbstractUser getSender() {
        return sender;
    }

    public Set<AbstractUser> getRecipients() {
        return Collections.unmodifiableSet(recipients);
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return id == message.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}