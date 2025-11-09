package repository;

import entities.flocks.Flock;
import entities.flocks.FlockInterest;
import exceptions.repositories.RepositoryException;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class FlockRepository extends AbstractFileRepository<Flock> {

    private final String filePath;

    public FlockRepository(String filePath) {
        this.filePath = filePath;
        readFromFile();
    }

    @Override
    protected long getId(Flock flock) {
        return flock.getId();
    }

    @Override
    public void readFromFile() {
        items.clear();
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNo = 0;

            while ((line = br.readLine()) != null) {
                lineNo++;
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(";", 4);
                if (parts.length < 3) {
                    throw new RepositoryException("Invalid flock line at " + lineNo + ": " + line);
                }

                final long id;
                try {
                    id = Long.parseLong(parts[0].trim());
                } catch (NumberFormatException ex) {
                    throw new RepositoryException("Invalid flock id at line " + lineNo + ": " + parts[0]);
                }

                FlockInterest interest;
                try {
                    interest = FlockInterest.valueOf(parts[1].trim());
                } catch (Exception ex) {
                    interest = FlockInterest.FLYING;
                }

                String name = parts[2].trim();

                Flock flock = new Flock(id, interest, name);

                if (parts.length >= 4) {
                    String membersPart = parts[3].trim();
                    if (!membersPart.isEmpty()) {
                        String[] memberIds = membersPart.split(",");
                        for (String memberIdStr : memberIds) {
                            memberIdStr = memberIdStr.trim();
                            if (memberIdStr.isEmpty()) continue;
                            try {
                                long duckId = Long.parseLong(memberIdStr);
                                flock.addMember(duckId);
                            } catch (NumberFormatException ignored) {
                            }
                        }
                    }
                }

                items.put(id, flock);
            }

        } catch (IOException e) {
            throw new RepositoryException("Error reading flocks from file: " + e.getMessage());
        }
    }

    @Override
    public void writeToFile() {
        File file = new File(filePath);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (Flock flock : items.values()) {
                StringBuilder sb = new StringBuilder();
                sb.append(flock.getId()).append(';');
                sb.append(flock.getInterest()).append(';');
                sb.append(flock.getName() == null ? "" : flock.getName()).append(';');

                Set<Long> ids = flock.getMembers();
                if (ids != null && !ids.isEmpty()) {
                    String joined = ids.stream()
                            .map(String::valueOf)
                            .collect(Collectors.joining(","));
                    sb.append(joined);
                }

                bw.write(sb.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RepositoryException("Error writing flocks to file: " + e.getMessage());
        }
    }
}
