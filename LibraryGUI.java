package LibraryManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;

public class LibraryGUI extends JFrame {
    private Library library;
    private JTextField isbnField, titleField, authorField, searchField;
    private JTable bookTable;
    private DefaultTableModel tableModel;

    public LibraryGUI() {
        library = new Library();
        library.addBook("012", "Introduction to Algorithms", "Thomas H. Cormen");
        library.addBook("123", "Cracking the Coding Interview", "Gayle Laakmann McDowell");
        library.addBook("345", "Design Patterns", "Erich Gamma");
        library.addBook("098", "Clean Code", "Robert C. Martin");
        library.addBook("901", "Head First Java", "Kathy Sierra");
        library.addBook("234", "Effective Java", "Joshua Bloch");
        library.addBook("557", "Java Concurrency in Practice", "Brian Goetz");
        library.addBook("840", "Java: The Complete Reference", "Herbert Schildt");
        library.addBook("103", "Thinking in Java", "Bruce Eckel");
        library.addBook("456", "Java: A Beginner's Guide", "Herbert Schildt");
        library.addBook("789", "Java: The Good Parts", "Douglas Crockford");
        library.addBook("082", "How to Cook", "Jenny Dale");

        initializeUI();
        displayBooks();
    }

    private void initializeUI() {
        setTitle("Library Management System");
        setSize(700, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Book Information"));

        inputPanel.add(new JLabel("ISBN:"));
        isbnField = new JTextField();
        inputPanel.add(isbnField);

        inputPanel.add(new JLabel("Title:"));
        titleField = new JTextField();
        inputPanel.add(titleField);

        inputPanel.add(new JLabel("Author:"));
        authorField = new JTextField();
        inputPanel.add(authorField);

        JButton addButton = new JButton("Add Book");
        addButton.addActionListener(e -> addBook());
        inputPanel.add(addButton);

        JButton updateButton = new JButton("Update Book");
        updateButton.addActionListener(e -> updateBook());
        inputPanel.add(updateButton);

        add(inputPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"ISBN", "Title", "Author", "Borrowed"}, 0);
        bookTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Library Books"));
        add(scrollPane, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        actionPanel.setBorder(BorderFactory.createTitledBorder("Actions"));

        searchField = new JTextField();
        actionPanel.add(new JLabel("Enter the ISBN:"));
        actionPanel.add(searchField);

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchBook());
        actionPanel.add(searchButton);

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteBook());
        actionPanel.add(deleteButton);

        JButton borrowButton = new JButton("Borrow");
        borrowButton.addActionListener(e -> borrowBook());
        actionPanel.add(borrowButton);

        JButton returnButton = new JButton("Return");
        returnButton.addActionListener(e -> returnBook());
        actionPanel.add(returnButton);

        JButton saveButton = new JButton("Save to File");
        saveButton.addActionListener(e -> saveToFile());
        actionPanel.add(saveButton);

        JButton loadButton = new JButton("Load from File");
        loadButton.addActionListener(e -> loadFromFile());
        actionPanel.add(loadButton);

        JButton countButton = new JButton("Count Books");
        countButton.addActionListener(e -> countBooks());
        actionPanel.add(countButton);

        add(actionPanel, BorderLayout.SOUTH);
    }

    private void addBook() {
        String isbn = isbnField.getText().trim();
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();

        if (isbn.isEmpty() || title.isEmpty() || author.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        library.addBook(isbn, title, author);
        displayBooks();
        clearFields();
        JOptionPane.showMessageDialog(this, "Book added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateBook() {
        String isbn = isbnField.getText().trim();
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();

        if (isbn.isEmpty() || title.isEmpty() || author.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Book book = library.searchBook(isbn);
        if (book != null) {
            book.title = title;
            book.author = author;
            displayBooks();
            clearFields();
            JOptionPane.showMessageDialog(this, "Book updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Book not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchBook() {
        String isbn = searchField.getText().trim();
        if (isbn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an ISBN to search.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Book book = library.searchBook(isbn);
        if (book != null) {
            JOptionPane.showMessageDialog(this, "Book found:\n" + book, "Search Result", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Book not found.", "Search Result", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteBook() {
        String isbn = searchField.getText().trim();
        if (isbn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an ISBN to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        library.deleteBook(isbn);
        displayBooks();
        JOptionPane.showMessageDialog(this, "Book deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void borrowBook() {
        String isbn = searchField.getText().trim();
        if (isbn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an ISBN to borrow.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (library.borrowBook(isbn)) {
            displayBooks();
            JOptionPane.showMessageDialog(this, "Book borrowed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Book not found or already borrowed.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void returnBook() {
        String isbn = searchField.getText().trim();
        if (isbn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an ISBN to return.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (library.returnBook(isbn)) {
            displayBooks();
            JOptionPane.showMessageDialog(this, "Book returned successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Book not found or not borrowed.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveToFile() {
        try (FileWriter writer = new FileWriter("library.txt")) {
            library.displayBooksInOrder(book -> {
                try {
                    writer.write(book + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            JOptionPane.showMessageDialog(this, "Library saved to file.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving to file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("library.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length == 4) {
                    String isbn = parts[0].split(": ")[1];
                    String title = parts[1].split(": ")[1];
                    String author = parts[2].split(": ")[1];
                    boolean borrowed = parts[3].split(": ")[1].equals("Yes");
                    Book book = new Book(isbn, title, author);
                    book.borrowed = borrowed;
                    library.addBook(book.isbn, book.title, book.author);
                }
            }
            displayBooks();
            JOptionPane.showMessageDialog(this, "Library loaded from file.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading from file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void countBooks() {
        int count = library.countBooks();
        JOptionPane.showMessageDialog(this, "Total books in library: " + count, "Count Books", JOptionPane.INFORMATION_MESSAGE);
    }

    private void displayBooks() {
        tableModel.setRowCount(0); // Clear existing rows
        library.displayBooksInOrder(book -> tableModel.addRow(new Object[]{book.isbn, book.title, book.author, book.borrowed ? "Yes" : "No"}));
    }

    private void clearFields() {
        isbnField.setText("");
        titleField.setText("");
        authorField.setText("");
        searchField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LibraryGUI gui = new LibraryGUI();
            gui.setVisible(true);
        });
    }
}