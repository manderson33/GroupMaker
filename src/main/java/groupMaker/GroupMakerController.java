package groupMaker;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GroupMakerController {

    @FXML
    private ListView<String> studentListView;
    @FXML
    private VBox table1, table2, table3, table4, table5;
    @FXML
    private TextField groupCountField;

    private final StudentManager studentManager = new StudentManager("/Users/marilounanderson/IdeaProjects/JavaFX_Projects/src/main/java/groupMaker/students.csv");

    private final List<VBox> tables;

    public GroupMakerController() {
        tables = new ArrayList<>();
    }

    @FXML
    public void initialize() {
        studentListView.getItems().addAll(studentManager.getStudentNames());

        tables.add(table1);
        tables.add(table2);
        tables.add(table3);
        tables.add(table4);
        tables.add(table5);

        setupListDragAndDrop();
        setupTableDragAndDrop(table1);
        setupTableDragAndDrop(table2);
        setupTableDragAndDrop(table3);
        setupTableDragAndDrop(table4);
        setupTableDragAndDrop(table5);
    }

    private void setupListDragAndDrop() {
        studentListView.setOnDragDetected(event -> {
            Dragboard db = studentListView.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(studentListView.getSelectionModel().getSelectedItem());
            db.setContent(content);
            event.consume();
        });
    }

    private void setupTableDragAndDrop(VBox table) {
        table.setOnDragOver(event -> {
            if (event.getGestureSource() != table && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        table.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                Label label = new Label(db.getString());
                setupLabelActions(label, table);
                table.getChildren().add(label);
                studentListView.getItems().remove(db.getString());
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    private void setupLabelActions(Label label, VBox table) {
        // Handle right-click to delete and return to the list
        label.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                studentListView.getItems().add(label.getText());
                table.getChildren().remove(label);
            }
        });
    }

    @FXML
    private void randomlyGroupStudents() {
        String groupCountText = groupCountField.getText();
        int groupCount;
        try {
            groupCount = Integer.parseInt(groupCountText);
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid number of groups.");
            return;
        }

        if (groupCount <= 0 || groupCount > tables.size()) {
            showAlert("Invalid Group Count", "Please enter a number between 1 and " + tables.size());
            return;
        }

        // Reset tables and add all students to a temp list
        List<String> tempList = new ArrayList<>(studentListView.getItems());
        studentListView.getItems().clear();
        for (VBox table : tables) {
            table.getChildren().clear();
        }

        // Shuffle and distribute students across the specified number of tables
        Collections.shuffle(tempList);
        int tableIndex = 0;
        for (String student : tempList) {
            Label label = new Label(student);
            setupLabelActions(label, tables.get(tableIndex));
            tables.get(tableIndex).getChildren().add(label);
            tableIndex = (tableIndex + 1) % groupCount;
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}