import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ToDoApp extends JFrame {
    private static final Path TASK_FILE = Paths.get("tasks.txt");

    private final DefaultListModel<String> taskListModel;
    private final JList<String> taskList;
    private final JTextField taskField;
    private final JButton addButton;
    private final JButton deleteButton;
    private boolean storageReady;

    public ToDoApp() {
        setTitle("To-Do List App");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        JScrollPane scrollPane = new JScrollPane(taskList);

        taskField = new JTextField(20);
        addButton = new JButton("Add Task");
        deleteButton = new JButton("Delete Task");

        JPanel inputPanel = new JPanel();
        inputPanel.add(taskField);
        inputPanel.add(addButton);
        inputPanel.add(deleteButton);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        loadTasks();

        addButton.addActionListener(e -> addTask());
        deleteButton.addActionListener(e -> deleteTask());
    }

    private void loadTasks() {
        try {
            for (String task : TaskStorage.load(TASK_FILE)) {
                taskListModel.addElement(task);
            }
            storageReady = true;
        } catch (Exception e) {
            storageReady = false;
            addButton.setEnabled(false);
            deleteButton.setEnabled(false);
            JOptionPane.showMessageDialog(
                this,
                "Saved tasks could not be read. The existing file was left unchanged. "
                    + "Resolve the file problem before editing tasks.",
                "Task storage unavailable",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void addTask() {
        String task = taskField.getText().trim();
        if (task.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a task!");
            return;
        }

        List<String> updatedTasks = currentTasks();
        updatedTasks.add(task);
        if (saveTasks(updatedTasks)) {
            taskListModel.addElement(task);
            taskField.setText("");
        }
    }

    private void deleteTask() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Select a task to delete!");
            return;
        }

        List<String> updatedTasks = currentTasks();
        updatedTasks.remove(selectedIndex);
        if (saveTasks(updatedTasks)) {
            taskListModel.remove(selectedIndex);
        }
    }

    private List<String> currentTasks() {
        return new ArrayList<>(Collections.list(taskListModel.elements()));
    }

    private boolean saveTasks(List<String> tasks) {
        if (!storageReady) return false;
        try {
            TaskStorage.save(TASK_FILE, tasks);
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Tasks were not changed because saving failed. Check the task file and try again.",
                "Unable to save tasks",
                JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ToDoApp().setVisible(true));
    }
}
