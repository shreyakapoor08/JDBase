package DBMS.MajorAssignment1;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Database {
    private Map<String, Map<Integer, JSONObject>> tables;
    private JSONObject data;

    public Database() {
        tables = new HashMap<>();
        data = new JSONObject();
        loadTablesFromFiles();
        loadMainDataFromFile("data.json");
    }

    private Map<String, Map<Integer, JSONObject>> transactionData = new HashMap<>();


    public void saveTableDataToFile(String tableName) {
        try (FileWriter fileWriter = new FileWriter("data_" + tableName + ".json")) {
            Map<Integer, JSONObject> table = tables.get(tableName);
            JSONArray tableData = new JSONArray();
            for (int rowId : table.keySet()) {
                tableData.add(table.get(rowId));
            }
            fileWriter.write(tableData.toJSONString());
        } catch (IOException e) {
            System.out.println("Error saving data to file: " + e.getMessage());
        }
    }

    public void loadTableDataFromFile(String tableName) {
        JSONParser parser = new JSONParser();
        try (FileReader fileReader = new FileReader("data_" + tableName + ".json")) {
            JSONArray tableData = (JSONArray) parser.parse(fileReader);
            System.out.println("Loaded data for table " + tableName + ": " + tableData);
            Map<Integer, JSONObject> table = new HashMap<>();
            int rowId = 1;
            for (Object row : tableData) {
                if (row instanceof JSONObject) {
                    table.put(rowId, (JSONObject) row);
                    rowId++;
                }
            }
            tables.put(tableName, table);
        } catch (IOException | org.json.simple.parser.ParseException | NullPointerException e) {
            tables.put(tableName, new HashMap<>());
        }
    }


    public void loadTablesFromFiles() {
        for (String tableName : getTableNames()) {
            loadTableDataFromFile(tableName);
        }
    }

    public void createTable(String tableName, Map<String, String> tableDefinition) {
        if (tables.containsKey(tableName)) {
            System.out.println("Table already exists.");
        } else {
            tables.put(tableName, new HashMap<>());
            saveTableDataToFile(tableName);
            System.out.println("Table created: " + tableName);
            System.out.println("Table Definition: " + tableDefinition);

            JSONObject tableStructure = new JSONObject();
            for (Map.Entry<String, String> entry : tableDefinition.entrySet()) {
                tableStructure.put(entry.getKey(), entry.getValue());
            }

            data.put(tableName, tableStructure);

            saveDataToFile("data.json");
        }
    }

    public List<String> getTableNames() {
        List<String> tableNames = new ArrayList<>();
        for (String fileName : new File(".").list()) {
            if (fileName.startsWith("data_") && fileName.endsWith(".json")) {
                String tableName = fileName.substring(5, fileName.length() - 5);
                tableNames.add(tableName);
            }
        }
        return tableNames;
    }

    public void insertData(String tableName, String columns, String values) {
        if (tables.containsKey(tableName)) {
            Map<Integer, JSONObject> table = tables.get(tableName);
            JSONObject rowData = new JSONObject();
            String[] colArray = columns.split(",");
            String[] valArray = values.split(",");

            if (colArray.length != valArray.length) {
                System.out.println("Column count doesn't match value count.");
                return;
            }

            for (int i = 0; i < colArray.length; i++) {
                rowData.put(colArray[i].trim(), valArray[i].trim());
            }

            int newRowId = table.size() + 1;
            table.put(newRowId, rowData);

            saveTableDataToFile(tableName);

            System.out.println("Values Inserted Successfully.");
        } else {
            System.out.println("Table not found.");
        }
    }

    public JSONArray selectData(String tableName) {
        if (tables.containsKey(tableName)) {
            Map<Integer, JSONObject> table = tables.get(tableName);
            JSONArray result = new JSONArray();

            for (int rowId : table.keySet()) {
                JSONObject rowData = table.get(rowId);
                result.add(rowData);
            }

            return result;
        }

        System.out.println("Table not found.");
        return new JSONArray();
    }

    public void updateData(String tableName, String setClause, String conditions) {
        if (tables.containsKey(tableName)) {
            Map<Integer, JSONObject> table = tables.get(tableName);
            JSONArray rowsToUpdate = new JSONArray();

            for (int rowId : table.keySet()) {
                JSONObject rowData = table.get(rowId);
                if (checkConditions(rowData, conditions)) {
                    String[] setParts = setClause.split("=");
                    if (setParts.length == 2) {
                        String columnName = setParts[0].trim();
                        String newValue = setParts[1].trim();
                        rowData.put(columnName, newValue);
                        rowsToUpdate.add(rowId);
                    }
                }
            }

            if (!rowsToUpdate.isEmpty()) {
                for (Object rowId : rowsToUpdate) {
                    System.out.println("Row updated");
                }
                saveTableDataToFile(tableName);
            } else {
                System.out.println("No rows were updated.");
            }
        } else {
            System.out.println("Table not found.");
        }
    }

    public void deleteData(String tableName, String conditions) {
        if (tables.containsKey(tableName)) {
            Map<Integer, JSONObject> table = tables.get(tableName);
            JSONArray rowsToDelete = new JSONArray();

            for (int rowId : table.keySet()) {
                JSONObject rowData = table.get(rowId);
                if (checkConditions(rowData, conditions)) {
                    rowsToDelete.add(rowId);
                }
            }

            if (!rowsToDelete.isEmpty()) {
                for (Object rowId : rowsToDelete) {
                    table.remove(rowId);
                    System.out.println("Row deleted");
                }
                saveTableDataToFile(tableName);
            } else {
                System.out.println("No rows were deleted.");
            }
        } else {
            System.out.println("Table not found.");
        }
    }


    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }



    private void loadMainDataFromFile(String fileName) {
        JSONParser parser = new JSONParser();
        try (FileReader fileReader = new FileReader(fileName)) {
            data = (JSONObject) parser.parse(fileReader);
        } catch (IOException | org.json.simple.parser.ParseException | NullPointerException e) {
            data = new JSONObject();
        }
    }

    private void saveDataToFile(String fileName) {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(data.toJSONString());
        } catch (IOException e) {
            System.out.println("Error saving data to file: " + e.getMessage());
        }
    }

    private boolean checkConditions(JSONObject rowData, String conditions) {
        String[] conditionParts = conditions.split("=");
        if (conditionParts.length == 2) {
            String columnName = conditionParts[0].trim();
            String expectedValue = conditionParts[1].trim();

            if (rowData.containsKey(columnName)) {
                String actualValue = rowData.get(columnName).toString();
                return actualValue.equals(expectedValue);
            }
        }
        return false;
    }


    public boolean tableExists(String tableName) {
        return tables.containsKey(tableName);
    }
}