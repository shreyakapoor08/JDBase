package DBMS.MajorAssignment1.Auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Authentication {
    private Map<String, User> users;
    private static final String USER_DATA_FILE = "user_data.txt";

    public Authentication() {
        users = new HashMap<>();
        readUserDataFromFile();
    }

    public void addUser(String username, String password) {
        String hashedPassword = hashPassword(password);
        User user = new User(username, hashedPassword);
        users.put(username, user);
        writeUserDataToFile();
    }

    public void signupUser(String username, String password) {
        if (users.containsKey(username)) {
            System.out.println("Username is already taken. Please choose another one.");
        } else {
            addUser(username, password);
            System.out.println("User registration successful!");
        }
    }

    public boolean authenticateUser(String username, String password) {
        if (users.containsKey(username)) {
            User user = users.get(username);
            String hashedPassword = hashPassword(password);
            return user.getHashedPassword().equals(hashedPassword);
        }
        return false;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] passwordBytes = password.getBytes();
            byte[] hashedBytes = md.digest(passwordBytes);
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void writeUserDataToFile() {
        try (FileWriter fileWriter = new FileWriter(USER_DATA_FILE)) {
            for (User user : users.values()) {
                String userDataLine = "username: " + user.getUsername() + ", hashedPassword: " + user.getHashedPassword() + "\n";
                fileWriter.write(userDataLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void readUserDataFromFile() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(USER_DATA_FILE))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String username = parts[0];
                    String hashedPassword = parts[1];
                    User user = new User(username, hashedPassword);
                    users.put(username, user);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
