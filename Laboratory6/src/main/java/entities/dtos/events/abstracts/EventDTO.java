package entities.dtos.events.abstracts;

import entities.dtos.events.interfaces.EventDTOI;

public abstract class EventDTO implements EventDTOI {

    protected long id;
    protected String name;

    public EventDTO(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

}
