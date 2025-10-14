package tasks;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import entities.RaceContext;

public class WriteOnFileTask extends Task {

    private final RaceContext raceContext;

    public WriteOnFileTask(String taskId_init, String description_init, RaceContext race_context_init) {
        super(taskId_init, description_init);
        this.raceContext = race_context_init;
    }

    @Override
    public void execute() {
        double bestTime = raceContext.getTime();

        try (PrintWriter pw = new PrintWriter(new FileWriter(raceContext.getFileNameOut()))) {
            pw.printf("%.2f%n", bestTime);
        } catch (IOException e) {
            System.err.println("Writing error: " + e.getMessage());
        }
    }
}
