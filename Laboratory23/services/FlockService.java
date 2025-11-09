package services;
import entities.ducks.Duck;
import entities.flocks.Flock;
import entities.flocks.FlockInterest;
import entities.pairs.DoublePair;
import exceptions.validators.FlockValidationException;
import repository.FlockRepository;
import validators.FlockValidator;
import java.util.*;


public class FlockService {

    private final FlockRepository flockRepo;
    private final FlockValidator validator;

    public FlockService(FlockRepository flockRepo_init,  FlockValidator validator_init) {
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

    public void deleteFlock(long id) throws FlockValidationException {
        validator.validateId(id);
        flockRepo.delete(id);
        flockRepo.writeToFile();
    }

    public Set<Long> getMembersIds(long flockId) {

        Flock flock = flockRepo.getById(flockId);
        return new HashSet<>(flock.getMembers());
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
