package com.example.project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.util.Comparator;
import javafx.collections.FXCollections;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class TaskManagerController {

    @FXML
    private Button sortButton;
    private String username;
    @FXML
    private Button logout;
    @FXML
    private Button addTask;
    @FXML
    private Button finishTask;
//    @FXML
//    private TableView inProgressTasks;
//    @FXML
//    private TableView finishedTasks;
    @FXML
    private Button editTasks;
    @FXML
    private Button finishTopTask;

    @FXML
    private TextField taskNameField;
    @FXML
    private TextField dueDateField;
    @FXML
    private TextField priorityField;
    @FXML
    private TextField assignedPersonField;

    private PriorityQueue<Task> inProgressTasks = new PriorityQueue<>(Comparator.comparingInt(Task::getPriority));
    private Stack<Task> completedTasks = new Stack<>();
    private PriorityQueue<Task> priorityTasks = new PriorityQueue<>(Comparator.comparingInt(Task::getPriority));
    private Map<String, Task> allTasks = new HashMap<>();

    private ObservableList<Task> inProgressTasksObservable = FXCollections.observableArrayList();
    private ObservableList<Task> completedTasksObservable = FXCollections.observableArrayList();
    private ObservableList<Task> priorityTasksObservable = FXCollections.observableArrayList();
    private ObservableList<Map.Entry<String, Task>> allTasksObservable = FXCollections.observableArrayList();

    @FXML
    private TableView<Task> inProgressTasksTable;
    @FXML
    private TableColumn<Task, String> inProgressIdColumn;
    @FXML
    private TableColumn<Task, String> inProgressDescriptionColumn;
    @FXML
    private TableColumn<Task, Integer> inProgressPriorityColumn;
    @FXML
    private TableColumn<Task, String> inProgressAssignedPersonColumn;
    @FXML
    private TableColumn<Task, String> inProgressDueDateColumn;
    @FXML
    private TableView<Task> completedTasksTable;
    @FXML
    private TableColumn<Task, String> completedIdColumn;
    @FXML
    private TableColumn<Task, String> completedDescriptionColumn;
    @FXML
    private TableColumn<Task, Integer> completedPriorityColumn;
    @FXML
    private TableColumn<Task, String> completedDueDateColumn;
    @FXML
    private TableColumn<Task, String> completedAssignedPersonColumn;
    public void setUsername(String username) {
        this.username = username;
    }

    @FXML
    public void initialize() {

        // TODO
        // DEBUGGER REMEMBER TO REMOVE IN REAL RUN
        // inProgressTasksObservable.add(new Task("1", "Test Task", 1, "Test Person", "tomorrow"));

        inProgressDueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        inProgressIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        inProgressDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        inProgressPriorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));
        inProgressAssignedPersonColumn.setCellValueFactory(new PropertyValueFactory<>("assignedPerson"));

        inProgressTasksTable.setItems(inProgressTasksObservable);

        completedIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        completedDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        completedPriorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));
        completedDueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        completedAssignedPersonColumn.setCellValueFactory(new PropertyValueFactory<>("assignedPerson"));

        completedTasksTable.setItems(completedTasksObservable);

    }

    // will handle the logout button
    @FXML
    private void handleLogout()
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login_screen.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) logout.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO add methods for task operations such as: add, completed tasks, prioritize tasks

    // handles the add task button
    @FXML
    private void handleAddTaskButtonAction() {
        try {
            String id = UUID.randomUUID().toString();
            String name = taskNameField.getText();
            String dueDate = dueDateField.getText(); // might have to parse date
            int priority = Integer.parseInt(priorityField.getText());
            String assignedPerson = assignedPersonField.getText();

            Task newTask = new Task(id, name, priority, assignedPerson, dueDate);
            addTask(newTask, true);

            taskNameField.clear();
            dueDateField.clear();
            priorityField.clear();
            assignedPersonField.clear();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleFinishTaskButtonAction() {
        Task selectedTask = inProgressTasksTable.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            completeTask(selectedTask);
        } else {

        }
    }


    // will deal witht he edit function button
    @FXML
    private void handleEditTaskButtonAction() {
        Task selectedTask = inProgressTasksTable.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            try {
                String newDescription = taskNameField.getText();
                String newDueDate = dueDateField.getText();
                int newPriority = Integer.parseInt(priorityField.getText());
                String newAssignedPerson = assignedPersonField.getText();

                // updates the task properties
                selectedTask.descriptionProperty().set(newDescription);
                selectedTask.dueDateProperty().set(newDueDate);
                selectedTask.priorityProperty().set(newPriority);
                selectedTask.assignedPersonProperty().set(newAssignedPerson);

                // updates the task in all relevant lists
                updateTaskInAllLists(selectedTask);

                saveAllTasksToFile();

                taskNameField.clear();
                dueDateField.clear();
                priorityField.clear();
                assignedPersonField.clear();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } else {
        }
    }

    // updates the task in the lists
    private void updateTaskInAllLists(Task task) {

        allTasks.put(task.getId(), task);

        if (task.getPriority() > 0) {
            priorityTasks.remove(task);
            priorityTasks.add(task);
            priorityTasksObservable.set(priorityTasksObservable.indexOf(task), task);
        }

        inProgressTasks.remove(task);
        inProgressTasks.add(task);
        inProgressTasksObservable.set(inProgressTasksObservable.indexOf(task), task);

        inProgressTasksTable.refresh();
    }

    // will add a task to the stack or in progress list
    public void addTask(Task task, boolean writeToDisk) {
        inProgressTasks.add(task); // adds all tasks to this list
        allTasks.put(task.getId(), task);
        updateTableViewFromQueue();

        if (task.getPriority() > 0) {
            priorityTasks.add(task);
            priorityTasksObservable.add(task);
        } else {
            inProgressTasks.add(task);
        }

        Map.Entry<String, Task> entry = new AbstractMap.SimpleEntry<>(task.getId(), task);
        allTasksObservable.add(entry);

        if (writeToDisk) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.username + ".txt", true))) {
                String taskData = convertTaskToString(task);
                writer.newLine();
                writer.write(taskData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // to update the tableview when queue is updated
    private void updateTableViewFromQueue() {
        List<Task> taskList = new ArrayList<>(inProgressTasks);
        ObservableList<Task> observableTaskList = FXCollections.observableArrayList(taskList);

        inProgressTasksTable.setItems(observableTaskList);
    }


    // will convert task to string for the txt file
    private String convertTaskToString(Task task) {
        return task.getId() + "," + task.getDescription() + "," + task.getPriority() + "," + task.getAssignedPerson() + "," + task.getDueDate();
    }

    // method to complete a task
    public void completeTask(Task task) {
        // will remove the task from the in progress tableview
        inProgressTasks.remove(task);
        inProgressTasksObservable.remove(task);
        updateTableViewFromQueue();

        // will add the task to the completed tableview
        completedTasks.push(task);
        completedTasksObservable.add(0, task);

        task.setCompleted(true);

        // remove from prio queue
        if (task.getPriority() > 0) {
            priorityTasks.remove(task);
            priorityTasksObservable.remove(task);
        }
        saveAllTasksToFile();
    }

    // will save changes to file
    private void saveAllTasksToFile() {
        File file = new File(this.username + ".txt");
        String firstLine = ""; // to store the first line/password

        // read and store the first line
        if (file.exists()) {
            try (Scanner scanner = new Scanner(file)) {
                if (scanner.hasNextLine()) {
                    firstLine = scanner.nextLine(); // Read the password
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // rewrites the file with the updated tableview
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(firstLine);
            writer.newLine();

            for (Task task : allTasks.values()) {
                String taskData = convertTaskToString(task);
                if (completedTasks.contains(task)) {
                    taskData += "~"; // will append '~' for completed tasks
                }
                writer.write(taskData);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // method to assign priority to a task
    public void assignPriorityToTask(String taskId, int priority) {
        Task task = allTasks.get(taskId);
        if (task != null) {
            task.setPriority(priority);

            inProgressTasks.remove(task);
            inProgressTasksObservable.remove(task);

            priorityTasks.add(task);
            priorityTasksObservable.add(task);
        }
    }

    // Method to remove a task
    public void removeTask(String taskId) {
        Task task = allTasks.remove(taskId);
        if (task != null) {
            allTasksObservable.remove(taskId);

            // remove from other data structures and their ObservableLists
            inProgressTasks.remove(task);
            inProgressTasksObservable.remove(task);

            completedTasks.remove(task);
            completedTasksObservable.remove(task);

            priorityTasks.remove(task);
            priorityTasksObservable.remove(task);
        }
    }

    public void loadTasksForUser(String username) {
        File file = new File(username + ".txt");
        if (file.exists()) {
            try (Scanner scanner = new Scanner(file)) {
                // skips the first line (password)
                if (scanner.hasNextLine()) {
                    scanner.nextLine();
                }

                while (scanner.hasNextLine()) {
                    String taskData = scanner.nextLine();
                    Task task = convertStringToTask(taskData);

                    if (task != null) { // Check if task is not null
                        // check if the task is completed and add to the appropriate list
                        if (task.isCompleted()) {
                            completedTasks.push(task);
                            completedTasksObservable.add(task);
                        } else {
                            inProgressTasks.add(task);
                            inProgressTasksObservable.add(task);
                        }

                        // add to priority queue if needed
                        if (task.getPriority() > 0) {
                            priorityTasks.add(task);
                            priorityTasksObservable.add(task);
                        }

                        // adds to all tasks map
                        allTasks.put(task.getId(), task);
                    } else {
                        System.err.println("Error parsing task data: " + taskData);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private Task convertStringToTask(String taskData) {
        boolean isCompleted = taskData.endsWith("~");
        if (isCompleted) {
            taskData = taskData.substring(0, taskData.length() - 1);
        }

        String[] parts = taskData.split(",");
        if (parts.length < 5) {
            // handles the error or log a warning
            System.err.println("Invalid task data format: " + taskData);
            return null;
        }

        Task task = new Task(parts[0], parts[1], Integer.parseInt(parts[2]), parts[3], parts[4]);
        task.setCompleted(isCompleted);
        return task;
    }

    @FXML
    private void handleSortButtonAction() {
        quickSort(inProgressTasksObservable, 0, inProgressTasksObservable.size() - 1);
        quickSort(completedTasksObservable, 0, completedTasksObservable.size() - 1);

        inProgressTasksTable.refresh();
        completedTasksTable.refresh();
    }

    // will sort the list using the quicksort algorithm
    private void quickSort(List<Task> list, int begin, int end) {
        if (begin < end) {
            int partitionIndex = partition(list, begin, end);

            quickSort(list, begin, partitionIndex-1);
            quickSort(list, partitionIndex+1, end);
        }
    }

    private int partition(List<Task> list, int begin, int end) {
        Task pivot = list.get(end);
        int i = (begin-1);

        for (int j = begin; j < end; j++) {
            if (list.get(j).getPriority() <= pivot.getPriority()) {
                i++;

                Task swapTemp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, swapTemp);
            }
        }

        Task swapTemp = list.get(i+1);
        list.set(i+1, list.get(end));
        list.set(end, swapTemp);

        return i+1;
    }

    // will finisht the task at top fo the queue
    @FXML
    private void handleFinishTopTaskButton() {
        if (!inProgressTasks.isEmpty()) {
            Task topTask = inProgressTasks.poll();
            if (topTask != null) {
                completeTask(topTask);
                // will save the changes to the text file
                saveAllTasksToFile();
            }
        }
    }



}
