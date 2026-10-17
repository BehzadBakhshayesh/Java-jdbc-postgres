package library.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import library.exceptions.LibraryException;

import java.io.File;
import java.util.ArrayList;

public class JsonStorage implements LibraryStorage {
    private final FileManager fileManager = new FileManager();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final String path;

    public JsonStorage(String path) {
        this.path = path;
    }

    @Override
    public void save(LibraryData data) {
        fileManager.saveText(this.path, gson.toJson(data));
    }

    @Override
    public LibraryData load() {
        if (!new File(this.path).exists()) {
            return new LibraryData(new ArrayList<>(), new ArrayList<>());
        }
        String text = fileManager.loadText(this.path);
        try {
            LibraryData data = gson.fromJson(text, LibraryData.class);
            if (data == null) {
                return new LibraryData(new ArrayList<>(), new ArrayList<>());
            }
            return data;
        } catch (JsonSyntaxException e) {
            throw new LibraryException("Invalid JSON file: " + e.getMessage());
        }
    }
}