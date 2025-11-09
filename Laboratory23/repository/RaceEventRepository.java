package repository;
import entities.events.RaceEvent;
import exceptions.repositories.RepositoryException;
import java.io.*;
import java.util.*;

public class RaceEventRepository extends EventRepository<RaceEvent> {

    private final String filePath;

    public RaceEventRepository(String filePath) {
        this.filePath = filePath;
        readFromFile();
    }

    @Override
    public void readFromFile() {
        items.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length < 3) continue;

                long eventId = Long.parseLong(tokens[0].trim());
                String eventName = tokens[1].trim();
                int M = Integer.parseInt(tokens[2].trim());

                RaceEvent event = new RaceEvent(eventId, eventName, M);

                List<Long> duckIds = new ArrayList<>();
                for (int i = 3; i < tokens.length; i++) {
                    duckIds.add(Long.parseLong(tokens[i].trim()));
                }
                event.setSubscribersIds(duckIds);

                items.put(eventId, event);
            }
        } catch (IOException e) {
            throw new RepositoryException("Error reading RaceEvents: " + e.getMessage());
        }
    }

    @Override
    public void writeToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (RaceEvent event : items.values()) {
                StringBuilder sb = new StringBuilder();
                sb.append(event.getId())
                        .append(", ").append(event.getName())
                        .append(", ").append(event.getM());

                event.getSubscribersIds().forEach(id -> sb.append(", ").append(id));

                bw.write(sb.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RepositoryException("Error writing RaceEvents: " + e.getMessage());
        }
    }
}
