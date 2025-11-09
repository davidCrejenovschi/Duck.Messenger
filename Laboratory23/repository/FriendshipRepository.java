package repository;
import java.io.*;

import exceptions.repositories.ReadFromFileException;
import exceptions.repositories.WriteToFileException;
import entities.pairs.FriendshipPair;

public class FriendshipRepository extends AbstractFileRepository<FriendshipPair> {

    private final String pathFile;

    public FriendshipRepository(String pathFile_init) {
        this.pathFile = pathFile_init;
        readFromFile();
    }

    @Override
    protected long getId(FriendshipPair friendshipPair){
        return friendshipPair.getId();

    }

    @Override
    public void readFromFile() {

        items.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(pathFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",");

                if (parts.length != 2) {
                    System.err.println("Invalid line format: " + line);
                    continue;
                }

                try {
                    long id1 = Long.parseLong(parts[0].trim());
                    long id2 = Long.parseLong(parts[1].trim());

                    FriendshipPair pair = new FriendshipPair(id1, id2);
                    items.put(pair.getId(), pair);

                } catch (NumberFormatException e) {
                    System.err.println("Invalid number in line: " + line);
                }
            }

        } catch (IOException e) {
            throw new ReadFromFileException("Error reading from file: " + pathFile, e);
        }
    }

    @Override
    public void writeToFile() {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(pathFile))) {

            for (FriendshipPair pair : items.values()) {
                bw.write(pair.getLeft() + "," + pair.getRight());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new WriteToFileException("Error writing to file: " + pathFile, e);
        }
    }


}
