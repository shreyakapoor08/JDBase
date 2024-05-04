package DBMS.MajorAssignment1.QueryFiles;

import DBMS.MajorAssignment1.Database;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class SelectQueryExecutor {
    private Database database;

    public SelectQueryExecutor(Database database) {
        this.database = database;
    }

    public void execute(String query) {
        query = query.trim();
        if (query.endsWith(";")) {
            query = query.substring(0, query.length() - 1);
        }

        String[] parts = query.split(" ");
        if (parts.length < 4) {
            System.out.println("Invalid SELECT query.");
            return;
        }

        if (!parts[1].equals("*") || !parts[2].equalsIgnoreCase("FROM")) {
            System.out.println("Invalid SELECT query format. Please use 'SELECT * FROM table'.");
            return;
        }

        String tableName = parts[3];
        JSONArray result = database.selectData(tableName);

        if (!result.isEmpty()) {
            System.out.println("Query result for table '" + tableName + "':");
            for (Object obj : result) {
                JSONObject row = (JSONObject) obj;
                for (Object key : row.keySet()) {
                    System.out.println(key + ": " + row.get(key));
                }
                System.out.println("----------");
            }
        } else {
            System.out.println("No data found in table '" + tableName + "'.");
        }
    }

}
