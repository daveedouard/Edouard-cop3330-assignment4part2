/*
 *  UCF COP3330 Fall 2021 Assignment 4 Solution
 *  Copyright 2021 Dave Edouard
 */

package ucf.assignments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.DefaultStringConverter;
import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.Scanner;

public class toDoList implements Initializable {
    // Variables from scenebuilder being initialized in program
    @FXML private TextField taskText;
    @FXML private TextField descriptionText;
    @FXML private DatePicker datePicker;
    @FXML private TableView<Object> Table;
    @FXML private TableColumn<Object, String> taskName;
    @FXML private TableColumn<Object, LocalDate> dueDate;
    @FXML private TableColumn<Object, String> completedYesNo;
    @FXML private TableColumn<Object, String> descriptionColumn;
    @FXML private RadioButton existing;
    @FXML private RadioButton complete;
    @FXML private RadioButton incomplete;

    private static Stage stage;
    private ObservableList<Object> tasks = FXCollections.observableArrayList();

    public static void setStage(Stage primaryStage) {
        stage = primaryStage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Sets values from inputs to correct variables
        taskName.setCellValueFactory(new PropertyValueFactory<Object, String>("taskName"));
        dueDate.setCellValueFactory(new PropertyValueFactory<Object, LocalDate>("dueDate"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<Object, String>("description"));
        completedYesNo.setCellValueFactory(new PropertyValueFactory<Object, String>("completed"));

        // Allows table to gain and lose rows
        Table.setEditable(true);
        taskName.setCellFactory(TextFieldTableCell.forTableColumn());
        descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        ObservableList<String> selections = FXCollections.observableArrayList("Yes", "No");
        completedYesNo.setCellFactory(ComboBoxTableCell.forTableColumn(new DefaultStringConverter(), selections));

        // Makes each column be identified as its own group
        ToggleGroup group = new ToggleGroup();
        existing.setToggleGroup(group);
        existing.setSelected(true);
        complete.setToggleGroup(group);
        incomplete.setToggleGroup(group);
    }

    @FXML
    public void addButton(javafx.event.ActionEvent actionEvent) {
        // Check if there are input errors
        if (taskText.getText().isEmpty() || datePicker.getValue() == null || descriptionText.getText().isEmpty()) {
            errorMessge("Error", "Fill in all inputs!");
            return;
        }

        // Create task row with user inputs
        Object newTask = new Object(
                taskText.getText(),
                datePicker.getValue(),
                "No",
                descriptionText.getText());

        // Maxes out description characters
        newTask.setDescription(newTask.getDescription().substring(0, Math.min(newTask.getDescription().length(), 256)));

        // Clears inputs for future actions
        taskText.clear();
        datePicker.getEditor().clear();
        descriptionText.clear();


        // Add task to the table
        tasks.add(newTask);
        Table.setItems(tasks);
    }

    @FXML
    public void removeButton(ActionEvent actionEvent) {
        // Get list of objects in table
        ObservableList<Object> list = Table.getItems();

        // Get selected row
        ObservableList<Object> selectedToDo = Table.getSelectionModel().getSelectedItems();

        // Remove selected item(s)
        for (Object item : selectedToDo) {
            list.remove(item);
            if (complete.isSelected() || incomplete.isSelected()) {
                tasks.remove(item);
            }
        }
    }

    // Displays all tasks
    @FXML
    public void existingButtonClicked(ActionEvent actionEvent) {
        Table.setItems(tasks);
    }

    // Filters for completed tasks only
    @FXML
    public void completeButtonClicked(ActionEvent actionEvent) {
        ObservableList<Object> list = FXCollections.observableArrayList();
        // adds completed task tot able
        for (Object todo : tasks) {
            if (todo.getCompleted().equals("Yes")) {
                list.add(todo);
            }
        }
        Table.setItems(list);
    }

    // Filters for incompleted tasks only
    @FXML
    public void incompleteButtonClicked(ActionEvent actionEvent) {
        ObservableList<Object> list = FXCollections.observableArrayList();
        // adds incomplete tasks to list
        for (Object todo : tasks) {
            if (todo.getCompleted().equals("No")) {
                list.add(todo);
            }
        }
        Table.setItems(list);
    }

    @FXML
    public void saveButton(ActionEvent actionEvent) throws IOException {
        // Pick a file directory
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fileChooser.setInitialFileName("ToDoList");
        File file = fileChooser.showSaveDialog(stage);

        // Create a text file with users name of choice
        FileWriter fw = new FileWriter(file);
        BufferedWriter  bw = new BufferedWriter(fw);

        // Loop through list to add text to file
        for (int i = 0; i < tasks.size(); i++) {
            Object tasks = this.tasks.get(i);
            // Add contents to text file
            bw.write(tasks.getTaskName());
            bw.newLine();
            bw.write(tasks.getDueDate().toString());
            bw.newLine();
            bw.write(tasks.getCompleted());
            bw.newLine();
            bw.write(tasks.getDescription());
            if (i != this.tasks.size() - 1)
                bw.newLine();
        }
        bw.close();
    }

    @FXML
    public void loadButton(ActionEvent actionEvent) throws FileNotFoundException {
        // Open file explorer
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showOpenDialog(stage);

        // Create new array list
        ObservableList<Object> ol = FXCollections.observableArrayList();

        // Confirms and opens correct file type
        Scanner scan = new Scanner(file);
        while (scan.hasNextLine()) {
            try {
                String taskName = scan.nextLine();
                if (taskName.length() == 0) break;
                String dueDateString = scan.nextLine();
                LocalDate dueDate = LocalDate.parse(dueDateString);
                String completed = scan.nextLine();
                String description = scan.nextLine();
                ol.add(new Object(taskName, dueDate, completed, description));
            } catch (Exception e) {
                // Returns error if file type is incorrect
                System.out.println(e);
                errorMessge("Open Error", "Incorrect File Type/Format");
                return;
            }
        }

        Table.setItems(ol);
    }

    // Edit task name column
    public void editTaskName(TableColumn.CellEditEvent editedCell) {
        Object todo = Table.getSelectionModel().getSelectedItem();
        todo.setTaskName(editedCell.getNewValue().toString());
    }

    // Edits whether task was completed or not
    public void editCompleted(TableColumn.CellEditEvent editedCell) {
        Object todo = Table.getSelectionModel().getSelectedItem();
        todo.setCompleted(editedCell.getNewValue().toString());
    }

    public void editDueDate(TableColumn.CellEditEvent editedCell){
        Object todo = Table.getSelectionModel().getSelectedItem();
    }

    // Edits task description
    public void editDescription(TableColumn.CellEditEvent editedCell) {
        Object todo = Table.getSelectionModel().getSelectedItem();
        todo.setDescription(editedCell.getNewValue().toString().substring(0, Math.min(editedCell.getNewValue().toString().length(), 256)));
    }

    // Alert window message
    private void errorMessge(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}