package entities;

public class RaceContext {

    private Duck[] ducks;
    private Lane[] lanes;
    private final String fileNameIn;
    private final String fileNameOut;
    private Double time = 0.0;
    private Duck[] chosenDucks;

    public RaceContext(String fileNameIn_init, String fileNameOut_init) {

        this.fileNameIn = fileNameIn_init;
        this.fileNameOut = fileNameOut_init;
    }

    public String getFileNameIn() {
        return fileNameIn;
    }

    public String getFileNameOut() {
        return fileNameOut;
    }

    public void setDucks(Duck[] ducks) {
        this.ducks = ducks;
    }

    public void setLanes(Lane[] lanes) {
        this.lanes = lanes;
    }

    public void setTime(Double time) {
        this.time = time;
    }

    public void setChosenDucks(Duck[] chosenDucks) {
        this.chosenDucks = chosenDucks;
        for (Duck duck : chosenDucks) {
            System.out.println(duck);
        }
    }

    public Duck[] getChosenDucks() {
        return chosenDucks;
    }

    public Duck[] getDucks() {
        return ducks;
    }

    public Lane[] getLanes() {
        return lanes;
    }

    public Double getTime() {
        return time;
    }
}

