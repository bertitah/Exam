import java.io.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;


// Custom exception for book lending system
class BookLendingException extends Exception {
    public BookLendingException(String message) {
        super(message);
    }
}

// Book class
class Book {
    private String title;
    private List<String> authors;
    private String identifier;
    private String category;
    private int recommendedAge;
    private boolean isCheckedOut;
    private User borrower;

    public Book(String title, List<String> authors, String identifier, String category, int recommendedAge) {
        this.title = title;
        this.authors = authors;
        this.identifier = identifier;
        this.category = category;
        this.recommendedAge = recommendedAge;
        this.isCheckedOut = false;
        this.borrower = null;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getCategory() {
        return category;
    }

    public int getRecommendedAge() {
        return recommendedAge;
    }

    public boolean isCheckedOut() {
        return isCheckedOut;
    }

    public User getBorrower() {
        return borrower;
    }

    public void setCheckedOut(boolean checkedOut) {
        isCheckedOut = checkedOut;
    }

    public void setBorrower(User borrower) {
        this.borrower = borrower;
    }
}

// User class
class User {
    private String firstName;
    private String lastName1;
    private String lastName2;
    private LocalDate dateOfBirth;
    private String id;

    public User(String firstName, String lastName1, String lastName2, String dateOfBirth, String id) {
        this.firstName = firstName;
        this.lastName1 = lastName1;
        this.lastName2 = lastName2;
        this.dateOfBirth = LocalDate.parse(dateOfBirth);
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName1() {
        return lastName1;
    }

    public String getLastName2() {
        return lastName2;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getId() {
        return id;
    }

    public int getAge() {
        LocalDate currentDate = LocalDate.now();
        return Period.between(dateOfBirth, currentDate).getYears();
    }
}

// Library class
class Library implements Serializable{
    private List<Book> books;
    private List<User> users;

    public Library() {
        this.books = new ArrayList<>();
        this.users = new ArrayList<>();
    }
    
    public List<User> getUsers() {
        return users;
    }

    public void registerBook(Book book) {
        books.add(book);
    }

    public void deregisterBook(Book book) {
        books.remove(book);
    }

    public void registerUser(User user) {
        users.add(user);
    }

    public void deregisterUser(User user) {
        users.remove(user);
    }

    public void lendBook(Book book, User user) throws BookLendingException {
        if (user.getAge() <= 8) {
            throw new BookLendingException("Users younger than 8 years are not allowed to borrow books.");
        }

        if (book.isCheckedOut()) {
            throw new BookLendingException("The book is already checked out.");
        }

        if (book.getRecommendedAge() > user.getAge()) {
            throw new BookLendingException("The book is not suitable for the user's age.");
        }

        book.setCheckedOut(true);
        book.setBorrower(user);
    }

    public void returnBook(Book book) throws BookLendingException {
        if (!book.isCheckedOut()) {
            throw new BookLendingException("The book is not checked out.");
        }

        book.setCheckedOut(false);
        book.setBorrower(null);
    }

    public List<Book> listBooksByTitle() {
        // Sort books by title
        List<Book> sortedBooks = new ArrayList<>(books);
        sortedBooks.sort((b1, b2) -> b1.getTitle().compareToIgnoreCase(b2.getTitle()));
        return sortedBooks;
    }

    public List<Book> listBooksByCategory(String category) {
        // Filter books by category
        List<Book> filteredBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.getCategory().equalsIgnoreCase(category)) {
                filteredBooks.add(book);
            }
        }
        return filteredBooks;
    }

    public List<Book> listCheckedOutBooks() {
        // Filter checked out books
        List<Book> checkedOutBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.isCheckedOut()) {
                checkedOutBooks.add(book);
            }
        }
        return checkedOutBooks;
    }

    public List<Book> listAvailableBooks() {
        // Filter available books
        List<Book> availableBooks = new ArrayList<>();
        for (Book book : books) {
            if (!book.isCheckedOut()) {
                availableBooks.add(book);
            }
        }
        return availableBooks;
    }

    public List<Book> listBooksByUser(User user) {
        // Filter books by user
        List<Book> userBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.isCheckedOut() && book.getBorrower().equals(user)) {
                userBooks.add(book);
            }
        }
        return userBooks;
    }

    public void storeLibraryState(String filename) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filename))) {
            outputStream.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Library loadLibraryState(String filename) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filename))) {
            return (Library) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}

public class BookLendingSystem {
    public static void main(String[] args) {
        // Create a library
        Library library = new Library();

        // Register books
        Book book1 = new Book("Book 1", Arrays.asList("Author 1"), "ISBN-001", "Adventures", 10);
        Book book2 = new Book("Book 2", Arrays.asList("Author 2"), "ISBN-002", "Science Fiction", 12);
        Book book3 = new Book("Book 3", Arrays.asList("Author 3"), "ISBN-003", "Romance", 15);
        Book book4 = new Book("Book 4", Arrays.asList("Author 4"), "ISBN-004", "History", 18);
        Book book5 = new Book("Book 5", Arrays.asList("Author 5"), "ISBN-005", "Art", 8);
        library.registerBook(book1);
        library.registerBook(book2);
        library.registerBook(book3);
        library.registerBook(book4);
        library.registerBook(book5);

        // Register users
        User user1 = new User("John", "Doe", "Smith", "1990-01-01", "ID-001");
        User user2 = new User("Jane", "Smith", "Johnson", "1995-05-05", "ID-002");
        library.registerUser(user1);
        library.registerUser(user2);

        // Lend books
        try {
            library.lendBook(book1, user1);
            library.lendBook(book2, user2);
        } catch (BookLendingException e) {
            System.out.println("Book lending failed: " + e.getMessage());
        }

        // Return books
        try {
            library.returnBook(book1);
        } catch (BookLendingException e) {
            System.out.println("Book return failed: " + e.getMessage());
        }

        // List books
        System.out.println("Books by title:");
        List<Book> booksByTitle = library.listBooksByTitle();
        for (Book book : booksByTitle) {
            System.out.println(book.getTitle());
        }

        System.out.println("\nBooks by category (Science Fiction):");
        List<Book> booksByCategory = library.listBooksByCategory("Science Fiction");
        for (Book book : booksByCategory) {
            System.out.println(book.getTitle());
        }

        System.out.println("\nChecked out books:");
        List<Book> checkedOutBooks = library.listCheckedOutBooks();
        for (Book book : checkedOutBooks) {
            System.out.println(book.getTitle());
        }

        System.out.println("\nAvailable books:");
        List<Book> availableBooks = library.listAvailableBooks();
        for (Book book : availableBooks) {
            System.out.println(book.getTitle());
        }

        System.out.println("\nBooks by user (John Doe Smith):");
        List<Book> userBooks = library.listBooksByUser(user1);
        for (Book book : userBooks) {
            System.out.println(book.getTitle());
        }

        // Store library state
        library.storeLibraryState("library_state.ser");

        // Load library state
        Library loadedLibrary = Library.loadLibraryState("library_state.ser");
        if (loadedLibrary != null) {
            System.out.println("\nLoaded library state:");
            List<Book> loadedBooksByTitle = loadedLibrary.listBooksByTitle();
            for (Book book : loadedBooksByTitle) {
                System.out.println(book.getTitle());
            }
        }
    }
}
