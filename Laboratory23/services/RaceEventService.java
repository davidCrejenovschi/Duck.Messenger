package services;
import entities.events.RaceEventDTO;
import entities.events.RaceEvent;
import exceptions.validators.EventValidationException;
import repository.RaceEventRepository;
import validators.events.RaceEventValidator;

public class RaceEventService extends EventServiceAbstract<RaceEvent, RaceEventDTO, RaceEventValidator> {

    public RaceEventService(RaceEventRepository repository, RaceEventValidator validator) {
        super(repository, validator);
    }

    @Override
    public void addEvent(RaceEventDTO dto) throws EventValidationException {

        RaceEvent event = new RaceEvent(dto.getId(), dto.getName(), dto.getM());

        validator.validate(event);
        repository.add(event);
        repository.writeToFile();
    }

    public void write_to_file() {
        repository.writeToFile();
    }


}
