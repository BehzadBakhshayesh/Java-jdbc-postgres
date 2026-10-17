package library.utils;

public interface LibraryStorage {
    void save(LibraryData data);
    LibraryData load();
}