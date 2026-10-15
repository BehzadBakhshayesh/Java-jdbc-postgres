package library;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class FileManager {
    public void saveText(String path, String text) {
        try {
            Files.writeString(Path.of(path), text);
        } catch (IOException e) {
            throw new LibraryException("File error: " + e.getMessage());
        }
    }

    public String loadText(String path) {
        try {
            return Files.readString(Path.of(path));
        } catch (IOException e) {
            throw new LibraryException("File error: " + e.getMessage());
        }
    }
}