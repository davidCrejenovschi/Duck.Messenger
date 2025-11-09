import repository.*;
import services.*;
import uis.UserInterface;
import validators.*;
import validators.events.RaceEventValidator;
import validators.users.DuckValidator;
import validators.users.PersonValidator;

void main() {

    PersonRepository personRepository = new PersonRepository("D:\\Lab 3\\src\\files\\persons.txt");
    DuckRepository duckRepository = new DuckRepository("D:\\Lab 3\\src\\files\\ducks.txt");
    FriendshipRepository friendshipRepository = new FriendshipRepository("D:\\Lab 3\\src\\files\\friendships.txt");
    FlockRepository flockRepository = new FlockRepository("D:\\Lab 3\\src\\files\\flocks.txt");
    RaceEventRepository raceEventRepository = new RaceEventRepository("D:\\Lab 3\\src\\files\\raceEvents");

    PersonValidator personValidator = new PersonValidator();
    DuckValidator duckValidator = new DuckValidator();
    FriendshipValidator friendshipValidator = new FriendshipValidator();
    FlockValidator flockValidator = new FlockValidator();
    RaceEventValidator raceEventValidator = new RaceEventValidator();

    PersonService personService = new PersonService(personRepository, personValidator);
    DuckService duckService = new DuckService(duckRepository, duckValidator);
    FriendshipService friendshipService = new FriendshipService(friendshipRepository, friendshipValidator);
    FlockService flockService = new FlockService(flockRepository, flockValidator);
    RaceEventService raceEventService = new RaceEventService(raceEventRepository, raceEventValidator);

    UserInterface userInterface = new UserInterface(duckService, personService, friendshipService, flockService,  raceEventService);
    userInterface.start();
}

