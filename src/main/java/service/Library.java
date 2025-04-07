package service; 

 import java.util.ArrayList;
 import java.util.List;
 import java.util.Optional;
 import model.Book;
 import model.User;
 import java.io.*;
 import java.util.*;

 public class Library {
    private List<Book> books;
    private List<User> users;
    private Map<Integer, Integer> issuedBooks; // Key: BookID, Value: UserID

    // Constants for filenames and delimiter
    private static final String BOOKS_FILE = "books.csv";
    private static final String USERS_FILE = "users.csv";
    private static final String ISSUED_FILE = "issued.csv";
    private static final String DELIMITER = ",";

    // Initializes empty array lists, then loads existing data from CSV files to populate these lists
    public Library() {
        books = new ArrayList<>();
        users = new ArrayList<>();
        issuedBooks = new HashMap<>();
        // Load data when the Library is created
        loadData();
    }

    public void loadData() {
        loadBooksFromFile();
        loadUsersFromFile();
        loadIssuedBooksFromFile();
        // Update ID counters after all data is loaded
        Book.updateIdCounter(books);
        User.updateIdCounter(users);
        System.out.println("Data loaded successfully.");
    }

    public void saveData() {
        saveBooksToFile();
        saveUsersToFile();
        saveIssuedBooksToFile();
        System.out.println("Data saved successfully.");
    }

    private void loadBooksFromFile() {
        File file = new File(BOOKS_FILE);
        if (!file.exists()) {
            System.out.println("Book data file not found. Starting with an empty book list.");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(DELIMITER);
                // Check for correct number of fields
                if (data.length == 5) {
                    try {
                        int id = Integer.parseInt(data[0].trim());
                        String name = data[1].trim();
                        String author = data[2].trim();
                        int totalCopies = Integer.parseInt(data[3].trim());
                        int availableCopies = Integer.parseInt(data[4].trim());
                        // Use the loading constructor
                        books.add(new Book(id, name, author, totalCopies, availableCopies));
                    } catch (NumberFormatException e) {
                        System.out.println("Skipping invalid book data line (parse error): " + line + " - " + e.getMessage());
                    }
                } else {
                     System.out.println("Skipping invalid book data line (wrong field count): " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading books from file: " + e.getMessage());
        }
    }

    private void loadUsersFromFile() {
         File file = new File(USERS_FILE);
         if (!file.exists()) {
             System.out.println("User data file not found. Starting with an empty user list.");
             return;
         }
         try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
             String line;
             while ((line = reader.readLine()) != null) {
                 String[] data = line.split(DELIMITER);
                  if (data.length == 3) {
                     try {
                         int id = Integer.parseInt(data[0].trim());
                         String name = data[1].trim();
                         String phone = data[2].trim();
                         users.add(new User(id, name, phone));
                     } catch (NumberFormatException e) {
                          System.out.println("Skipping invalid user data line (parse error): " + line + " - " + e.getMessage());
                     }
                  } else {
                      System.out.println("Skipping invalid user data line (wrong field count): " + line);
                  }
             }
         } catch (IOException e) {
             System.out.println("Error loading users from file: " + e.getMessage());
         }
    }

    private void loadIssuedBooksFromFile() {
         File file = new File(ISSUED_FILE);
         if (!file.exists()) {
              System.out.println("Issued books data file not found. Assuming no books are currently issued.");
             return;
         }
         try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
             String line;
             while ((line = reader.readLine()) != null) {
                 String[] data = line.split(DELIMITER);
                 if (data.length == 2) { // BookID, UserID
                     try {
                         int bookId = Integer.parseInt(data[0].trim());
                         int userId = Integer.parseInt(data[1].trim());
                              issuedBooks.put(bookId, userId);
                     } catch (NumberFormatException e) {
                          System.out.println("Skipping invalid issued data line (parse error): " + line + " - " + e.getMessage());
                     }
                 } else {
                     System.out.println("Skipping invalid issued data line (wrong field count): " + line);
                 }
             }
         } catch (IOException e) {
             System.out.println("Error loading issued books data: " + e.getMessage());
         }
    }

    private void saveBooksToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKS_FILE))) {
            for (Book book : books) {
                writer.write(book.toCsvString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving books to file: " + e.getMessage());
        }
    }

     private void saveUsersToFile() {
         try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE))) {
             for (User user : users) {
                 writer.write(user.toCsvString());
                 writer.newLine();
             }
         } catch (IOException e) {
             System.out.println("Error saving users to file: " + e.getMessage());
         }
     }

     private void saveIssuedBooksToFile() {
          try (BufferedWriter writer = new BufferedWriter(new FileWriter(ISSUED_FILE))) {
              for (Map.Entry<Integer, Integer> entry : issuedBooks.entrySet()) {
                  writer.write(entry.getKey() + DELIMITER + entry.getValue());
                  writer.newLine();
              }
          } catch (IOException e) {
              System.out.println("Error saving issued books data: " + e.getMessage());
          }
     }

    public void addBook(String name, String author, int copies) {
        Book newBook = new Book(name, author, copies);
        books.add(newBook);
        System.out.println("Added book: " + newBook.getName() + " (ID: " + newBook.getId() + ")");
    }

    public void addUser(String name, String phone) {
        User newUser = new User(name, phone);
        users.add(newUser);
        System.out.println("Added user: " + newUser.getName() + " (ID: " + newUser.getId() + ")");
    }

    public Optional<Book> findBookById(int bookId) {
        for (Book book : books) {
            if (book.getId() == bookId) {
                return Optional.of(book);
            }
        }
        return Optional.empty();
    }

     public Optional<User> findUserById(int userId) {
        for (User user : users) {
            if (user.getId() == userId) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

     public void retrieveBookDetails(int bookId) {
         Optional<Book> bookOpt = findBookById(bookId);
         if (bookOpt.isPresent()) {
             Book book = bookOpt.get();
             String availability;
             if (issuedBooks.containsKey(bookId)) {
                 Optional<User> issuerOpt = findUserById(issuedBooks.get(bookId));
                 String issuerName = issuerOpt.isPresent() ? issuerOpt.get().getName() : "Unknown User";
                 availability = "Issued to " + issuerName + " (ID: " + issuedBooks.get(bookId) + ")";
             } else if (book.getAvailableCopies() > 0) {
                 availability = "Available (" + book.getAvailableCopies() + "/" + book.getTotalCopies() + ")";
             } else {
                 availability = "All copies currently issued";
             }
              System.out.println("Book Details:");
              System.out.println("  ID: " + book.getId());
              System.out.println("  Name: " + book.getName());
              System.out.println("  Author: " + book.getAuthor());
              System.out.println("  Status: " + availability);
         } else {
             System.out.println("Error: Book with ID " + bookId + " not found.");
         }
     }

     public void issueBook(int bookId, int userId) {
         Optional<Book> bookOpt = findBookById(bookId);
         Optional<User> userOpt = findUserById(userId);

         if (!bookOpt.isPresent()) {
             System.out.println("Error: Invalid Book ID " + bookId);
             return;
         }
         if (!userOpt.isPresent()) {
              System.out.println("Error: Invalid User ID " + userId);
              return;
         }

         Book book = bookOpt.get();
         User user = userOpt.get();

         if (issuedBooks.containsKey(bookId)) {
              int issuerUserId = issuedBooks.get(bookId);
              System.out.println("Error: Book '" + book.getName() + "' is already issued to User ID " + issuerUserId);
         } else if (book.getAvailableCopies() <= 0) {
              System.out.println("Error: No copies of '" + book.getName() + "' are available.");
         } else {
        	  // Decrement available copies
              if (book.issueCopy()) {
                  issuedBooks.put(bookId, userId);
                  System.out.println("Book '" + book.getName() + "' issued to User '" + user.getName() + "'.");
              } else {
                  System.out.println("Error: Could not issue book '" + book.getName() + "'.");
              }
         }
     }

     public void returnBook(int bookId, int userId) {
         Optional<Book> bookOpt = findBookById(bookId);
         Optional<User> userOpt = findUserById(userId);

         if (!bookOpt.isPresent()) {
             System.out.println("Error: Invalid Book ID " + bookId);
             return;
         }
         if (!userOpt.isPresent() && issuedBooks.containsKey(bookId) && issuedBooks.get(bookId) == userId) {
              System.out.println("Error: User ID " + userId + " returning the book not found, but book was issued to this ID.");
         }

         Book book = bookOpt.get();
         User user = userOpt.orElse(null);

         if (!issuedBooks.containsKey(bookId)) {
             System.out.println("Error: Book '" + book.getName() + "' is not currently marked as issued.");
         } else if (issuedBooks.get(bookId) != userId) {
             int actualIssuerId = issuedBooks.get(bookId);
             Optional<User> actualIssuerOpt = findUserById(actualIssuerId);
             String actualIssuerName = actualIssuerOpt.isPresent() ? actualIssuerOpt.get().getName() : "Unknown User";
             System.out.println("Error: Book '" + book.getName() + "' was issued to User '" + actualIssuerName +"' (ID: "+ actualIssuerId +"), not User ID " + userId + ".");
         } else {
             // Process the return
             book.returnCopy();
             issuedBooks.remove(bookId);
             String returnerName = (user != null) ? user.getName() : "ID " + userId;
             System.out.println("Book '" + book.getName() + "' returned successfully by User '" + returnerName + "'.");
         }
    }

    public void displayAllBooks() {
        System.out.println("\n--- Library Catalog ---");
        if (books.isEmpty()) {
            System.out.println("No books in the library.");
        } else {
            // Sort books by ID
            books.sort(Comparator.comparingInt(Book::getId));
            for (Book book : books) {
                System.out.println(book);
            }
        }
        System.out.println("----------------------");
    }

     public void displayAllUsers() {
         System.out.println("\n--- Library Users ---");
         if (users.isEmpty()) {
             System.out.println("No users registered.");
         } else {
             // Sort users by ID
             users.sort(Comparator.comparingInt(User::getId));
             for (User user : users) {
                 System.out.println(user);
             }
         }
         System.out.println("---------------------");
     }
 }