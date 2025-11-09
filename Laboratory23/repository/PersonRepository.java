package repository;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import entities.persons.Person;
import exceptions.repositories.ReadFromFileException;
import exceptions.repositories.WriteToFileException;

public class PersonRepository extends AbstractFileRepository<Person> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final String pathFile;

    public PersonRepository(String pathFile_init) {
        this.pathFile = pathFile_init;
        readFromFile();
    }

    @Override
    protected long getId(Person item) {
        return item.getId();
    }

    @Override
    public void readFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(pathFile))) {
            String line;
            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");

                Person p = new Person(
                        Long.parseLong(fields[0]),
                        fields[1],
                        fields[2],
                        fields[3],
                        fields[4],
                        fields[5],
                        fields[6],
                        Integer.parseInt(fields[7]),
                        LocalDate.parse(fields[8], formatter)
                );

                items.put(p.getId(), p);
            }

        } catch (IOException e) {
            throw new ReadFromFileException("Error writing file " + pathFile, e);
        }
    }

    @Override
    public void writeToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(pathFile))) {

            bw.write("id,username,password,email,first_name,last_name,occupation,empathyLevel,birth_date");
            bw.newLine();

            for (Person p : items.values()) {
                String line = String.join(",",
                        String.valueOf(p.getId()),
                        p.getUsername(),
                        p.getPassword(),
                        p.getEmail(),
                        p.getFirst_name(),
                        p.getLast_name(),
                        p.getOccupation(),
                        String.valueOf(p.getEmpathyLevel()),
                        p.getBirth_date().format(formatter)
                );
                bw.write(line);
                bw.newLine();
            }

        }catch (IOException e) {
            throw new WriteToFileException("Error writing to file", e);
        }
    }
}
