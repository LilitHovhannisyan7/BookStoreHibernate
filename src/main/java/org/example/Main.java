package org.example;
import jakarta.persistence.*;
import org.hibernate.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

@Entity
@Table(name = "books")
class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "bookid")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "genre")
    private String genre;

    @Column(name = "price")
    private Double price;

    @Column(name = "quantityinstock")
    private Integer quantityInStock;

    public Book()
    {

    }

    public Book(Long id, String title, String author, String genre, Double price, Integer quantity_in_stock) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.price = price;
        this.quantityInStock = quantity_in_stock;
    }
    public Long getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public String getTitle() {
        return title;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getQuantityInStock() {
        return quantityInStock;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setQuantityInStock(Integer quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

@Entity
@Table(name = "customers")
class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "customerid")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone")
    private String phone;

    public Customer()
    {

    }
    public Customer(Long id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }
}


@Entity
@Table(name = "sales")
class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "saleid")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bookid", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "customerid", nullable = false)
    private Customer customer;

    @Column(name = "dateofsale")
    private Date dateOfSale;

    @Column(name = "quantitysold")
    private Integer quantitySold;

    @Column(name = "totalprice")
    private Double totalPrice;


    public Sale()
    {

    }
    public Sale(Sale sale)
    {
        this.totalPrice = sale.totalPrice;
        this.dateOfSale = sale.dateOfSale;
        this.quantitySold = sale.quantitySold;
        this.customer = sale.customer;
        this.book = sale.book;
        this.id = sale.id;
    }
    public Sale(Long id, Book book, Customer customer, Date dateOfSale, Integer quantitySold, Double totalPrice) {
        this.id = id;
        this.book = book;
        this.customer = customer;
        this.dateOfSale = dateOfSale;
        this.quantitySold = quantitySold;
        this.totalPrice = totalPrice;
    }

    public Long getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Date getDateOfSale() {
        return dateOfSale;
    }

    public Integer getQuantitySold() {
        return quantitySold;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setQuantitySold(Integer quantitySold) {
        this.quantitySold = quantitySold;
    }

    public void setDateOfSale(Date dateOfSale) {
        this.dateOfSale = dateOfSale;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

public class Main
{
    public static void updateBookDetails(Book book) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            // Get the existing book from the database
            Book existingBook = session.get(Book.class, book.getId());

            if (existingBook != null) {
                // Update the existingBook with the values from the input book
                existingBook.setTitle(book.getTitle());
                existingBook.setAuthor(book.getAuthor());
                existingBook.setGenre(book.getGenre());
                existingBook.setPrice(book.getPrice());
                existingBook.setQuantityInStock(book.getQuantityInStock());

                // Use merge to update the existingBook
                session.merge(existingBook);

                transaction.commit();
            } else {
                System.out.println("Book not found with ID: " + book.getId());
            }
        }

    }
    public static List<Book> getBooksByGenre(String genre) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            try {
                Query query = session.createQuery("FROM Book WHERE LOWER(genre) = LOWER(:genre)", Book.class);
                query.setParameter("genre", genre.toLowerCase());
                return query.getResultList();
            } catch (Exception e) {
                e.printStackTrace();
                return Collections.emptyList(); // Return an empty list in case of an exception
            }
        }
    }


    public static List<Book> getBooksByAuthor(String author) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query query = session.createQuery("FROM Book WHERE author = :author", Book.class);
            query.setParameter("author", author);
            return query.getResultList();
        }
    }

    public static void updateCustomerDetails(Customer customer) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            // Retrieve the existing customer from the database
            Customer existingCustomer = session.get(Customer.class, customer.getId());

            // Update the properties of the existing customer with the new values
            if(existingCustomer != null) {
                existingCustomer.setName(customer.getName());
                existingCustomer.setEmail(customer.getEmail());
                existingCustomer.setPhone(customer.getPhone());

                // Merge the updated customer back into the session
                session.merge(existingCustomer);


                // Commit the transaction
                transaction.commit();
            }
            else {
                System.out.println("Customer not found with ID: " + customer.getId());
            }
        }
    }

    public static List<Sale> getCustomerPurchaseHistory(Long customerId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query query = session.createQuery("FROM Sale WHERE customer.id = :customerId", Sale.class);
            query.setParameter("customerId", customerId);
            return query.getResultList();
        }
    }
    public static void processNewSale(Sale sale) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Book book = session.get(Book.class, sale.getBook().getId());
            Customer customer = session.get(Customer.class, sale.getCustomer().getId());

            Sale newSale = new Sale(sale);
            newSale.setBook(book);  // Set the Book entity
            newSale.setCustomer(customer);  // Set the Customer entity

            session.save(newSale);  // Use save instead of merge for new entities
            transaction.commit();
        }
    }


    public static Double calculateTotalRevenueByGenre(String genre) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query query = session.createQuery("SELECT SUM(s.totalPrice) FROM Sale s JOIN s.book b WHERE b.genre = :genre", Double.class);
            query.setParameter("genre", genre);
            return (Double) query.getSingleResult();
        }
    }


    public static List<Object[]> getBooksSoldReport() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query query = session.createQuery("SELECT b.title, c.name, s.dateOfSale FROM Sale s JOIN s.book b JOIN s.customer c", Object[].class);
            return query.getResultList();
        }
    }


    public static List<Object[]> getRevenueByGenreReport() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query query = session.createQuery("SELECT b.genre, SUM(s.totalPrice) FROM Sale s JOIN s.book b GROUP BY b.genre", Object[].class);
            return query.getResultList();
        }
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            printMenu();
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        update_book_details(scanner);
                        break;
                    case 2:
                        get_books_by_genre(scanner);
                        break;
                    case 3:
                        get_books_by_author(scanner);
                        break;
                    case 4:
                        update_customer_details(scanner);
                        break;
                    case 5:
                        get_customer_purchase_history(scanner);
                        break;
                    case 6:
                        process_new_sale(scanner);
                        break;
                    case 7:
                        calculate_total_revenue_by_genre(scanner);
                        break;
                    case 8:
                        get_books_sold_report();
                        break;
                    case 9:
                        get_revenue_by_genre_report();
                        break;
                    case 0:
                        System.out.println("Exiting the program");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid");
                        break;
                }
            }
        }

        private static void printMenu() {
            System.out.println("Menu");
            System.out.println("1. Update Book Details");
            System.out.println("2. Get Books by Genre");
            System.out.println("3. Get Books by Author");
            System.out.println("4. Update Customer Details");
            System.out.println("5. Get Customer Purchase History");
            System.out.println("6. Process New Sale");
            System.out.println("7. Calculate Total Revenue by Genre");
            System.out.println("8. Get Books Sold Report");
            System.out.println("9. Get Revenue by Genre Report");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
        }


    private static void update_book_details(Scanner scanner) {
        System.out.println("Enter the Book ID to update:");
        Long bookId = scanner.nextLong();

        System.out.println("Enter the new title:");
        String title = scanner.next();

        System.out.println("Enter the new author:");
        String author = scanner.next();

        System.out.println("Enter the new genre:");
        String genre = scanner.next();

        System.out.println("Enter the new price:");
        Double price = scanner.nextDouble();

        System.out.println("Enter the new quantity in stock:");
        Integer quantityInStock = scanner.nextInt();

        Book updatedBook = new Book(bookId, title, author, genre, price, quantityInStock);
        updateBookDetails(updatedBook);
        System.out.println("Book details updated successfully.");
    }

    private static void get_books_by_genre(Scanner scanner) {
        System.out.println("Enter the genre to search for:");
        String genre = scanner.next();

        List<Book> books = getBooksByGenre(genre);

        if (books.isEmpty()) {
            System.out.println("No books found for the given genre.");
        } else {
            System.out.println("Books found for the given genre:");
            for (Book book : books) {
                System.out.println(book.getTitle() + " by " + book.getAuthor());
            }
        }
    }

    private static void get_books_by_author(Scanner scanner) {
        System.out.println("Enter the author to search for:");
        String author = scanner.next();

        List<Book> books = getBooksByAuthor(author);

        if (books.isEmpty()) {
            System.out.println("No books found for the given author.");
        } else {
            System.out.println("Books found for the given author:");
            for (Book book : books) {
                System.out.println(book.getTitle() + " by " + book.getAuthor());
            }
        }
    }

    private static void update_customer_details(Scanner scanner) {
        System.out.println("Enter the Customer ID to update:");
        Long customerId = scanner.nextLong();

        System.out.println("Enter the new name:");
        String name = scanner.next();

        System.out.println("Enter the new email:");
        String email = scanner.next();

        System.out.println("Enter the new phone number:");
        String phone = scanner.next();

        Customer updatedCustomer = new Customer(customerId, name, email, phone);
        updateCustomerDetails(updatedCustomer);
        System.out.println("Customer details updated successfully.");
    }

    private static void get_customer_purchase_history(Scanner scanner) {
        System.out.println("Enter the Customer ID to retrieve purchase history:");
        Long customerId = scanner.nextLong();

        List<Sale> purchaseHistory = getCustomerPurchaseHistory(customerId);

        if (purchaseHistory.isEmpty()) {
            System.out.println("No purchase history found for the given customer.");
        } else {
            System.out.println("Purchase history for the given customer:");
            for (Sale sale : purchaseHistory) {
                System.out.println("Book ID: " + sale.getBook() + ", Quantity Sold: " + sale.getQuantitySold() +
                        ", Total Price: " + sale.getTotalPrice() + ", Date of Sale: " + sale.getDateOfSale());
            }
        }
    }

    private static void process_new_sale(Scanner scanner) {
        System.out.println("Enter the Book ID for the new sale:");
        Long bookId = scanner.nextLong();

        System.out.println("Enter the Customer ID for the new sale:");
        Long customerId = scanner.nextLong();

        System.out.println("Enter the quantity sold:");
        Integer quantitySold = scanner.nextInt();

        System.out.println("Enter the total price:");
        Double totalPrice = scanner.nextDouble();

        // Retrieve the Book and Customer entities from the database
        Book book = HibernateUtil.getSessionFactory().openSession().get(Book.class, bookId);
        Customer customer = HibernateUtil.getSessionFactory().openSession().get(Customer.class, customerId);

        // Create a new Sale instance
        Sale newSale = new Sale(null, book, customer, new Date(), quantitySold, totalPrice);

        // Process the new sale
        processNewSale(newSale);
        System.out.println("New sale processed successfully.");
    }


    private static void calculate_total_revenue_by_genre(Scanner scanner) {
        System.out.println("Enter the genre to calculate total revenue:");
        String genre = scanner.next();

        Double totalRevenue = calculateTotalRevenueByGenre(genre);
        System.out.println("Total revenue for the given genre: " + totalRevenue);
    }

    private static void get_books_sold_report() {
        List<Object[]> booksSoldReport = getBooksSoldReport();

        if (booksSoldReport.isEmpty()) {
            System.out.println("No books sold report available.");
        } else {
            System.out.println("Books Sold Report:");
            for (Object[] report : booksSoldReport) {
                System.out.println("Book Title: " + report[0] + ", Customer Name: " + report[1] +
                        ", Date of Sale: " + report[2]);
            }
        }
    }

    private static void get_revenue_by_genre_report() {
        List<Object[]> revenueByGenreReport = getRevenueByGenreReport();

        if (revenueByGenreReport.isEmpty()) {
            System.out.println("No revenue by genre report available.");
        } else {
            System.out.println("Revenue by Genre Report:");
            for (Object[] report : revenueByGenreReport) {
                System.out.println("Genre: " + report[0] + ", Total Revenue: " + report[1]);
            }
        }
    }



}
