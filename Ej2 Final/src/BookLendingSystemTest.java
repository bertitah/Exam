import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Arrays;

class BookLendingSystemTest {
    private Library library;
    private Book book1;
    private Book book2;
    private Book book3;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        library = new Library();
        book1 = new Book("Book 1", Arrays.asList("Author 1"), "ISBN-001", "Adventures", 10);
        book2 = new Book("Book 2", Arrays.asList("Author 2"), "ISBN-002", "Science Fiction", 12);
        book3 = new Book("Book 3", Arrays.asList("Author 3"), "ISBN-003", "Romance", 15);
        user1 = new User("John", "Doe", "Smith", "1990-01-01", "ID-001");
        user2 = new User("Jane", "Smith", "Johnson", "1995-05-05", "ID-002");
    }

    @Test
    void testRegisterBook() {
        library.registerBook(book1);
        library.registerBook(book2);
        library.registerBook(book3);

        List<Book> books = library.listAvailableBooks();
        Assertions.assertEquals(3, books.size());
        Assertions.assertTrue(books.contains(book1));
        Assertions.assertTrue(books.contains(book2));
        Assertions.assertTrue(books.contains(book3));
    }

    @Test
    void testDeregisterBook() {
        library.registerBook(book1);
        library.registerBook(book2);
        library.registerBook(book3);

        library.deregisterBook(book2);

        List<Book> books = library.listAvailableBooks();
        Assertions.assertEquals(2, books.size());
        Assertions.assertTrue(books.contains(book1));
        Assertions.assertFalse(books.contains(book2));
        Assertions.assertTrue(books.contains(book3));
    }

    @Test
    void testRegisterUser() {
        library.registerUser(user1);
        library.registerUser(user2);

        List<User> users = library.getUsers();
        Assertions.assertEquals(2, users.size());
        Assertions.assertTrue(users.contains(user1));
        Assertions.assertTrue(users.contains(user2));
    }

    @Test
    void testDeregisterUser() {
        library.registerUser(user1);
        library.registerUser(user2);

        library.deregisterUser(user2);

        List<User> users = library.getUsers();
        Assertions.assertEquals(1, users.size());
        Assertions.assertTrue(users.contains(user1));
        Assertions.assertFalse(users.contains(user2));
    }

    @Test
    void testLendBook() throws BookLendingException {
        library.registerBook(book1);
        library.registerUser(user1);

        library.lendBook(book1, user1);

        Assertions.assertTrue(book1.isCheckedOut());
        Assertions.assertEquals(user1, book1.getBorrower());
    }

    @Test
    void testLendBookInvalidUserAge() {
        library.registerBook(book1);
        library.registerUser(user2);

        Assertions.assertThrows(BookLendingException.class, () -> {
            library.lendBook(book1, user2);
        });
    }

    @Test
    void testLendBookAlreadyCheckedOut() throws BookLendingException {
        library.registerBook(book1);
        library.registerUser(user1);

        library.lendBook(book1, user1);

        Assertions.assertThrows(BookLendingException.class, () -> {
            library.lendBook(book1, user2);
        });
    }

    @Test
    void testLendBookNotSuitableForUserAge() {
        library.registerBook(book1);
        library.registerUser(user1);

        Assertions.assertThrows(BookLendingException.class, () -> {
            library.lendBook(book1, user1);
        });
    }

    @Test
    void testReturnBook() throws BookLendingException {
        library.registerBook(book1);
        library.registerUser(user1);

        library.lendBook(book1, user1);
        library.returnBook(book1);

        Assertions.assertFalse(book1.isCheckedOut());
        Assertions.assertNull(book1.getBorrower());
    }

    @Test
    void testReturnBookNotCheckedOut() {
        library.registerBook(book1);

        Assertions.assertThrows(BookLendingException.class, () -> {
            library.returnBook(book1);
        });
    }

    @Test
    void testListBooksByTitle() {
        library.registerBook(book2);
        library.registerBook(book1);
        library.registerBook(book3);

        List<Book> booksByTitle = library.listBooksByTitle();

        Assertions.assertEquals(3, booksByTitle.size());
        Assertions.assertEquals(book1, booksByTitle.get(0));
        Assertions.assertEquals(book2, booksByTitle.get(1));
        Assertions.assertEquals(book3, booksByTitle.get(2));
    }

    @Test
    void testListBooksByCategory() {
        library.registerBook(book1);
        library.registerBook(book2);
        library.registerBook(book3);

        List<Book> booksByCategory = library.listBooksByCategory("Science Fiction");

        Assertions.assertEquals(1, booksByCategory.size());
        Assertions.assertEquals(book2, booksByCategory.get(0));
    }

    @Test
    void testListCheckedOutBooks() throws BookLendingException {
        library.registerBook(book1);
        library.registerBook(book2);
        library.registerUser(user1);

        library.lendBook(book1, user1);

        List<Book> checkedOutBooks = library.listCheckedOutBooks();

        Assertions.assertEquals(1, checkedOutBooks.size());
        Assertions.assertEquals(book1, checkedOutBooks.get(0));
    }

    @Test
    void testListAvailableBooks() throws BookLendingException {
        library.registerBook(book1);
        library.registerBook(book2);
        library.registerUser(user1);

        library.lendBook(book1, user1);

        List<Book> availableBooks = library.listAvailableBooks();

        Assertions.assertEquals(1, availableBooks.size());
        Assertions.assertEquals(book2, availableBooks.get(0));
    }

    @Test
    void testListBooksByUser() throws BookLendingException {
        library.registerBook(book1);
        library.registerBook(book2);
        library.registerBook(book3);
        library.registerUser(user1);
        library.registerUser(user2);

        library.lendBook(book1, user1);
        library.lendBook(book2, user1);
        library.lendBook(book3, user2);

        List<Book> user1Books = library.listBooksByUser(user1);
        List<Book> user2Books = library.listBooksByUser(user2);

        Assertions.assertEquals(2, user1Books.size());
        Assertions.assertEquals(book1, user1Books.get(0));
        Assertions.assertEquals(book2, user1Books.get(1));

        Assertions.assertEquals(1, user2Books.size());
        Assertions.assertEquals(book3, user2Books.get(0));
    }
}

