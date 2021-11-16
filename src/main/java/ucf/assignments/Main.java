/*
 *  UCF COP3330 Fall 2021 Assignment 4 Solution
 *  Copyright 2021 Dave Edouard
 */

package ucf.assignments;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


public class Main extends Application  {
    // Loads fxml file and titles it
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("ucf/assignments/toDoList.fxml"));
            toDoList controller = loader.getController();
            toDoList.setStage(primaryStage);
            Parent root = FXMLLoader.load(getClass().getResource("toDoList.fxml"));
            // Titles window
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("To Do List");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Main function
    public static void main(String[] args) {
        launch(args);
    }
}
