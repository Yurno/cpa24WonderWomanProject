package com.example.project;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Task {
    private final StringProperty id;
    private final StringProperty description;
    private final IntegerProperty priority;
    private final StringProperty assignedPerson;
    private final StringProperty dueDate;
    private boolean completed;

    public Task(String id, String description, int priority, String assignedPerson, String dueDate) {
        this.id = new SimpleStringProperty(id);
        this.description = new SimpleStringProperty(description);
        this.priority = new SimpleIntegerProperty(priority);
        this.assignedPerson = new SimpleStringProperty(assignedPerson);
        this.dueDate = new SimpleStringProperty(dueDate);
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isCompleted() {
        return completed;
    }

    public StringProperty dueDateProperty() {
        return dueDate;
    }

    public String getDueDate() {
        return dueDate.get();
    }

    public void setDueDate(String dueDate) {
        this.dueDate.set(dueDate);
    }

    // ID property
    public StringProperty idProperty() {
        return id;
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    // Description property
    public StringProperty descriptionProperty() {
        return description;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    // Priority property
    public IntegerProperty priorityProperty() {
        return priority;
    }

    public int getPriority() {
        return priority.get();
    }

    public void setPriority(int priority) {
        this.priority.set(priority);
    }

    // Assigned Person property
    public StringProperty assignedPersonProperty() {
        return assignedPerson;
    }

    public String getAssignedPerson() {
        return assignedPerson.get();
    }

    public void setAssignedPerson(String assignedPerson) {
        this.assignedPerson.set(assignedPerson);
    }

    @Override
    public String toString()
    {
        return "Task{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                ", assignedPerson='" + assignedPerson + '\'' +
                '}';
    }
}
