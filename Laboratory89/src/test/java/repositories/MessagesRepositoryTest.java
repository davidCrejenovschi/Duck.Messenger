package repositories;
import connections.DBConnection;
import entities.messages.Message;
import entities.messages.ReplyMessage;
import entities.users.Duck;
import entities.users.Person;
import exceptions.RepositoryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class MessagesRepositoryTest {

    private MessageRepository repository;

    @BeforeEach
    void setUp() {

        DBConnection.setTestMode(true);
        initializeH2Database();
        repository = new MessageRepository();

    }

    private void initializeH2Database() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            String sql = new String(Files.readAllBytes(Paths.get("src/test/resources/schema.sql")));

            stmt.execute(sql);

        } catch (Exception e) {
            throw new RuntimeException("Error creating tables in H2", e);
        }
    }

    @Test
    void AddMessage() {

        PersonRepository personRepo = new PersonRepository();
        Person sender = personRepo.getById(6L);
        Person recipient = personRepo.getById(7L);

        Message message = new Message(sender, "Hello! This is a test message.");
        message.addReceiver(recipient);

        repository.add(message);

        assertTrue(message.getId() > 0, "Message ID must be positive");

        Message fetchedMessage = repository.getById(message.getId());

        assertNotNull(fetchedMessage, "Message should be found in DB");
        assertEquals("Hello! This is a test message.", fetchedMessage.getContent());
        assertEquals(sender.getId(), fetchedMessage.getSender().getId(), "Sender ID must match");
        assertEquals(1, fetchedMessage.getRecipients().size(), "Should have exactly one recipient");

        ReplyMessage reply = new ReplyMessage(recipient, "Hi! Received it loud and clear.", message);
        reply.addReceiver(sender);

        repository.add(reply);

        Message fetchedReplyAsMessage = repository.getById(reply.getId());

        assertInstanceOf(ReplyMessage.class, fetchedReplyAsMessage, "Fetched object must be an instance of ReplyMessage");

        ReplyMessage fetchedReply = (ReplyMessage) fetchedReplyAsMessage;

        assertEquals("Hi! Received it loud and clear.", fetchedReply.getContent());
        assertEquals(recipient.getId(), fetchedReply.getSender().getId(), "Reply sender ID is incorrect");

        assertNotNull(fetchedReply.getQuotedMessage(), "Quoted message must not be null");
        assertEquals(message.getId(), fetchedReply.getQuotedMessage().getId(), "Quoted message ID does not match");
    }

    @Test
    void VerifyLongConversationFlow() {

        DuckRepository duckRepository = new DuckRepository();
        PersonRepository personRepository = new PersonRepository();
        Duck duck = duckRepository.getById(1);
        Person person = personRepository.getById(10);

        Message msg1 = new Message(duck, "Hey person, do you have the report?");
        msg1.addReceiver(person);
        repository.add(msg1);

        Message fetchedMsg1 = repository.getById(msg1.getId());
        assertNotNull(fetchedMsg1);
        assertEquals("Hey person, do you have the report?", fetchedMsg1.getContent());
        assertEquals(duck.getId(), fetchedMsg1.getSender().getId());
        assertEquals(1, fetchedMsg1.getRecipients().size());

        ReplyMessage reply1 = new ReplyMessage(person, "Yes, I just finished it.", fetchedMsg1);
        reply1.addReceiver(duck);
        repository.add(reply1);

        ReplyMessage fetchedReply1 = (ReplyMessage) repository.getById(reply1.getId());
        assertEquals("Yes, I just finished it.", fetchedReply1.getContent());
        assertEquals(person.getId(), fetchedReply1.getSender().getId());
        assertEquals(msg1.getId(), fetchedReply1.getQuotedMessage().getId());

        Message reply2 = new ReplyMessage(duck, "Great! Please send it over.", fetchedReply1);
        reply2.addReceiver(person);
        repository.add(reply2);

        ReplyMessage fetchedReply2 = (ReplyMessage) repository.getById(reply2.getId());

        assertEquals("Great! Please send it over.", fetchedReply2.getContent());
        assertEquals(duck.getId(), fetchedReply2.getSender().getId());
        assertEquals(reply1.getId(), fetchedReply2.getQuotedMessage().getId());

        Message reply3 = new ReplyMessage(person, "Sending it now via email.", reply2);
        reply3.addReceiver(duck);
        repository.add(reply3);

        ReplyMessage fetchedReply3 = (ReplyMessage) repository.getById(reply3.getId());

        assertEquals("Sending it now via email.", fetchedReply3.getContent());
        assertEquals(person.getId(), fetchedReply3.getSender().getId());
        assertEquals(reply2.getId(), fetchedReply3.getQuotedMessage().getId());
    }

    @Test
    void SendMessageFail() {

        PersonRepository personRepo = new PersonRepository();
        long invalidSenderId = 99999L;
        long invalidRecipientId = 88888L;

        assertThrows(Exception.class, () -> {

            Person sender = personRepo.getById(invalidSenderId);
            Message message = new Message(sender, "This message should not exist.");
            Person recipient = personRepo.getById(invalidRecipientId);
            message.addReceiver(recipient);

            repository.add(message);
        });
    }

    @Test
    void deleteMessage() {

        PersonRepository personRepo = new PersonRepository();
        Person sender = personRepo.getById(6);
        Person recipient = personRepo.getById(7);

        Message message = new Message(sender, "This message will be deleted.");
        message.addReceiver(recipient);

        repository.add(message);

        long messageId = message.getId();

        assertNotNull(repository.getById(messageId));

        repository.delete(messageId);

        assertThrows(RepositoryException.class, () -> repository.getById(messageId));

    }

    @Test
    void GetAllMessages() {

        Collection<Message> allMessages = repository.getAll();
        assertEquals(9, allMessages.size());
    }

    @Test
    void GetMessageById() {

        PersonRepository personRepo = new PersonRepository();
        Person user = personRepo.getById(9);

        Message message = new Message(user, "Specific message to find");
        repository.add(message);

        Long id = message.getId();

        Message foundMessage = repository.getById(id);

        assertNotNull(foundMessage);
        assertEquals(id, foundMessage.getId());
        assertEquals("Specific message to find", foundMessage.getContent());
    }

    @Test
    void GetMessagesByIdsList() {

        List<Long> idsToFetch = Arrays.asList(1L, 2L);

        Collection<Message> results = repository.getByIds(idsToFetch);

        assertNotNull(results);
        assertEquals(2, results.size());

    }

    @Test
    void getConversationBefore() {

        int pageSize = 2;

        List<Message> firstPage = repository.getConversationBefore(6, 7, null, pageSize);
        assertEquals(2, firstPage.size());
        assertEquals("Hi John! Yes, at 10 AM.", firstPage.get(0).getContent());
        assertEquals("I might be 5 minutes late though.", firstPage.get(1).getContent());

        LocalDateTime cursor = firstPage.get(0).getTimestamp();

        List<Message> secondPage = repository.getConversationBefore(6, 7, cursor, pageSize);

        assertEquals(1, secondPage.size());
        assertEquals("Hello Jane, are we meeting for coffee?", secondPage.getFirst().getContent());


        LocalDateTime cursor2 = secondPage.getFirst().getTimestamp();

        List<Message> thirdPage = repository.getConversationBefore(6, 7, cursor2, pageSize);

        assertTrue(thirdPage.isEmpty());
    }

}
