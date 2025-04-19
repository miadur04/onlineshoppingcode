import java.util.*;

class Product {
    int id;
    String name;
    double price;
    int stock;

    Product(int id, String name, double price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public String toString() {
        return id + ". " + name + " - $" + price + " (" + stock + " in stock)";
    }
}

class User {
    String username;
    String password;
    List<Product> cart = new ArrayList<>();

    User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    void viewProducts(List<Product> products) {
        System.out.println("\nAvailable Products:");
        for (Product p : products) {
            System.out.println(p);
        }
    }

    void addToCart(Product p, Scanner sc) {
        System.out.print("Enter quantity: ");
        int quantity = sc.nextInt();
    
        if (quantity <= 0) {
            System.out.println("Quantity must be greater than 0.");
            return;
        }
    
        if (quantity > p.stock) {
            System.out.println("Not enough stock available. Only " + p.stock + " items available.");
        } else {
            // Add the product to the cart 'quantity' times
            for (int i = 0; i < quantity; i++) {
                cart.add(p);
            }
            p.stock -= quantity;  // Reduce stock accordingly
            System.out.println(quantity + " " + p.name + "(s) added to cart.");
        }
    }

    void viewCart() {
        System.out.println("\nYour Cart:");
        if (cart.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }
        double total = 0;
        for (Product p : cart) {
            System.out.println(p.name + " - $" + p.price);
            total += p.price;
        }
        System.out.println("Total: $" + total);
    }

    void purchase() {
        if (cart.isEmpty()) {
            System.out.println("Cart is empty. Add items to purchase.");
            return;
        }
        for (Product p : cart) {
            p.stock--;
        }
        System.out.println("Purchase successful! Thank you, " + username + "!");
        cart.clear();
    }
}

class Admin {
    private final String adminUser = "miad";
    private final String adminPass = "miad4";

    boolean login(String user, String pass) {
        return adminUser.equals(user) && adminPass.equals(pass);
    }

    void addProduct(List<Product> products, Scanner sc) {
        System.out.print("Enter product name: ");
        String name = sc.next();
        
        // Handling price input
        double price = 0;
        while (true) {
            try {
                System.out.print("Enter price: ");
                price = sc.nextDouble();
                break;  // Exit loop if valid input
            } catch (InputMismatchException e) {
                System.out.println("Invalid price. Please enter a valid number.");
                sc.next();  // Clear the buffer
            }
        }
        
        // Handling stock input
        int stock = 0;
        while (true) {
            try {
                System.out.print("Enter stock: ");
                stock = sc.nextInt();
                break;  // Exit loop if valid input
            } catch (InputMismatchException e) {
                System.out.println("Invalid stock. Please enter a valid integer.");
                sc.next();  // Clear the buffer
            }
        }
    
        int id = products.size() + 1;  // Auto increment ID
        products.add(new Product(id, name, price, stock));
        System.out.println("Product added.");
        
        // Optional: Display the updated product list
        System.out.println("Current Product List: ");
        for (Product p : products) {
            System.out.println(p);
        }
    }

    void viewProducts(List<Product> products) {
        System.out.println("\nAll Products:");
        for (Product p : products) {
            System.out.println(p);
        }
    }

    void viewUsers(List<User> users) {
        System.out.println("\nRegistered Users:");
        for (User u : users) {
            System.out.println(u.username);
        }
    }
}

public class Main {
    static List<Product> products = new ArrayList<>();
    static List<User> users = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    static Admin admin = new Admin();

    public static void main(String[] args) {
        seedData();

        while (true) {
            System.out.println("\n--- Online Shopping System ---");
            System.out.println("1. Admin Login");
            System.out.println("2. User Register");
            System.out.println("3. User Login");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            int ch = sc.nextInt();

            switch (ch) {
                case 1 -> adminLogin();
                case 2 -> userRegister();
                case 3 -> userLogin();
                case 4 -> {
                    System.out.println("Thank you for visiting!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    static void adminLogin() {
        System.out.print("Admin Username: ");
        String user = sc.next();
        System.out.print("Admin Password: ");
        String pass = sc.next();
        if (admin.login(user, pass)) {
            while (true) {
                System.out.println("\n--- Admin Panel ---");
                System.out.println("1. Add Product");
                System.out.println("2. View Products");
                System.out.println("3. View Users");
                System.out.println("4. Logout");
                System.out.print("Choose option: ");
                int ch = sc.nextInt();
                switch (ch) {
                    case 1 -> admin.addProduct(products, sc);
                    case 2 -> admin.viewProducts(products);
                    case 3 -> admin.viewUsers(users);
                    case 4 -> { return; }
                    default -> System.out.println("Invalid option.");
                }
            }
        } else {
            System.out.println("Invalid admin credentials.");
        }
    }

    static void userRegister() {
        System.out.print("Choose username: ");
        String username = sc.next();
        System.out.print("Choose password: ");
        String password = sc.next();
        for (User u : users) {
            if (u.username.equals(username)) {
                System.out.println("Username already exists.");
                return;
            }
        }
        users.add(new User(username, password));
        System.out.println("Registration successful.");
    }

    static void userLogin() {
        System.out.print("Username: ");
        String username = sc.next();
        System.out.print("Password: ");
        String password = sc.next();
        for (User u : users) {
            if (u.username.equals(username) && u.password.equals(password)) {
                userPanel(u);
                return;
            }
        }
        System.out.println("Invalid credentials.");
    }

    static void userPanel(User user) {
        while (true) {
            System.out.println("\n--- User Panel (" + user.username + ") ---");
            System.out.println("1. View Products");
            System.out.println("2. Add to Cart");
            System.out.println("3. View Cart");
            System.out.println("4. Purchase");
            System.out.println("5. Logout");
            System.out.print("Choose option: ");
            int ch = sc.nextInt();
    
            switch (ch) {
                case 1 -> user.viewProducts(products);
                case 2 -> {
                    user.viewProducts(products);
                    System.out.print("Enter product ID to add to cart: ");
                    int pid = sc.nextInt();
                    Product p = findProductById(pid);
                    if (p != null) {
                        user.addToCart(p, sc);  // Pass the Scanner object here for quantity input
                    } else {
                        System.out.println("Invalid product ID.");
                    }
                }
                case 3 -> user.viewCart();
                case 4 -> user.purchase();
                case 5 -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    static Product findProductById(int id) {
        for (Product p : products) {
            if (p.id == id) return p;
        }
        return null;
    }

    static void seedData() {
        products.add(new Product(1, "Laptop", 700, 5));
        products.add(new Product(2, "Phone", 300, 10));
        products.add(new Product(3, "Headphones", 50, 20));
    }
}
