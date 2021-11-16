/*
 *  UCF COP3330 Fall 2021 Assignment 4 Solution
 *  Copyright 2021 Dave Edouard
 */

package ucf.assignments;

import java.time.LocalDate;

// Create to do object
public class Object {
    private String taskName, description, completed;
    private LocalDate dueDate;

    // Used for functions in other classes
    public Object(String taskName, LocalDate dueDate, String completed, String description) {
        this.taskName = taskName;
        this.description = description;
        this.completed = completed;
        this.dueDate = dueDate;
    }

    // Function to get task name
    public String getTaskName() {
        return taskName;
    }

    // Function that sets task name equal to itself
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    // Function that takes description input
    public String getDescription() {
        return description;
    }

    // function that sets description input to variable
    public void setDescription(String description) {
        this.description = description;
    }

    // Function that determines whether the task is completes
    public String getCompleted() {
        return completed;
    }

    // Sets current status of task to variable
    public void setCompleted(String completed) {
        this.completed = completed;
    }

    // Sets date of task
    public LocalDate getDueDate() {
        return dueDate;
    }

    // Sets new date to itself
    public void setDueDate() {
        this.dueDate = dueDate;
    }

}

