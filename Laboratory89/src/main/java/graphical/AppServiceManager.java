package graphical;
import repositories.DuckRepository;
import repositories.FriendshipRepository;
import repositories.MessageRepository;
import repositories.PersonRepository;
import services.DuckService;
import services.FriendshipService;
import services.MessageService;
import services.PersonService;
import validators.DuckValidator;
import validators.FriendshipValidator;
import validators.MessageValidator;
import validators.PersonValidator;

public class AppServiceManager {

    DuckService duckService;
    FriendshipService friendshipService;
    MessageService messageService;
    PersonService personService;

    public AppServiceManager() {

        duckService = new DuckService(new DuckRepository(), new DuckValidator());
        friendshipService = new FriendshipService(new FriendshipRepository(), new FriendshipValidator());
        messageService = new MessageService(new MessageRepository(), new MessageValidator());
        personService = new PersonService(new PersonRepository(), new PersonValidator());

    }

    public DuckService getDuckService() {
        return duckService;
    }

    public FriendshipService getFriendshipService() {
        return friendshipService;
    }

    public MessageService getMessageService() {
        return messageService;
    }

    public PersonService getPersonService() {
        return personService;
    }
}
