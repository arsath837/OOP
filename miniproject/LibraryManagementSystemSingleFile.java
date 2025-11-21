package com.miniproject;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Iterator;

class Book {
    public int sNo;
    public String bookName;
    public String authorName;
    public int totalQuantity;
    public int availableQuantity;

    public Book(int sNo, String bookName, String authorName, int quantity) {
        this.sNo = sNo;
        this.bookName = bookName;
        this.authorName = authorName;
        this.totalQuantity = quantity;
        this.availableQuantity = quantity;
    }
}

class LibraryMember {
    public String name;
    public int regNum;
    public int booksIssuedCount = 0;

    public LibraryMember(String name, int regNum) {
        this.name = name;
        this.regNum = regNum;
    }
}

public class LibraryManagementSystemSingleFile {
    private ArrayList<Book> books = new ArrayList<>();
    private ArrayList<LibraryMember> members = new ArrayList<>();
    private Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        LibraryManagementSystemSingleFile library = new LibraryManagementSystemSingleFile();
        library.runMenu();
    }

    public void runMenu() {
        while (true) {
            System.out.println("\n*** Library Management System Menu ***");
            System.out.println("1. Add New Book");
            System.out.println("2. Add New Member");
            System.out.println("3. Show All Books");
            System.out.println("4. Show All Members");
            System.out.println("5. Issue a Book");
            System.out.println("6. Return a Book");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            int choice = 0;
            try {
                choice = input.nextInt();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number (1-7).");
                input.nextLine(); // Clear the buffer
                continue;
            }

            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    addMember();
                    break;
                case 3:
                    showAllBooks();
                    break;
                case 4:
                    showAllMembers();
                    break;
                case 5:
                    issueBook();
                    break;
                case 6:
                    returnBook();
                    break;
                case 7:
                    System.out.println("Exiting System.");
                    input.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addBook() {
        System.out.println("\nEnter Serial No of Book:");
        int sNo = input.nextInt();
        input.nextLine();
        System.out.println("Enter Book Name:");
        String name = input.nextLine();
        System.out.println("Enter Author Name:");
        String author = input.nextLine();
        System.out.println("Enter Quantity of Books:");
        int qty = input.nextInt();

        books.add(new Book(sNo, name, author, qty));
        System.out.println("Book added successfully!");
    }

    private void addMember() {
        input.nextLine();
        System.out.println("\nEnter Member Name:");
        String name = input.nextLine();
        System.out.println("Enter Registration Number:");
        int regNum = input.nextInt();

        members.add(new LibraryMember(name, regNum));
        System.out.println("Member added successfully!");
    }

    private void showAllBooks() {
        System.out.println("\n--- Library Book Collection ---");
        System.out.printf("%-10s %-20s %-20s %-15s %-10s\n", "S.No", "Name", "Author", "Available Qty", "Total Qty");
        for (Book book : books) {
            System.out.printf("%-10d %-20s %-20s %-15d %-10d\n",
                    book.sNo, book.bookName, book.authorName, book.availableQuantity, book.totalQuantity);
        }
    }

    private void showAllMembers() {
        System.out.println("\n--- Registered Library Members ---");
        System.out.printf("%-20s %-15s %-15s\n", "Name", "Reg. Number", "Books Issued");
        for (LibraryMember member : members) {
            System.out.printf("%-20s %-15d %-15d\n",
                    member.name, member.regNum, member.booksIssuedCount);
        }
    }

    private void issueBook() {
        System.out.println("\nEnter Book Serial No to issue:");
        int bookSNo = input.nextInt();
        System.out.println("Enter Member Registration No:");
        int memberRegNum = input.nextInt();

        Book book = findBook(bookSNo);
        LibraryMember member = findMember(memberRegNum);

        if (book != null && member != null) {
            if (book.availableQuantity > 0) {
                book.availableQuantity--;
                member.booksIssuedCount++;
                System.out.println("Book issued successfully to " + member.name + "!");
            } else {
                System.out.println("Sorry, the book is currently out of stock.");
            }
        } else {
            System.out.println("Error: Book or Member not found.");
        }
    }

    private void returnBook() {
        System.out.println("\nEnter Book Serial No to return:");
        int bookSNo = input.nextInt();
        System.out.println("Enter Member Registration No:");
        int memberRegNum = input.nextInt();

        Book book = findBook(bookSNo);
        LibraryMember member = findMember(memberRegNum);

        if (book != null && member != null) {
            if (member.booksIssuedCount > 0 && book.availableQuantity < book.totalQuantity) {
                book.availableQuantity++;
                member.booksIssuedCount--;
                System.out.println("Book returned successfully from " + member.name + "!");
            } else {
                System.out.println("Error: The book was not issued to this member or is already returned.");
            }
        } else {
            System.out.println("Error: Book or Member not found.");
        }
    }

    private Book findBook(int sNo) {
        for (Book book : books) {
            if (book.sNo == sNo) {
                return book;
            }
        }
        return null;
    }

    private LibraryMember findMember(int regNum) {
        for (LibraryMember member : members) {
            if (member.regNum == regNum) {
                return member;
            }
        }
        return null;
    }
}

