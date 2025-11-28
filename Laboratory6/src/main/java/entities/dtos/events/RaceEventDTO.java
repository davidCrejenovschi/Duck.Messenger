package entities.dtos.events;

import entities.dtos.events.abstracts.EventDTO;

public class RaceEventDTO extends EventDTO {

    private final int M;

    public RaceEventDTO(long id, String name, int M) {
        super(id, name);
        this.M = M;
    }
    public int getM() {
        return M;
    }

}
