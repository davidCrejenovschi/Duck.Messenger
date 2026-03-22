package services;

import entities.dtos.MessageDTO;
import entities.messages.Message;
import exceptions.ValidationException;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import repositories.MRepository;
import validators.MessageValidator;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public class MessageService {

    private final MRepository messageRepository;
    private final MessageValidator messageValidator;
    private final ObjectProperty<Message> messageChangeProperty = new SimpleObjectProperty<>();

    public MessageService(MRepository messageRepository, MessageValidator messageValidator) {
        this.messageRepository = messageRepository;
        this.messageValidator = messageValidator;
    }

    public ObjectProperty<Message> getMessageChangeProperty() {
        return messageChangeProperty;
    }

    public void AddMessage(MessageDTO messageDTO) throws ValidationException {
        Message message = messageValidator.validate(messageDTO);
        messageRepository.add(message);
        messageChangeProperty.set(message);
    }

    public void deleteMessage(long id) {
        messageRepository.delete(id);
    }

    public Message getMessageById(long id) {
        return messageRepository.getById(id);
    }

    public Collection<Message> getAllMessages() {
        return messageRepository.getAll();
    }

    public Collection<Message> getAllMessagesByIds(List<Long> ids) {
        return messageRepository.getByIds(ids);
    }

    public List<Message> getUserConversation(long userId, long friendId, LocalDateTime before, int pageSize) {
        return messageRepository.getConversationBefore(userId, friendId, before, pageSize);
    }

}