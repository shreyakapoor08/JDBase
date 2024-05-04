package DBMS.MajorAssignment1.QueryFiles;

import DBMS.MajorAssignment1.Database;

public class UpdateQueryExecutor {
    private Database database;

    public UpdateQueryExecutor(Database database) {
        this.database = database;
    }

    public void execute(String query) {
        if (query.endsWith(";")) {
            query = query.substring(0, query.length() - 1);
        }

        String[] parts = query.split(" ");
        if (parts.length < 6) {
            System.out.println("Invalid UPDATE query.");
            return;
        }

        String tableName = parts[1];
        String setClause = query.substring(query.indexOf("SET") + 4, query.indexOf("WHERE")).trim();
        String conditions = query.substring(query.indexOf("WHERE") + 5).trim();

        database.updateData(tableName, setClause, conditions);
    }
}
