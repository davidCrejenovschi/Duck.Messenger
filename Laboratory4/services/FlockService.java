package services;
import entities.users.ducks.abstracts.Duck;
import entities.flocks.Flock;
import entities.flocks.enums.FlockInterest;
import entities.pairs.DoublePair;
import exceptions.validators.FlockValidationException;
import repository.for_files.FlockFileRepository;
import validators.FlockValidator;
import java.util.*;


public class FlockService {

    private final FlockFileRepository flockRepo;
    private final FlockValidator validator;

    public FlockService(FlockFileRepository flockRepo_init, FlockValidator validator_init) {
        flockRepo = flockRepo_init;
        validator = validator_init;
    }

    public Collection<Flock> getAllFlocks() {
        return flockRepo.getAll();
    }

    public Flock getFlockById(long flockId) throws FlockValidationException {
        validator.validateId(flockId);
        return flockRepo.getById(flockId);
    }

    public void addFlock(long id, FlockInterest interest, String name) throws FlockValidationException {
        Flock flock = new Flock(id, interest, name);
        validator.validateFlock(flock);
        flockRepo.add(flock);
        flockRepo.writeToFile();
    }

    public Set<Long> deleteFlock(long id) throws FlockValidationException {
        validator.validateId(id);
        Flock flock = flockRepo.getById(id);
        flockRepo.delete(id);
        flockRepo.writeToFile();
        return flock.getMembers();
    }

    public Set<Long> getMembers(long flockId) {

        Flock flock = flockRepo.getById(flockId);
        return new HashSet<>(flock.getMembers());
    }

    public void addDuckToFlock(long duck_id, long flock_id){
        Flock flock = flockRepo.getById(flock_id);
        flock.addMember(duck_id);
    }

    public void removeDuckFromFlock(long duck_id, long flock_id){
        Flock flock = flockRepo.getById(flock_id);
        flock.removeMember(duck_id);
    }

    public Set<Duck> resolveMembersAsDucks(long flockId, Collection<Duck> availableDucks) throws FlockValidationException {

        validator.validateId(flockId);

        Flock flock = flockRepo.getById(flockId);

        if (availableDucks == null || availableDucks.isEmpty()) {
            return Collections.emptySet();
        }

        Map<Long, Duck> duckMap = new HashMap<>();
        for (Duck duck : availableDucks) {
            if (duck != null) {
                duckMap.put(duck.getId(), duck);
            }
        }

        Set<Duck> result = new HashSet<>();
        for (Long memberId : flock.getMembers()) {
            Duck d = duckMap.get(memberId);
            if (d != null) {
                result.add(d);
            }
        }

        return result;
    }

    public DoublePair calculateFlockPerformance(long flockId, Collection<Duck> availableDucks) throws FlockValidationException {

        Set<Duck> members = resolveMembersAsDucks(flockId, availableDucks);
        if (members.isEmpty()) return new DoublePair();
        Flock flock = flockRepo.getById(flockId);
        return flock.getFlockPerformance(members);
    }

    public void writeToFile() {
        flockRepo.writeToFile();
    }


}
