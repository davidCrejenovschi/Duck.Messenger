package entities.events;

public class RaceEventDTO extends EventDTO {

    private int M;

    public RaceEventDTO(long id, String name, int M) {
        super(id, name);
        this.M = M;
    }
    public int getM() {
        return M;
    }

    public void setM(int M) {
        this.M = M;
    }
}
