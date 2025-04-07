package tests;

import model.Book;
import model.User;
import service.Library;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;


 public class LibraryTest {

    private Library library;
    private static final String TEST_BOOKS_FILE = "test_books.csv";
    private static final String TEST_USERS_FILE = "test_users.csv";
    private static final String TEST_ISSUED_FILE = "test_issued.csv";

    @Before
    public void setUp() {
        // Clean up any leftover files from previous failed tests
        cleanupTestFiles();

        // Try to load default files
        library = new Library(); 
    }

    @After
    public void tearDown() {
        // Clean up files created during the test
        cleanupTestFiles();
    }

    private void cleanupTestFiles() {
        try {
            Files.deleteIfExists(Paths.get(TEST_BOOKS_FILE));
            Files.deleteIfExists(Paths.get(TEST_USERS_FILE));
            Files.deleteIfExists(Paths.get(TEST_ISSUED_FILE));
            Files.deleteIfExists(Paths.get("books.csv"));
            Files.deleteIfExists(Paths.get("users.csv"));
            Files.deleteIfExists(Paths.get("issued.csv"));
        } catch (IOException e) {
            System.err.println("Warning: Could not clean up test files: " + e.getMessage());
        }
    }

    @Test
    public void testAddBook() {
        library.addBook("Test Book", "Test Author", 1);

        // Assumes first book gets ID 1
        Optional<Book> foundBook = library.findBookById(1); 
        assertTrue("Book should be found by ID after adding.", foundBook.isPresent());
        assertEquals("Test Book", foundBook.get().getName());
        assertEquals("Test Author", foundBook.get().getAuthor());
        assertEquals(1, foundBook.get().getTotalCopies());
    }

    @Test
    public void testAddUser() {
    	library.addUser("Test User", "12345");
    	// Assumes first user gets ID 1
        Optional<User> foundUser = library.findUserById(1);
        assertTrue("User should be found by ID after adding.", foundUser.isPresent());
        assertEquals("Test User", foundUser.get().getName());
        assertEquals("12345", foundUser.get().getPhoneNumber());
    }


    @Test
    public void testFindBookByIdNotFound() {
        Optional<Book> foundBook = library.findBookById(999);
        assertFalse("Should not find a book with an ID that does not exist.", foundBook.isPresent());
    }

    @Test
    public void testIssueBookSuccess() {
        library.addBook("Available Book", "Author A", 1); // ID 1
        library.addUser("Test Borrower", "54321");
        library.issueBook(1, 1);

        // Verify book state
        Optional<Book> bookOpt = library.findBookById(1);
        assertTrue(bookOpt.isPresent());
        assertEquals("Available copies should be 0 after issuing.", 0, bookOpt.get().getAvailableCopies());
    }

     @Test
     public void testIssueBookNoCopiesAvailable() {
         library.addBook("Single Copy Book", "Author B", 1); // ID 1
         library.addUser("User 1", "111");
         library.addUser("User 2", "222");
         // Issue the only copy to User 1
         library.issueBook(1, 1);

         // Attempt to issue the same book to User 2 (should fail)
         library.issueBook(1, 2);

         // Verify book state hasn't changed unexpectedly
         Optional<Book> bookOpt = library.findBookById(1);
         assertTrue(bookOpt.isPresent());
         assertEquals("Available copies should still be 0.", 0, bookOpt.get().getAvailableCopies());
     }

     @Test
     public void testIssueBookInvalidBookId() {
         library.addUser("Test User", "123"); // ID 1
         // Attempt to issue a book that does not exist
         library.issueBook(999, 1);
     }


    @Test
    public void testReturnBookSuccess() {
        library.addBook("To Return", "Author C", 1); // ID 1
        library.addUser("Borrower", "789");
        library.issueBook(1, 1);

        // Return the book
        library.returnBook(1, 1);

        // Verify book state
        Optional<Book> bookOpt = library.findBookById(1);
        assertTrue(bookOpt.isPresent());
        assertEquals("Available copies should be 1 after returning.", 1, bookOpt.get().getAvailableCopies());
    }

     @Test
     public void testReturnBookNotIssued() {
         library.addBook("Never Issued", "Author D", 1); // ID 1
         library.addUser("User", "000");

         // Attempt to return a book that wasn't issued
         library.returnBook(1, 1);

         // Verify book state has not changed
         Optional<Book> bookOpt = library.findBookById(1);
         assertTrue(bookOpt.isPresent());
         assertEquals("Available copies should remain 1.", 1, bookOpt.get().getAvailableCopies());
     }

     @Test
      public void testReturnBookWrongUser() {
          library.addBook("Borrowed Book", "Author E", 1); // ID 1
          library.addUser("Borrower 1", "111");
          library.addUser("Borrower 2", "222");
          // Issue to User 1
          library.issueBook(1, 1);

          // User 2 attempts to return (should print error)
          library.returnBook(1, 2);

          // Verify book state has not changed
          Optional<Book> bookOpt = library.findBookById(1);
          assertTrue(bookOpt.isPresent());
          assertEquals("Available copies should still be 0.", 0, bookOpt.get().getAvailableCopies());
      }

     // TODO: Test persistence

 }