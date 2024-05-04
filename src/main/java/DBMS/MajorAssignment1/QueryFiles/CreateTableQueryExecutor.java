package DBMS.MajorAssignment1.QueryFiles;

import DBMS.MajorAssignment1.Database;

import java.util.HashMap;
import java.util.Map;

public class CreateTableQueryExecutor {
    private Database database;

    public CreateTableQueryExecutor(Database database) {
        this.database = database;
    }

    public void execute(String query) {
        String[] parts = query.split(" ");
        if (parts.length < 4) {
            System.out.println("Invalid CREATE TABLE query.");
            return;
        }

        String tableName = parts[2];
        String columnsPart = query.substring(query.indexOf("(") + 1, query.lastIndexOf(");")).trim();

        Map<String, String> columns = parseTableDefinition(columnsPart);

        database.createTable(tableName, columns);
    }

    private Map<String, String> parseTableDefinition(String columns) {
        Map<String, String> tableDefinition = new HashMap<>();
        String[] columnArray = columns.split(",");
        for (String column : columnArray) {
            String[] columnParts = column.trim().split(" ");
            if (columnParts.length != 2) {
                System.out.println("Invalid column definition: " + column);
                return new HashMap<>();
            }
            String columnName = columnParts[0];
            String dataType = columnParts[1];
            tableDefinition.put(columnName, dataType);
        }
        return tableDefinition;
    }
}
