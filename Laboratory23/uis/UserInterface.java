package uis;
import entities.ducks.Duck;
import entities.ducks.DuckDTO;
import entities.ducks.DuckType;
import entities.events.RaceEvent;
import entities.events.RaceEventDTO;
import entities.flocks.Flock;
import entities.flocks.FlockInterest;
import entities.pairs.DoublePair;
import entities.persons.PersonDTO;
import entities.users.User;
import entities.users.UserDTO;
import exceptions.repositories.RepositoryException;
import exceptions.validators.EventValidationException;
import exceptions.validators.FlockValidationException;
import exceptions.validators.UserValidationException;
import exceptions.validators.ValidationException;
import services.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class UserInterface {

    private final DuckService duckService;
    private final PersonService personService;
    private final FriendshipService friendshipService;
    private final FlockService flockService;
    private final RaceEventService  raceEventService;
    private final Scanner scanner;

    public UserInterface(DuckService duckService,
                         PersonService personService,
                         FriendshipService friendshipService,
                         FlockService flockService,
                         RaceEventService raceEventService) {
        this.duckService = duckService;
        this.personService = personService;
        this.friendshipService = friendshipService;
        this.flockService = flockService;
        this.raceEventService = raceEventService;
        this.scanner = new Scanner(System.in);
    }

    private void principalMenuText(){
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. User Management");
        System.out.println("2. Friendship Management");
        System.out.println("3. Flock Management");
        System.out.println("4. Race Event Management");
        System.out.println("0. Exit");
        System.out.print("Your choice: ");
    }
    private void subMenuAddUserText(){
        System.out.println("Select user type:");
        System.out.println("1. Duck");
        System.out.println("2. Person");
        System.out.print("Choice: ");
    }

    public void start() {
        while (true) {
           principalMenuText();
            String mainChoice = scanner.nextLine().trim();

            switch (mainChoice) {
                case "0":
                    System.out.println("Exiting...");
                    return;

                case "1":
                    userManagementMenu();
                    break;

                case "2":
                    friendshipManagementMenu();
                    break;

                case "3":
                    flockManagementMenu();
                    break;

                case "4":
                    eventManagementMenu();
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
    private void userManagementMenu() {
        while (true) {
            System.out.println("\n--- USER MANAGEMENT ---");
            System.out.println("1. Add User");
            System.out.println("2. Delete User");
            System.out.println("0. Back to Main Menu");
            System.out.print("Your choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "0":
                    return;
                case "1":
                    addUser();
                    break;
                case "2":
                    deleteUser();
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
    private void friendshipManagementMenu() {
        while (true) {
            System.out.println("\n--- FRIENDSHIP MANAGEMENT ---");
            System.out.println("1. Add Friendship");
            System.out.println("2. Delete Friendship");
            System.out.println("3. View Communities");
            System.out.println("4. View Most Sociable Community");
            System.out.println("0. Back to Main Menu");
            System.out.print("Your choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "0":
                    return;
                case "1":
                    addFriendship();
                    break;
                case "2":
                    deleteFriendship();
                    break;
                case "3":
                    viewCommunities();
                    break;
                case "4":
                    viewMostSociableCommunity();
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
    private void flockManagementMenu() {
        while (true) {
            System.out.println("\n--- FLOCK MANAGEMENT ---");
            System.out.println("1. Add Flock");
            System.out.println("2. Delete Flock");
            System.out.println("3. Add Duck to Flock");
            System.out.println("4. Remove Duck from Flock");
            System.out.println("5. Get performance for a Flock");
            System.out.println("0. Back to Main Menu");
            System.out.print("Your choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "0":
                    return;
                case "1":
                    addFlock();
                    break;
                case "2":
                    deleteFlock();
                    break;
                case "3":
                    addDuckToFlock();
                    break;
                case "4":
                    removeDuckFromFlock();
                    break;
                case "5":
                    getPerformanceForFlock();
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
    private void eventManagementMenu() {
        while (true) {
            System.out.println("\n--- EVENT MANAGEMENT ---");
            System.out.println("1. Add Event");
            System.out.println("2. Delete Event");
            System.out.println("3. Notify Event Subscribers");
            System.out.println("4. Add user to a event.");
            System.out.println("0. Back to Main Menu");
            System.out.print("Your choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "0":
                    return;
                case "1":
                    addEventMenu();
                    break;
                case "2":
                    deleteEvent();
                    break;
                case "3":
                    notifyEventSubscribers();
                    break;
                case "4":
                    addUserToEvent();
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
    private void addEventMenu() {
        while (true) {
            System.out.println("\n--- ADD EVENT ---");
            System.out.println("1. RaceEvent");
            System.out.println("0. Back");
            System.out.print("Choose event type to add: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "0":
                    return;
                case "1":
                    addRaceEvent();
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void addRaceEvent() {
        try {
            System.out.print("Enter event id: ");
            String idLine = scanner.nextLine().trim();
            long id = Long.parseLong(idLine);

            System.out.print("Enter event name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Enter M (max participants per competition): ");
            String mLine = scanner.nextLine().trim();
            int M = Integer.parseInt(mLine);

            RaceEventDTO dto = new RaceEventDTO(id, name, M);

            try {
                raceEventService.addEvent(dto);
                System.out.println("RaceEvent added successfully.");
            } catch (exceptions.validators.EventValidationException ve) {
                System.out.println("Validation failed: " + ve.getMessage());
            } catch (exceptions.repositories.RepositoryException re) {
                System.out.println("Repository error: " + re.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }

        } catch (NumberFormatException nfe) {
            System.out.println("Invalid number format. Operation cancelled.");
        }
    }
    private void deleteEvent() {
        try {
            System.out.print("Enter event id to delete: ");
            String idLine = scanner.nextLine().trim();

            long id = Long.parseLong(idLine);
            if (id <= 0) {
                System.out.println("Id must be a positive number. Operation cancelled.");
                return;
            }

            String idString = Long.toString(id);

            if (idString.endsWith("03")) {
                try {
                    raceEventService.deleteEvent(id);
                    System.out.println("Race event deleted successfully.");
                } catch (EventValidationException ve) {
                    System.out.println("Validation failed: " + ve.getMessage());
                } catch (RepositoryException re) {
                    System.out.println("Repository error: " + re.getMessage());
                } catch (Exception e) {
                    System.out.println("Unexpected error: " + e.getMessage());
                }
            } else {
                System.out.println("This id does not belong to a RaceEvent (does not end with '03'). No action taken.");
            }

        } catch (NumberFormatException nfe) {
            System.out.println("Invalid id format. Operation cancelled.");
        }
    }
    private void notifyEventSubscribers() {
        try {

            System.out.print("Enter event id to notify subscribers: ");
            String idLine = scanner.nextLine().trim();
            long id = Long.parseLong(idLine);

            if (id <= 0) {
                System.out.println("Id must be a positive number. Operation cancelled.");
                return;
            }

            String idString = Long.toString(id);

            if (idString.endsWith("03")) {
                try {
                    raceEventService.getEventById(id).selectPotentialParticipants(duckService.getAllUsers());
                    raceEventService.notifySubscribers(id);
                } catch (RepositoryException re) {
                    System.out.println("Repository error: " + re.getMessage());
                } catch (Exception e) {
                    System.out.println("Unexpected error: " + e.getMessage());
                }
            } else {
                System.out.println("This id does not belong to a RaceEvent (does not end with '03'). No action taken.");
            }

        } catch (NumberFormatException nfe) {
            System.out.println("Invalid id format. Operation cancelled.");
        }
    }
    private void addUserToEvent() {
        try {
            System.out.print("Enter event id: ");
            String eventIdLine = scanner.nextLine().trim();
            long eventId = Long.parseLong(eventIdLine);

            System.out.print("Enter user (duck) id to subscribe: ");
            String userIdLine = scanner.nextLine().trim();
            long userId = Long.parseLong(userIdLine);

            if (eventId <= 0 || userId <= 0) {
                System.out.println("Ids must be positive numbers. Operation cancelled.");
                return;
            }

            String idString = Long.toString(eventId);

            if (idString.endsWith("03")) {
                try {

                    RaceEvent event = raceEventService.getEventById(eventId);
                    List<Long> subscribers = event.getSubscribersIds();

                    if (subscribers.contains(userId)) {
                        System.out.println("User is already subscribed to this event.");
                        return;
                    }

                    event.subscribe(userId);

                    System.out.println("User successfully added to RaceEvent.");
                    raceEventService.write_to_file();

                } catch (exceptions.validators.EventValidationException ve) {
                    System.out.println("Validation failed: " + ve.getMessage());
                } catch (exceptions.repositories.RepositoryException re) {
                    System.out.println("Repository error: " + re.getMessage());
                } catch (Exception e) {
                    System.out.println("Unexpected error: " + e.getMessage());
                }

            } else {
                System.out.println("This id does not belong to a RaceEvent (does not end with '03'). No action taken.");
            }

        } catch (NumberFormatException nfe) {
            System.out.println("Invalid number format. Operation cancelled.");
        }
    }



    private void addUser() {

        subMenuAddUserText();
        String typeChoice = scanner.nextLine();

        switch (typeChoice) {
            case "1":
                addDuck();
                break;
            case "2":
                addPerson();
                break;
            default:
                System.out.println("Invalid choice. Returning to main menu.");
        }
    }
    private UserDTO readCommonUserData() throws NumberFormatException {
        System.out.print("Enter id: ");
        long id = Long.parseLong(scanner.nextLine());
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        return new UserDTO(id, username, password, email);
    }
    private DuckType parseDuckType(String value) {
        try {
            return DuckType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("Invalid value for DuckType");
        }
    }
    private void addDuck() {

        try {
            UserDTO userDTO = readCommonUserData();
            System.out.print("Enter duck type: ");
            String duckType = scanner.nextLine();
            System.out.print("Enter speed: ");
            double speed = Double.parseDouble(scanner.nextLine());
            System.out.print("Enter stamina: ");
            double stamina = Double.parseDouble(scanner.nextLine());
            DuckDTO duckDTO = new DuckDTO(userDTO, parseDuckType(duckType), speed, stamina);

            duckService.addUser(duckDTO);
            System.out.println("Duck added successfully.");

        } catch (NumberFormatException e) {
            System.out.println("Invalid number format.");
        } catch (UserValidationException | RepositoryException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IllegalArgumentException e){
            System.out.println( e.getMessage() );
        }
    }
    private void addPerson() {

        try {
            UserDTO userDTO = readCommonUserData();
            System.out.print("Enter first name: ");
            String firstName = scanner.nextLine();
            System.out.print("Enter last name: ");
            String lastName = scanner.nextLine();
            System.out.print("Enter occupation: ");
            String occupation = scanner.nextLine();
            System.out.print("Enter empathy level: ");
            int empathy = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter birth date (yyyy-MM-dd): ");
            LocalDate birthDate = LocalDate.parse(scanner.nextLine());
            PersonDTO personDTO = new PersonDTO(userDTO, firstName, lastName, occupation, empathy, birthDate);

            personService.addUser(personDTO);
            System.out.println("Person added successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format.");
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format.");
        } catch (UserValidationException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deleteUser() {

        try {
            System.out.print("Enter id: ");
            long id = Long.parseLong(scanner.nextLine());
            String idStr = Long.toString(id);

            if (idStr.endsWith("00")) {
                duckService.deleteUser(id);
                System.out.println("Duck deleted successfully.");
            } else if (idStr.endsWith("01")) {
                personService.deleteUser(id);
                System.out.println("Person deleted successfully.");
            } else {
                System.out.println("Invalid id format. Cannot determine user type.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format.");
        } catch (ValidationException | RepositoryException e) {
            System.out.println("Error : " + e.getMessage());
        }
    }

    private void addFriendship() {
        try {

            System.out.print("Enter first user id: ");
            long leftId = Long.parseLong(scanner.nextLine());

            System.out.print("Enter second user id: ");
            long rightId = Long.parseLong(scanner.nextLine());

            User left = duckService.getUser(leftId);
            User right = duckService.getUser(rightId);

            friendshipService.addFriendship(leftId, rightId);
            System.out.println("Friendship added successfully.");

            left.addFriend(rightId);
            right.addFriend(leftId);


        } catch (NumberFormatException e) {
            System.out.println("Invalid number format.");
        } catch (ValidationException | RepositoryException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deleteFriendship() {
        try {
            System.out.print("Enter friendship id (format: <id1>#<id2>): ");
            String input = scanner.nextLine().trim();

            long[] ids = splitCompositeIdByHash(input); // ids[0] = firstId, ids[1] = secondId
            long firstId = ids[0];
            long secondId = ids[1];
            String compositeStr = firstId + "0" + secondId;
            long combinedId = Long.parseLong(compositeStr);
            friendshipService.deleteFriendship(combinedId);

            User left = duckService.getUser(firstId);
            left.removeFriend(secondId);
            User right = duckService.getUser(secondId);
            right.removeFriend(firstId);


            System.out.println("Friendship deleted successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid friendship id format: " + e.getMessage());
        } catch (ValidationException | RepositoryException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private long[] splitCompositeIdByHash(String composite) {
        if (composite == null || composite.isEmpty()) {
            throw new IllegalArgumentException("Empty input");
        }

        int sepIndex = composite.indexOf('#');
        if (sepIndex <= 0 || sepIndex == composite.length() - 1) {
            throw new IllegalArgumentException("Expected format: <id1>#<id2> (both sides must be non-empty)");
        }

        String left = composite.substring(0, sepIndex).trim();
        String right = composite.substring(sepIndex + 1).trim();

        if (left.isEmpty() || right.isEmpty()) {
            throw new IllegalArgumentException("Both id parts must contain digits");
        }

        long id1 = Long.parseLong(left);
        long id2 = Long.parseLong(right);

        return new long[]{id1, id2};
    }


    private void viewCommunities() {
        System.out.println("Number of communities: " + friendshipService.viewCommunities(friendshipService.getAllFriendships()));
    }

    private void viewMostSociableCommunity() {

        Set<Long> mostSociable = friendshipService.viewMostSociableCommunity(friendshipService.getAllFriendships());

        if (mostSociable != null) {
            System.out.print("The most developed community is formed by users with ids: ");
            for (Long id : mostSociable) {
                System.out.print(id + " ");
            }
            System.out.println();
        } else {
            System.out.println("No communities found.");
        }
    }

    private void addDuckToFlock() {
        try {
            System.out.print("Enter duck id: ");
            long duckId = Long.parseLong(scanner.nextLine().trim());

            System.out.print("Enter flock id: ");
            long flockId = Long.parseLong(scanner.nextLine().trim());

            Duck duck = duckService.getUser(duckId);
            Flock flock = flockService.getFlockById(flockId);

            Flock currentFlockId = duck.getFlock();

            if (currentFlockId != null ) {
                System.out.println("Error: Duck " + duckId + " is already in flock " + currentFlockId + ". Cannot add to another flock.");
                return;
            }

            duck.setFlock(flock);
            flock.addMember(duck.getId());
            flockService.writeToFile();

        } catch (NumberFormatException nfe) {
            System.out.println("Invalid number format. Please enter numeric ids.");
        } catch (RepositoryException re) {
            System.out.println("Repository error: " + re.getMessage());
        } catch (Exception ex) {
            System.out.println("Unexpected error: " + ex.getMessage());
        }
    }
    private void removeDuckFromFlock() {
        try {
            System.out.print("Enter duck id: ");
            long duckId = Long.parseLong(scanner.nextLine().trim());

            Duck duck = duckService.getUser(duckId);
            Flock currentFlock = duck.getFlock();

            if (currentFlock == null) {
                System.out.println("Duck " + duckId + " is not in any flock.");
                return;
            }

            currentFlock.removeMember(duckId);
            duck.setFlock(null);

            System.out.println("Duck " + duckId + " was removed from flock " + currentFlock.getName() + ".");

        } catch (NumberFormatException nfe) {
            System.out.println("Invalid number format. Please enter numeric ids.");
        } catch (RepositoryException re) {
            System.out.println("Repository error: " + re.getMessage());
        } catch (Exception ex) {
            System.out.println("Unexpected error: " + ex.getMessage());
        }
    }
    private void addFlock() {
        try {
            System.out.print("Enter flock id: ");
            long flockId = Long.parseLong(scanner.nextLine().trim());

            System.out.print("Enter flock name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Enter flock interest (FLYING / SWIMMING): ");
            String interestInput = scanner.nextLine().trim().toUpperCase();

            FlockInterest interest;
            try {
                interest = FlockInterest.valueOf(interestInput);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid interest type. Must be FLYING or SWIMMING.");
                return;
            }

            flockService.addFlock(flockId, interest, name);

            System.out.println("Flock '" + name + "' added successfully.");

        } catch (NumberFormatException nfe) {
            System.out.println("Invalid number format for flock id.");
        } catch (RepositoryException re) {
            System.out.println("Repository error: " + re.getMessage());
        } catch (Exception ex) {
            System.out.println("Unexpected error: " + ex.getMessage());
        }
    }
    private void deleteFlock() {
        try {
            System.out.print("Enter flock id to delete: ");
            long flockId = Long.parseLong(scanner.nextLine().trim());

            Flock flock = flockService.getFlockById(flockId);

            for (Long duckId : flock.getMembers()) {
                try {
                    Duck duck = duckService.getUser(duckId);
                    duck.setFlock(null);
                } catch (RepositoryException ignored) {
                }
            }

            flockService.deleteFlock(flockId);

            System.out.println("Flock " + flockId + " (" + flock.getName() + ") deleted successfully.");

        } catch (NumberFormatException nfe) {
            System.out.println("Invalid number format. Please enter numeric id.");
        } catch (RepositoryException re) {
            System.out.println("Repository error: " + re.getMessage());
        } catch (Exception ex) {
            System.out.println("Unexpected error: " + ex.getMessage());
        }
    }
    private void getPerformanceForFlock() {

        try {
            System.out.print("Enter flock id: ");
            long flockId = Long.parseLong(scanner.nextLine().trim());
            DoublePair performance = flockService.calculateFlockPerformance(flockId, duckService.getAllUsers());
            System.out.print("Flock performance is : " + performance.left.toString() +"[SPEED]" + performance.right.toString()+"[STAMINA]");
        }catch (RepositoryException re){
            System.out.println("Repository error: " + re.getMessage());
        } catch (FlockValidationException e){
            System.out.println("Flock validation error: " + e.getMessage());
        }
    }






}
