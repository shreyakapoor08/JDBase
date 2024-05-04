package DBMS.MajorAssignment1.QueryFiles;

import DBMS.MajorAssignment1.Database;

import java.util.LinkedList;

import static DBMS.MajorAssignment1.Main.isTransactionActive;


public class QueryExecutor {
    private static Database database;
    private LinkedList<String> transactionQueries;

    public QueryExecutor(Database database) {
        this.database = database;
    }

    /**
     * Executes an SQL query and handles SELECT, INSERT, UPDATE, DELETE, and CREATE TABLE statements.
     *
     * @param query The SQL query to execute.
     */
    public static void executeQuery(String query) {
        String[] parts = query.trim().split(" ");
        if (parts.length < 2) {
            System.out.println("Invalid query.");
            return;
        }
        String queryType = parts[0].toUpperCase();

        switch (queryType) {
            case "SELECT":
                executeSelectQuery(query);
                break;
            case "INSERT":
                executeInsertQuery(query);
                break;
            case "UPDATE":
                executeUpdateQuery(query);
                break;
            case "DELETE":
                executeDeleteQuery(query);
                break;
            case "CREATE":
                executeCreateTableQuery(query);
                break;
            default:
                System.out.println("Unsupported query type.");
        }
    }

    private static void executeSelectQuery(String query) {
        SelectQueryExecutor selectQueryExecutor = new SelectQueryExecutor(database);
        selectQueryExecutor.execute(query);
    }

    private static void executeInsertQuery(String query) {
        InsertQueryExecutor insertQueryExecutor = new InsertQueryExecutor(database);
        insertQueryExecutor.execute(query);
    }

    private static void executeUpdateQuery(String query) {
        UpdateQueryExecutor updateQueryExecutor = new UpdateQueryExecutor(database);
        updateQueryExecutor.execute(query);
    }

    private static void executeDeleteQuery(String query) {
        DeleteQueryExecutor deleteQueryExecutor = new DeleteQueryExecutor(database);
        deleteQueryExecutor.execute(query);
    }

    private static void executeCreateTableQuery(String query) {
        CreateTableQueryExecutor createTableExecutor = new CreateTableQueryExecutor(database);
        createTableExecutor.execute(query);
    }

    private boolean isTransactionActive() {
        return isTransactionActive;
    }
}
