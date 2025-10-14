package tasks;
import entities.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadFromFileTask extends Task {

    RaceContext raceContext;

    public ReadFromFileTask(String taskId_init, String description_init, RaceContext race_context_init) {

        super(taskId_init, description_init);
        raceContext = race_context_init;
    }

    @Override
    public void execute() {

        String fileName = raceContext.getFileNameIn();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            String header = readNonEmptyLine(br);
            String[] data = header.split("\\s+");
            int N = Integer.parseInt(data[0]);
            int M = Integer.parseInt(data[1]);

            if(N<0 || N>3000) {
                throw new IllegalArgumentException("N should be between 0 and 3000");
            }
            if(M<0 || M>3000) {
                throw new IllegalArgumentException("M should be between 0 and 3000");
            }

            String speedsLine = readNonEmptyLine(br);
            String resistLine = readNonEmptyLine(br);
            String buoysLine = readNonEmptyLine(br);

            String[] speeds = speedsLine.trim().split("\\s+");
            String[] resists = resistLine.trim().split("\\s+");
            String[] buoys = buoysLine.trim().split("\\s+");

            if (speeds.length < N || resists.length < N) {
                throw new IOException("Not enough duck values in input");
            }
            if (buoys.length < M) {
                throw new IOException("Not enough buoy values in input");
            }

            Duck[] ducks = new Duck[N];

            for (int i = 0; i < N; i++) {

                int speed = Integer.parseInt(speeds[i]);
                int resist = Integer.parseInt(resists[i]);
                ducks[i] = new Duck( i+1, speed, resist);
            }
            raceContext.setDucks(ducks);

            Lane[] lanes = new Lane[M];
            for (int i = 0; i < M; i++) {
                Lane lane = new Lane(i+1, Integer.parseInt(buoys[i]));
                lanes[i] = lane;
            }

            raceContext.setLanes(lanes);


        } catch (IOException | IllegalArgumentException e) {

            System.err.println("Error reading file " + raceContext.getFileNameIn() + ": " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    private String readNonEmptyLine(BufferedReader br) throws IOException {
        // the line we will return
        String line;
        // keep reading lines until there are no more
        while ((line = br.readLine()) != null) {
            // remove spaces and tabs from the start and end of the line
            line = line.trim();
            // if after trimming the line is not empty, return it
            if (!line.isEmpty()) return line;
            // otherwise the line was empty or only whitespace, continue to the next line
        }
        // we reached the end of file without finding a non-empty line, throw an exception
        throw new IOException("Unexpected end of file while searching for a non-empty line");
    }


}