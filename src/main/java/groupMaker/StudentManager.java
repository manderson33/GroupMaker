package groupMaker;

/**
 * @author marilounanderson on 10/10/2024
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StudentManager {

    private final List<String> studentNames = new ArrayList<>();

    public StudentManager(String csvFilePath) {
        loadStudentsFromCSV(csvFilePath);
    }

    private void loadStudentsFromCSV(String csvFilePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                studentNames.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getStudentNames() {
        return studentNames;
    }
}