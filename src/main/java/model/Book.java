package model;

import java.util.List;

public class Book {
   private static int idCounter = 1; // Default starting ID
   private int id;
   private String name;
   private String author;
   private int totalCopies;
   private int availableCopies;

   private static final String DELIMITER = ","; // Delimiter for CSV file, to separate fields

   // Constructor for adding books and generate an ID
   public Book(String name, String author, int totalCopies) {
       this(idCounter++, name, author, totalCopies, totalCopies); // Call the main constructor
   }

   // Constructor for loading books from CSV, takes existing ID
   public Book(int id, String name, String author, int totalCopies, int availableCopies) {
       this.id = id;
       this.name = name;
       this.author = author;
       this.totalCopies = totalCopies;
       this.availableCopies = availableCopies;
       if (id >= idCounter) {
            idCounter = id + 1;
       }
   }

   // Getters to access private fields
   public int getId() { return id; }
   public String getName() { return name; }
   public String getAuthor() { return author; }
   public int getTotalCopies() { return totalCopies; }
   public int getAvailableCopies() { return availableCopies; }


   // Method to issue a copy (called by Library)
   public boolean issueCopy() {
       if (availableCopies > 0) {
           availableCopies--;
           return true; // Successfully issued
       }
       return false; // No copies available
   }

   // Method to return a copy (called by Library)
   public void returnCopy() {
       if (availableCopies < totalCopies) {
            availableCopies++;
       } else {
            // Log error if trying to return more copies than total
            System.out.println("Attempted to return a copy of '" + name + "' when all copies were already available.");
       }
   }

   // Format Book data as a CSV string
   public String toCsvString() {
       // TODO: Handle commas automatically
       return id + DELIMITER + name + DELIMITER + author + DELIMITER + totalCopies + DELIMITER + availableCopies;
   }

   // Updates counter after loading all data, so new books get unique IDs, higher than loaded IDs
   public static void updateIdCounter(List<Book> books) {
        int maxId = 0;
        for (Book book : books) {
            if (book.getId() > maxId) {
                maxId = book.getId();
            }
        }
        idCounter = maxId + 1;
    }


   // Used to display books in the console
   @Override
   public String toString() {
       return "ID: " + id + ", Name: '" + name + "', Author: '" + author +
              "', Available Copies: " + availableCopies + "/" + totalCopies;
   }
}