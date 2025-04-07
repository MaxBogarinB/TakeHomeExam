package model;

import java.util.List;

public class User {
   private static int idCounter = 1; // Default starting ID
   private int id;
   private String name;
   private String phoneNumber;

   private static final String DELIMITER = ","; // Delimiter for CSV file, to separate fields

   // Constructor for adding users and generate an ID
   public User(String name, String phoneNumber) {
       this(idCounter++, name, phoneNumber); // Call the main constructor
   }

   // Constructor for loading users from CSV, takes existing ID
   public User(int id, String name, String phoneNumber) {
       this.id = id;
       this.name = name;
       this.phoneNumber = phoneNumber;
       if (id >= idCounter) {
           idCounter = id + 1;
       }
   }

   // Getters to access private fields
   public int getId() { return id; }
   public String getName() { return name; }
   public String getPhoneNumber() { return phoneNumber; }

   // Format User data as a CSV string
   public String toCsvString() {
	   // TODO: Handle commas automatically
       return id + DELIMITER + name + DELIMITER + phoneNumber;
   }

    // Updates counter after loading all data, so new users get unique IDs, higher than loaded IDs
    public static void updateIdCounter(List<User> users) {
        int maxId = 0;
        for (User user : users) {
            if (user.getId() > maxId) {
                maxId = user.getId();
            }
        }
        idCounter = maxId + 1;
    }

   // Used to display users in the console
   @Override
   public String toString() {
       return "ID: " + id + ", Name: '" + name + "', Phone: '" + phoneNumber + "'";
   }
}