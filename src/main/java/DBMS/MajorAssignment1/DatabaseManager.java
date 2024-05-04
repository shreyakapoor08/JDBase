package DBMS.MajorAssignment1;

/**
 * Initializes the shared database instance.
 */
public class DatabaseManager {
    private static Database sharedDatabase;

    public DatabaseManager() {
        sharedDatabase = new Database();
    }

    /**
     * Retrieves the shared database instance.
     *
     * @return The shared database instance.
     */
    public static Database getSharedDatabase() {
        return sharedDatabase;
    }
}