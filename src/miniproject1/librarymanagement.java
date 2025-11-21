package miniproject1;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

class Book {
    private String id;
    private String title;
    private String author;
    private boolean isIssued;

    public Book(String id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isIssued = false;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean isIssued() { return isIssued; }

    public void setIssued(boolean issued) {
        isIssued = issued;
    }

    @Override
    public String toString() {
        return "Book ID: " + id +
                ", Title: " + title +
                ", Author: " + author +
                ", Issued: " + (isIssued ? "Yes" : "No");
    }
}

class Member {
    private String memberId;
    private String name;
    private List<String> borrowedBookIds;

    public Member(String memberId, String name) {
        this.memberId = memberId;
        this.name = name;
        this.borrowedBookIds = new ArrayList<>();
    }

    public String getMemberId() { return memberId; }
    public String getName() { return name; }
    public List<String> getBorrowedBookIds() { return borrowedBookIds; }

    public void borrowBook(String bookId) {
        borrowedBookIds.add(bookId);
    }

    public void returnBook(String bookId) {
        borrowedBookIds.remove(bookId);
    }
}

class BookNotAvailableException extends Exception {
    public BookNotAvailableException(String message) {
        super(message);
    }
}

class InvalidReturnException extends Exception {
    public InvalidReturnException(String message) {
        super(message);
    }
}

class Library {
    private HashMap<String, Book> inventory = new HashMap<>();
    private HashMap<String, Member> members = new HashMap<>();

    public void addBook(Book book) {
        inventory.put(book.getId(), book);
        logOperation("Added Book: " + book.getId() + " - " + book.getTitle());
    }

    public void addMember(Member member) {
        members.put(member.getMemberId(), member);
        logOperation("Added Member: " + member.getMemberId() + " - " + member.getName());
    }

    public void issueBook(String bookId, String memberId) throws BookNotAvailableException {
        Book book = inventory.get(bookId);
        Member member = members.get(memberId);

        if (book == null)
            throw new BookNotAvailableException("Book not found!");
        if (member == null)
            throw new BookNotAvailableException("Member not found!");
        if (book.isIssued())
            throw new BookNotAvailableException("Book is already issued!");

        book.setIssued(true);
        member.borrowBook(bookId);

        logOperation("Issued Book: " + bookId + " to Member: " + memberId);
    }

    public void returnBook(String bookId, String memberId, int daysLate)
            throws InvalidReturnException {

        Book book = inventory.get(bookId);
        Member member = members.get(memberId);

        if (book == null || member == null)
            throw new InvalidReturnException("Invalid return details!");

        if (!member.getBorrowedBookIds().contains(bookId))
            throw new InvalidReturnException("This member did not borrow this book!");

        book.setIssued(false);
        member.returnBook(bookId);

        int lateFee = daysLate * 2; // ₹2/day
        logOperation("Returned Book: " + bookId + " from Member: " + memberId +
                ", Late Fee: ₹" + lateFee);

        System.out.println("Book returned successfully. Late Fee: ₹" + lateFee);
    }

    public void showInventory() {
        System.out.println("\n------ Library Inventory ------");
        for (Book book : inventory.values()) {
            System.out.println(book);
        }
    }

    public void logOperation(String message) {
        try (FileWriter fw = new FileWriter("library_log.txt", true)) {
            fw.write(message + "\n");
        } catch (IOException e) {
            System.out.println("Error logging operation.");
        }
    }
}

public class librarymanagement {

    public static void main(String[] args) {

        Library library = new Library();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Library Menu =====");
            System.out.println("1. Add Book");
            System.out.println("2. Add Member");
            System.out.println("3. Issue Book");
            System.out.println("4. Return Book");
            System.out.println("5. Show Inventory");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");

            int choice;

            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Enter a number.");
                continue;
            }

            try {
                switch (choice) {
                    case 1:
                        System.out.print("Book ID: ");
                        String id = sc.nextLine();

                        System.out.print("Title: ");
                        String title = sc.nextLine();

                        System.out.print("Author: ");
                        String author = sc.nextLine();

                        library.addBook(new Book(id, title, author));
                        System.out.println("Book added successfully!");
                        break;

                    case 2:
                        System.out.print("Member ID: ");
                        String mId = sc.nextLine();

                        System.out.print("Name: ");
                        String name = sc.nextLine();

                        library.addMember(new Member(mId, name));
                        System.out.println("Member added successfully!");
                        break;

                    case 3:
                        System.out.print("Book ID to issue: ");
                        String issueBookId = sc.nextLine();

                        System.out.print("Member ID: ");
                        String issueMemberId = sc.nextLine();

                        library.issueBook(issueBookId, issueMemberId);
                        System.out.println("Book issued successfully!");
                        break;

                    case 4:
                        System.out.print("Book ID to return: ");
                        String returnBookId = sc.nextLine();

                        System.out.print("Member ID: ");
                        String returnMemberId = sc.nextLine();

                        System.out.print("Days Late: ");
                        int daysLate = Integer.parseInt(sc.nextLine());

                        library.returnBook(returnBookId, returnMemberId, daysLate);
                        break;

                    case 5:
                        library.showInventory();
                        break;

                    case 6:
                        System.out.println("Thank you. Exiting...");
                        sc.close();
                        return;

                    default:
                        System.out.println("Invalid option! Try again.");
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
