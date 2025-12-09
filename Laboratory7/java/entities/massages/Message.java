package entities.massages;

import java.time.LocalDateTime;

public class Message {
    private long id;
    private long fromUserId;
    private long toUserId;
    private String messageText;
    private LocalDateTime date;
    private String response;

    public Message() {}

    public Message(long fromUserId, long toUserId, String messageText) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.messageText = messageText;
        this.date = LocalDateTime.now();
        this.response = null; // Initial e null
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getFromUserId() { return fromUserId; }
    public void setFromUserId(long fromUserId) { this.fromUserId = fromUserId; }
    public long getToUserId() { return toUserId; }
    public void setToUserId(long toUserId) { this.toUserId = toUserId; }
    public String getMessageText() { return messageText; }
    public void setMessageText(String messageText) { this.messageText = messageText; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public String getResponse() { return response; }
    public void setResponse(String response) { this.response = response; }

    @Override
    public String toString() {
        return "Msg: " + messageText + " | Response: " + (response == null ? "None" : response);
    }
}