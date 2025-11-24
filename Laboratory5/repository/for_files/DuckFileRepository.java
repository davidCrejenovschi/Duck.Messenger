package repository.for_files;
import entities.users.ducks.abstracts.Duck;
import entities.users.ducks.enums.DuckType;
import entities.users.ducks.FlyingDuck;
import entities.users.ducks.SwimmingDuck;
import exceptions.repositories.ReadFromFileException;
import exceptions.repositories.WriteToFileException;
import repository.abstracts.AbstractFileRepository;
import java.io.*;


public class DuckFileRepository extends AbstractFileRepository<Duck> {

    private final String pathFile;

    public DuckFileRepository(String pathFile_init) {
        this.pathFile = pathFile_init;
        readFromFile();
    }

    @Override
    protected long getId(Duck item) {
        return item.getId();
    }

    @Override
    protected void readFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(pathFile))) {
            br.readLine();
            String line;

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                Duck d = createDuck(fields);
                items.put(d.getId(), d);
            }

        } catch (IOException e) {
            throw new ReadFromFileException("Error reading file " + pathFile, e);
        }
    }
    private DuckType parseDuckType(String value) {
        try {
            return DuckType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("Invalid value for DuckType in the file: '" + value + "'");
        }
    }
    private Duck createDuck(String[] fields) {
        DuckType duckType = parseDuckType(fields[4]);
        long id = Long.parseLong(fields[0]);
        String username = fields[1];
        String password = fields[2];
        String email = fields[3];
        double speed = Double.parseDouble(fields[5]);
        double stamina = Double.parseDouble(fields[6]);

        return switch (duckType) {
            case SWIMMER -> new SwimmingDuck(id, username, password, email, speed, stamina);
            case FLYER -> new FlyingDuck(id, username, password, email, speed, stamina);
        };
    }

    @Override
    protected void writeToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(pathFile))) {
            bw.write("id,username,password,email,duckType,speed,stamina");
            bw.newLine();

            for (Duck d : items.values()) {
                String line = String.join(",",
                        String.valueOf(d.getId()),
                        d.getUsername(),
                        d.getPassword(),
                        d.getEmail(),
                        d.getDuckType().name(),
                        String.valueOf(d.getSpeed()),
                        String.valueOf(d.getStamina())
                );
                bw.write(line);
                bw.newLine();
            }

        } catch (IOException e) {
            throw new WriteToFileException("Error writing to file", e);
        }
    }

    @Override
    public void saveChangesToExternMemory() {
        writeToFile();
    }
}


