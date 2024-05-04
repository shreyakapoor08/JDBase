package DBMS.MajorAssignment1;

import DBMS.MajorAssignment1.Auth.Authentication;
import DBMS.MajorAssignment1.Auth.CaptchaManager;
import DBMS.MajorAssignment1.QueryFiles.QueryExecutor;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

import static DBMS.MajorAssignment1.QueryFiles.QueryExecutor.executeQuery;

/**
 * Main class for the custom DBMS application.
 */
public class Main {
    private static Authentication userAuth;
    private static CaptchaManager captchaManager;
    private static DatabaseManager databaseManager;
    private static QueryExecutor queryExecutor;
    public static LinkedList<String> transactionQueries = new LinkedList<>();
    public static boolean isTransactionActive = false;
    private static Database transactionDatabase;


    public static void main(String[] args) {
        initializeComponents();
        runDBMS();
    }

    private static void initializeComponents() {
        userAuth = new Authentication();
        captchaManager = new CaptchaManager();
        databaseManager = new DatabaseManager();
        queryExecutor = new QueryExecutor(databaseManager.getSharedDatabase());
        transactionDatabase = new Database();
    }

    /**
     * Displays the main menu options for the user.
     */
    private static void displayMainMenu() {
        System.out.println("1. Signup");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.print("Select an option: ");
    }

    private static void displayDBMSMenu() {
        System.out.println("\nDBMS Menu:");
        System.out.println("1. Create Database");
        System.out.println("2. Begin Transaction");
        System.out.println("3. Execute Query");
        System.out.println("4. Commit Transaction");
        System.out.println("5. Rollback Transaction");
        System.out.println("6. Logout");
        System.out.print("Select an option: ");
    }

    private static void runDBMS() {
        System.out.print("Welcome to my custom DB\n");
        Scanner scanner = new Scanner(System.in);

        while (true) {
            displayMainMenu();
            int choice;

            while (true) {
                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                    break;
                } else {
                    System.out.println("Invalid selection. Please enter a numeric option.");
                    scanner.nextLine();
                }
            }

            scanner.nextLine();

            if (choice == 1) {
                signupFlow(scanner);
            } else if (choice == 2) {
                loginFlow(scanner);
            } else if (choice == 3) {
                System.out.println("Exiting the DBMS");
                break;
            } else {
                System.out.println("Invalid option.");
            }
        }

        scanner.close();
    }


    private static void loginFlow(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (userAuth.authenticateUser(username, password)) {
            String captcha = captchaManager.getGeneratedCaptcha();
            System.out.println("Captcha: " + captcha);

            System.out.print("Enter the captcha: ");
            String captchaInput = scanner.nextLine();

            if (captchaManager.validateCaptcha(captchaInput)) {
                System.out.println("Authentication successful!");

                while (true) {
                    displayDBMSMenu();
                    int dbmsChoice;

                    while (true) {
                        if (scanner.hasNextInt()) {
                            dbmsChoice = scanner.nextInt();
                            break;
                        } else {
                            System.out.println("Invalid selection. Please enter a numeric option.");
                            scanner.nextLine();
                        }
                    }

                    scanner.nextLine();

                    if (dbmsChoice == 1) {
                        File f = new File("data.json");
                        if(f.exists()){
                            System.out.println("Database Already Exist");
                        }
                        else{
                            try {
                                f.createNewFile();
                                System.out.println("Database created");
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    } else if (dbmsChoice == 3) {
                        System.out.print("Enter SQL query: ");
                        String sqlQuery = scanner.nextLine();

                        if (isTransactionActive) {
                            transactionQueries.add(sqlQuery);
                            System.out.println("Following Query added to the transaction:");
                            System.out.println(sqlQuery);
                        } else {
                            queryExecutor.executeQuery(sqlQuery);
                        }
                    } else if (dbmsChoice == 2) {
                        // Begin Transaction
                        System.out.println("Transaction started.");
                        isTransactionActive = true;
                    } else if (dbmsChoice == 4) {
                        // Commit Transaction
                        commitTransaction();
                        System.out.println("Transaction committed.");
                    } else if (dbmsChoice == 5) {
                        // Rollback Transaction
                        rollbackTransaction();
                        System.out.println("Transaction rolled back.");
                    } else if (dbmsChoice == 6) {
                        System.out.println("Logging out.");
                        break;
                    } else {
                        System.out.println("Invalid option.");
                    }
                }
            } else {
                System.out.println("Captcha validation failed. Please try logging in again");
            }
        } else {
            System.out.println("Authentication failed. Invalid username or password.");
        }
    }

    private static void signupFlow(Scanner scanner) {
        System.out.print("Enter username for signup: ");
        String username = scanner.nextLine();
        System.out.print("Enter password for signup: ");
        String password = scanner.nextLine();

        userAuth.signupUser(username, password);
    }

    private static void commitTransaction() {
        if (isTransactionActive) {
            for (String query : transactionQueries) {
                executeQuery(query);
            }

            transactionQueries.clear();

            isTransactionActive = false;
        } else {
            return;
        }
    }


    private static void rollbackTransaction() {
        if (isTransactionActive) {
            System.out.println("Transaction queries to be rolled back:");
            for (String query : transactionQueries) {
                System.out.println(query);
            }

            transactionQueries.clear();
            isTransactionActive = false;

            System.out.println("Transaction rolled back.");

            System.out.println("Transaction queries rolled back:");
            for (String query : transactionQueries) {
                System.out.println(query);
            }
        } else {
            System.out.println("No active transaction to rollback.");
        }
    }


}
