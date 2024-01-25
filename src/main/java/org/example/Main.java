package org.example;
import jakarta.persistence.*;
import org.hibernate.*;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

@Entity
@Table(name = "books")
class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "book_id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "genre")
    private String genre;

    @Column(name = "price")
    private Double price;

    @Column(name = "quantity_in_stock")
    private Integer quantityInStock;


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
    @Column(name = "customer_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone")
    private String phone;

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
    @Column(name = "sale_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Integer bookId;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Integer customerId;

    @Column(name = "date_of_sale")
    private Date dateOfSale;

    @Column(name = "quantity_sold")
    private Integer quantitySold;

    @Column(name = "total_price")
    private Double totalPrice;


    public Sale(Sale sale)
    {
        this.totalPrice = sale.totalPrice;
        this.dateOfSale = sale.dateOfSale;
        this.quantitySold = sale.quantitySold;
        this.customerId = sale.customerId;
        this.bookId = sale.bookId;
        this.id = sale.id;
    }
    public Sale(Long id, Integer bookId, Integer customerId, Date dateOfSale, Integer quantitySold, Double totalPrice) {
        this.id = id;
        this.bookId = bookId;
        this.customerId = customerId;
        this.dateOfSale = dateOfSale;
        this.quantitySold = quantitySold;
        this.totalPrice = totalPrice;
    }

    public Long getId() {
        return id;
    }

    public Integer getBookId() {
        return bookId;
    }

    public Integer getCustomerId() {
        return customerId;
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

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
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

            Book newBook = session.get(Book.class, book.getId());
//            book.setTitle(book.getTitle());
//            book.setAuthor(book.getAuthor());
//            book.setGenre(book.getGenre());
//            book.setPrice(book.getPrice());
//            book.setQuantityInStock(book.getQuantityInStock());
            session.merge(newBook);
            transaction.commit();
        }
    }
    public static List<Book> getBooksByGenre(String genre) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query query = session.createQuery("FROM Book WHERE genre = :genre", Book.class);
            query.setParameter("genre", genre);
            return query.getResultList();
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

            Customer newCustomer = session.get(Customer.class, customer.getId());
//            customer.setName(customer.getName());
//            customer.setEmail(customer.getEmail());
//            customer.setPhone(customer.getPhone());
            session.merge(newCustomer);
            transaction.commit();
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

            Book book = session.get(Book.class, sale.getBookId());
            Customer customer = session.get(Customer.class, sale.getCustomerId());

            Sale newSale = new Sale(sale);
//            sale.setBook(book);
//            sale.setCustomer(customer);
//            sale.setDateOfSale(dateOfSale);
//            sale.setQuantitySold(quantitySold);
//            sale.setTotalPrice(totalPrice);

            session.merge(sale);
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
                System.out.println("Book ID: " + sale.getBookId() + ", Quantity Sold: " + sale.getQuantitySold() +
                        ", Total Price: " + sale.getTotalPrice() + ", Date of Sale: " + sale.getDateOfSale());
            }
        }
    }

    private static void process_new_sale(Scanner scanner) {
        System.out.println("Enter the Book ID for the new sale:");
        Integer bookId = scanner.nextInt();

        System.out.println("Enter the Customer ID for the new sale:");
        Integer customerId = scanner.nextInt();

        System.out.println("Enter the quantity sold:");
        Integer quantitySold = scanner.nextInt();

        System.out.println("Enter the total price:");
        Double totalPrice = scanner.nextDouble();

        Sale newSale = new Sale(null, bookId, customerId, new Date(), quantitySold, totalPrice);
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
