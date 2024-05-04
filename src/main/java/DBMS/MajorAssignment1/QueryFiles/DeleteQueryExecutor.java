package DBMS.MajorAssignment1.QueryFiles;

import DBMS.MajorAssignment1.Database;

public class DeleteQueryExecutor {
    private Database database;

    public DeleteQueryExecutor(Database database) {
        this.database = database;
    }

    public void execute(String query) {
        if (query.endsWith(";")) {
            query = query.substring(0, query.length() - 1);
        }

        String[] parts = query.split(" ");
        if (parts.length < 3) {
            System.out.println("Invalid DELETE query.");
            return;
        }

        String tableName = parts[2];
        String conditions = query.substring(query.indexOf("WHERE") + 5).trim();

        database.deleteData(tableName, conditions);
    }
}
