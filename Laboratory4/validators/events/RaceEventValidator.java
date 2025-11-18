package validators.events;
import entities.events.RaceEvent;
import exceptions.validators.EventValidationException;
import exceptions.validators.RaceEventValidationException;
import validators.events.abstracts.EventValidatorAbstract;

import java.util.ArrayList;
import java.util.List;

public class RaceEventValidator extends EventValidatorAbstract<RaceEvent> {

    @Override
    public void validate(RaceEvent event) throws RaceEventValidationException {
        List<String> errors = new ArrayList<>();

        try {
            validateId(event.getId());
        } catch (RaceEventValidationException ex) {
            errors.add(ex.getMessage());
        }

        try {
            validateName(event.getName());
        } catch (EventValidationException ex) {
            errors.add(ex.getMessage());
        }

        try {
            validateM(event.getM());
        } catch (RaceEventValidationException ex) {
            errors.add(ex.getMessage());
        }

        if (!errors.isEmpty()) {
            throw new RaceEventValidationException(String.join(" ", errors));
        }
    }

    @Override
    public void validateId(long id) throws RaceEventValidationException {

        if (id <= 0) {
            throw new RaceEventValidationException("RaceEvent ID must be a positive number.");
        }
        String idString = Long.toString(id);
        if (!idString.endsWith("03")) {
            throw new RaceEventValidationException("RaceEvent ID must end with '03' (examples: 103, 203, 1303).");
        }
    }

    public void validateM(int M) throws RaceEventValidationException {
        if (M <= 2) {
            throw new RaceEventValidationException("RaceEvent M (number of participants) must be greater than 2.");
        }
    }
}
