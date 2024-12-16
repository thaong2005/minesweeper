package LibraryManager;
import java.util.Scanner;
import java.util.function.Consumer;

class Book {
    String isbn;    // primary key
    String title;
    String author;
    boolean borrowed;   // Available status
    Book left, right;

    public Book(String isbn, String title, String author) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.borrowed = false;
    }

    @Override
    public String toString() {
        return "ISBN: " + isbn + ", Title: " + title
                + ", Author: " + author + ", Available: " + (borrowed ? "Yes" : "No");
    }
}

class Library {

    private Book root;

    public void addBook(String isbn, String title, String author) {
        root = addBook(root, isbn, title, author);
    }

    private Book addBook(Book node, String isbn, String title, String author) {
        if (node == null) {
            return new Book(isbn, title, author);
        }
        if (isbn.compareTo(node.isbn) < 0) {
            node.left = addBook(node.left, isbn, title, author);
        } else if (isbn.compareTo(node.isbn) > 0) {
            node.right = addBook(node.right, isbn, title, author);
        }
        return node;
    }

    public Book searchBook(String isbn) {
        return searchBook(root, isbn);
    }

    private Book searchBook(Book node, String isbn) {
        if (node == null || node.isbn.equals(isbn)) {
            return node;
        }
        if (isbn.compareTo(node.isbn) < 0) {
            return searchBook(node.left, isbn);
        }
        return searchBook(node.right, isbn);
    }

    public void deleteBook(String isbn) {
        root = deleteBook(root, isbn);
    }



    private Book deleteBook(Book node, String isbn) {
        if (node == null) {
            return null;
        }
        if (isbn.compareTo(node.isbn) < 0) {
            node.left = deleteBook(node.left, isbn);
        } else if (isbn.compareTo(node.isbn) > 0) {
            node.right = deleteBook(node.right, isbn);
        } else {
            if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            }
            Book minNode = findMin(node.right);
            node.isbn = minNode.isbn;
            node.title = minNode.title;
            node.author = minNode.author;
            node.right = deleteBook(node.right, minNode.isbn);
        }
        return node;
    }



    
    private Book findMin(Book node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    public void displayBooksInOrder(Consumer<Book> action) {
        inorderTraversal(root, action);
    }

    private void inorderTraversal(Book node, Consumer<Book> action) {
        if (node != null) {
            inorderTraversal(node.left, action);
            action.accept(node);
            inorderTraversal(node.right, action);
        }
    }

    public int countBooks() {
        return countNodes(root);
    }

    private int countNodes(Book node) {
        if (node == null) {
            return 0;
        }
        return 1 + countNodes(node.left) + countNodes(node.right);
    }

    public boolean borrowBook(String isbn) {
        Book book = searchBook(isbn);
        if (book == null) {
            return false;
        }
        if (book.borrowed) {
            return false;
        }
        book.borrowed = true;
        return true;
    }

    public boolean returnBook(String isbn) {
        Book book = searchBook(isbn);
        if (book == null) {
            return false;
        }
        if (!book.borrowed) {
            return false;
        }
        book.borrowed = false;
        return true;
    }


}

