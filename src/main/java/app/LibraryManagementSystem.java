package app; 

 import java.util.Scanner;
 import java.util.InputMismatchException;
 import service.Library;

 public class LibraryManagementSystem {

    public static void main(String[] args) {
        Library library = new Library();
        Scanner scanner = new Scanner(System.in);
        int choice = -1;

        while (choice != 0) {
            displayMenu();
            System.out.print("Enter your choice: ");

            try {
                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1: // Add Book
                        System.out.print("Enter book name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter author name: ");
                        String author = scanner.nextLine();
                        System.out.print("Enter number of copies: ");
                        int copies = readIntInput(scanner); // Use helper for robust int input
                        library.addBook(name, author, copies);
                        break;
                    case 2: // Add User
                         System.out.print("Enter user name: ");
                         String userName = scanner.nextLine();
                         System.out.print("Enter phone number: ");
                         String phone = scanner.nextLine();
                         library.addUser(userName, phone);
                        break;
                    case 3: // Retrieve Book Details
                        System.out.print("Enter book ID to retrieve: ");
                        int retrieveId = readIntInput(scanner);
                        library.retrieveBookDetails(retrieveId);
                        break;
                    case 4: // Issue Book
                        System.out.print("Enter book ID to issue: ");
                        int issueBookId = readIntInput(scanner);
                        System.out.print("Enter user ID to issue to: ");
                        int issueUserId = readIntInput(scanner);
                        library.issueBook(issueBookId, issueUserId);
                        break;
                    case 5: // Return Book
                         System.out.print("Enter book ID to return: ");
                         int returnBookId = readIntInput(scanner);
                         System.out.print("Enter user ID returning the book: ");
                         int returnUserId = readIntInput(scanner);
                         library.returnBook(returnBookId, returnUserId);
                        break;
                    case 6: // Display All Books
                         library.displayAllBooks();
                        break;
                    case 7: // Display All Users
                         library.displayAllUsers();
                        break;
                    case 0:
                        System.out.println("Exiting Library System...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                 System.out.println("Invalid input. Please enter a number.");
                 scanner.nextLine();
                 // Reset choice to continue loop
                 choice = -1;
            } catch (Exception e) {
                  System.err.println("An error occurred: " + e.getMessage());
                  e.printStackTrace();
                  choice = -1;
            }
             if (choice != 0) {
                 System.out.println("\nPress Enter to continue...");
                 // Pause for user to read output
                 scanner.nextLine();
             }
        }

        // Save data before closing the application
        library.saveData();
        scanner.close();
        System.out.println("Application closed.");
    }

    public static void displayMenu() {
        System.out.println("\n=============================");
        System.out.println(" Library Management System Menu");
        System.out.println("=============================");
        System.out.println("1. Add Book");
        System.out.println("2. Add User");
        System.out.println("3. Retrieve Book Details");
        System.out.println("4. Issue Book");
        System.out.println("5. Return Book");
        System.out.println("6. Display All Books");
        System.out.println("7. Display All Users");
        System.out.println("0. Save and Exit");
        System.out.println("=============================");
    }

    // Method to handle integer input
    private static int readIntInput(Scanner scanner) {
        int input = -1;
        while (true) {
            try {
                input = scanner.nextInt();
                scanner.nextLine();
                return input;
            } catch (InputMismatchException e) {
                System.out.print("Invalid input. Please enter a whole number: ");
                scanner.nextLine();
            }
        }
    }
 }