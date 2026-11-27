import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class TaskStorageTest {
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    public static void main(String[] args) throws Exception {
        Path directory = Files.createTempDirectory("todo-storage-test-");
        Path file = directory.resolve("tasks.txt");

        try {
            check(TaskStorage.load(file).isEmpty(), "missing storage should load as an empty list");
            Files.createFile(file);
            check(TaskStorage.load(file).isEmpty(), "empty storage should load as an empty list");

            TaskStorage.save(file, List.of("First task", "తెలుగు పని", "日本語のタスク"));
            List<String> saved = TaskStorage.load(file);
            check(saved.equals(List.of("First task", "తెలుగు పని", "日本語のタスク")),
                "tasks should round-trip with UTF-8");

            boolean rejectedBlank = false;
            try {
                TaskStorage.save(file, List.of("valid task", "   "));
            } catch (IllegalArgumentException expected) {
                rejectedBlank = true;
            }
            check(rejectedBlank, "blank task titles should be rejected");
            check(TaskStorage.load(file).equals(saved),
                "invalid save must not overwrite existing valid tasks");

            Path directoryAsFile = directory.resolve("not-a-file");
            Files.createDirectory(directoryAsFile);
            boolean writeFailed = false;
            try {
                TaskStorage.save(directoryAsFile, List.of("new task"));
            } catch (java.io.IOException expected) {
                writeFailed = true;
            }
            check(writeFailed, "writing over a directory should fail");
            check(Files.isDirectory(directoryAsFile), "failed save must preserve the existing directory");

            Files.write(file, new byte[] {(byte) 0xC3, (byte) 0x28});
            boolean malformedRejected = false;
            try {
                TaskStorage.load(file);
            } catch (MalformedInputException expected) {
                malformedRejected = true;
            }
            check(malformedRejected, "malformed UTF-8 should be rejected instead of silently decoded");

            System.out.println("Task storage tests passed");
        } finally {
            Files.deleteIfExists(file);
            Files.deleteIfExists(directory.resolve("not-a-file"));
            Files.deleteIfExists(directory);
        }
    }
}
