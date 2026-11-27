import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public final class TaskStorage {
    private TaskStorage() {}

    public static List<String> load(Path file) throws IOException {
        try {
            List<String> stored = Files.readAllLines(file, StandardCharsets.UTF_8);
            List<String> tasks = new ArrayList<>(stored.size());
            for (String task : stored) {
                if (task == null || task.trim().isEmpty()) {
                    throw new IOException("Task file contains an empty task entry.");
                }
                tasks.add(task.trim());
            }
            return tasks;
        } catch (NoSuchFileException e) {
            return new ArrayList<>();
        }
    }

    public static void save(Path file, List<String> tasks) throws IOException {
        if (file == null || tasks == null) {
            throw new IllegalArgumentException("A task file and task list are required.");
        }

        List<String> normalized = new ArrayList<>(tasks.size());
        for (String task : tasks) {
            if (task == null || task.trim().isEmpty()) {
                throw new IllegalArgumentException("Task titles cannot be empty.");
            }
            normalized.add(task.trim());
        }

        Path target = file.toAbsolutePath().normalize();
        Path parent = target.getParent();
        if (parent == null) {
            throw new IOException("Task file must have a parent directory.");
        }

        Files.createDirectories(parent);
        Path temporary = Files.createTempFile(parent, target.getFileName().toString(), ".tmp");
        try {
            Files.write(temporary, normalized, StandardCharsets.UTF_8);
            try {
                Files.move(temporary, target,
                    StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temporary, target, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temporary);
        }
    }
}
