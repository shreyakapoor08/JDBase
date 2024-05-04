package DBMS.MajorAssignment1.QueryFiles;

import DBMS.MajorAssignment1.Database;

public class InsertQueryExecutor {
    private Database database;

    public InsertQueryExecutor(Database database) {
        this.database = database;
    }

    public void execute(String query) {
        String[] parts = query.split(" ");
        if (parts.length < 6) {
            System.out.println("Invalid INSERT query.");
            return;
        }

        String tableName = parts[2];
        String columns = query.substring(query.indexOf("(") + 1, query.indexOf(")")).trim();
        String values = query.substring(query.lastIndexOf("(") + 1, query.lastIndexOf(")")).trim();

        database.insertData(tableName, columns, values);
    }
}
