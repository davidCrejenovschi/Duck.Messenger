package services.events;
import entities.dtos.events.RaceEventDTO;
import entities.events.RaceEvent;
import exceptions.validators.EventValidationException;
import repository.for_files.RaceEventFileRepository;
import services.events.abstracts.EventServiceAbstract;
import validators.events.RaceEventValidator;

public class RaceEventService extends EventServiceAbstract<RaceEvent, RaceEventDTO, RaceEventValidator> {

    public RaceEventService(RaceEventFileRepository repository, RaceEventValidator raceEventValidator) {
        super(repository, raceEventValidator);
    }

    @Override
    public void addEvent(RaceEventDTO dto) throws EventValidationException {

        RaceEvent event = new RaceEvent(dto.getId(), dto.getName(), dto.getM());

        validatorEvent.validate(event);
        repository.add(event);
        repository.writeToFile();
    }

}
