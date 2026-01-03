package validators;
import entities.dtos.MessageDTO;
import entities.messages.Message;
import entities.messages.ReplyMessage;
import entities.users.AbstractUser;
import exceptions.ValidationException;
import java.util.Set;

public class MessageValidator {

    public Message validate(MessageDTO messageDTO) throws ValidationException {

        if (messageDTO == null) {
            throw new ValidationException("MessageDTO cannot be null!");
        }

        AbstractUser sender = validateSender(messageDTO.sender().orElse(null));
        Set<AbstractUser> recipients = validateRecipient(messageDTO.recipient().orElse(null), sender);
        String content = validateContent(messageDTO.content().orElse(null));
        Message quotedMessage = messageDTO.quotedMessage().orElse(null);

        if (quotedMessage == null) {
            Message message =  new Message(sender, content);
            for (AbstractUser recipient : recipients) {
                message.addReceiver(recipient);
            }
            return message;

        } else {
            ReplyMessage replyMessage = new ReplyMessage(sender, content, quotedMessage);
            for (AbstractUser recipient : recipients) {
                replyMessage.addReceiver(recipient);
            }
            return  replyMessage;

        }
    }

    private AbstractUser validateSender(AbstractUser sender) throws ValidationException {

        if (sender == null) {
            throw new ValidationException("Message sender is mandatory.");
        }

        return sender;
    }

    private Set<AbstractUser> validateRecipient(Set<AbstractUser> recipients, AbstractUser sender) throws ValidationException {

        if  (recipients == null) {
            throw new ValidationException("Message recipient is mandatory.");
        }

        if (recipients.isEmpty()) {
            throw new ValidationException("Message must have at least one recipient.");
        }

        for (AbstractUser recipient : recipients) {

            if (recipient == null) {
                throw new ValidationException("Recipient list contains a null user.");
            }
            if (recipient.equals(sender)) {
                throw new ValidationException("Sender cannot be a recipient of the same message.");
            }
        }
        return recipients;
    }

    private String validateContent(String content) throws ValidationException {

        if (content == null || content.trim().isEmpty()) {
            throw new ValidationException("Message content is mandatory.");
        }
        return content;
    }

}