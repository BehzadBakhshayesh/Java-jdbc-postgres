package library;

public interface LibraryStorage {
    void save(LibraryData data);
    LibraryData load();
}