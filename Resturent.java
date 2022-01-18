import java.util.ArrayList;
import java.util.Scanner;


class Customer{
    String name;
    int numberOfOrders;
    double totalMoneySpent, totalCaloriesConsumed;
    public Customer(String name){
        this.name = name;
        numberOfOrders = 0;
        totalMoneySpent = 0;
    }
    void placeOrderForDrink(String drink, double amount, String ... addOns){
        OrderDrinks orderDrinks = new OrderDrinks(drink, amount, name, addOns);
        orderDrinks.serveDrink();
        numberOfOrders++;
        totalMoneySpent += orderDrinks.getPrice();
        totalCaloriesConsumed += orderDrinks.getCalories();
    }

    void printCustomerDetails(){
        System.out.println("               Customer: " + name);
        System.out.println("       Number of orders: " + numberOfOrders);
        System.out.println("      Total money spent: " + totalMoneySpent + " taka");
        System.out.println("Total calories consumed: " + totalCaloriesConsumed + " calories");
        System.out.println("-----------------------------------------------------");
    }
}






interface DrinkDispenser{
    void dispenseDrink(double amount, String customer, String ... addOns);
    double calculatePrice(double amount);
    double calculateCalories(double amount);
}
interface DrinkDispenserForCocaCola extends DrinkDispenser{
    void dispenseDrink(double amount, String customer, String ... addOns);
    double calculatePrice(double amount);
    double calculateCalories(double amount);
    void dietCoke();
    void CocaColaZero();
}
interface DrinkDispenserForSprite extends DrinkDispenser{
    void dispenseDrink(double amount, String customer, String ... addOns);
    double calculatePrice(double amount);
    double calculateCalories(double amount);
    void addLemonFlavour();
    void addStrawberryFlavour();
    void addCherryFlavour();
}
class CocaColaDispenser implements DrinkDispenserForCocaCola{
    double unitPrice = 0.17;
    boolean dietCoke = false, CocaColaZero = false;
    public void dispenseDrink(double amount, String customer, String ... addOns){
        System.out.println("Dispensing " + amount + "ml of CocaCola for " + customer);
        for(String addOn : addOns){
            switch(addOn){
                case "diet":
                    dietCoke();
                    break;
                case "zero":
                    CocaColaZero();
                    break;
            }
        }
    }
    public double calculatePrice(double amount) {
        double price = amount * unitPrice;
        if(dietCoke){
            price = price + (price * 0.25);
        }
        if(CocaColaZero){
            price = price + (price * 0.35);
        }
        return price;
    }
    public double calculateCalories(double amount) {
        double calories = amount * 1;
        if(dietCoke){
            calories = (calories * 0.75);
        }
        if(CocaColaZero){
            calories = (calories * 0.1);
        }
        return calories;
    }
    public void dietCoke() {
        dietCoke = true;
        System.out.println("** Diet Coke added");
    }
    public void CocaColaZero() {
        CocaColaZero = true;
        System.out.println("** CocaCola Zero added");
    }
}
class SpriteDispenser implements DrinkDispenserForSprite{
    double unitPrice = 0.15;
    boolean lemonFlavour = false, strawberryFlavour = false, cherryFlavour = false;
    public void dispenseDrink(double amount, String customer, String ... addOns){
        System.out.println("Dispensing " + amount + "ml of Sprite for " + customer);
        for(String addOn : addOns){
            switch(addOn){
                case "lemon":
                    addLemonFlavour();
                    break;
                case "strawberry":
                    addStrawberryFlavour();
                    break;
                case "cherry":
                    addCherryFlavour();
                    break;
            }
        }
    }
    public double calculatePrice(double amount) {
        double price = amount * unitPrice;
        if(lemonFlavour){
            price = price + (price * 0.25);
        }
        if(strawberryFlavour){
            price = price + (price * 0.3);
        }
        if(cherryFlavour){
            price = price + (price * 0.5);
        }
        return price;
    }
    public double calculateCalories(double amount) {
        return amount * 0.35;
    }
    public void addLemonFlavour() {
        lemonFlavour = true;
        System.out.println("** Lemon flavour is added");
    }
    public void addStrawberryFlavour() {
        strawberryFlavour = true;
        System.out.println("** Strawberry flavour is added");
    }
    public void addCherryFlavour() {
        cherryFlavour = true;
        System.out.println("** Cherry flavour is added");
    }
}
class DrinkDispenserFactory{
    public static DrinkDispenser getDrinkDispenser(String drink){
        switch (drink){
            case "CocaCola":
                return new CocaColaDispenser();
            case "Sprite":
                return new SpriteDispenser();
            default:
                return null;
        }
    }
}
class OrderDrinks{
    DrinkDispenser drinkDispenser;
    double amount;
    String customer;
    String addOns[];
    public OrderDrinks(String drink, double amount, String customer, String[] addOns){
        drinkDispenser = DrinkDispenserFactory.getDrinkDispenser(drink);
        this.amount = amount;
        this.customer = customer;
        this.addOns = addOns;
    }
    public void serveDrink(){
        drinkDispenser.dispenseDrink(amount, customer, addOns);
    }
    public double getPrice() {
        return drinkDispenser.calculatePrice(amount);
    }
    public double getCalories() {
        return drinkDispenser.calculateCalories(amount);
    }
}






class CustomerLoyalty{
    interface LoyaltyPolicy{
        boolean compare(Customer customer1, Customer customer2);
    }
    void sortCustomers(ArrayList<Customer> customers, LoyaltyPolicy loyaltyPolicy){
        for(int i = 0; i < customers.size(); i++){
            for(int j = i + 1; j < customers.size(); j++){
                if(loyaltyPolicy.compare(customers.get(i), customers.get(j))){
                    Customer temp = customers.get(i);
                    customers.set(i, customers.get(j));
                    customers.set(j, temp);
                }
            }
        }
        System.out.println("Sorted customers according to loyalty policy");
        for(Customer customer : customers){
            customer.printCustomerDetails();
        }
    }
}
public class Resturent {
    static ArrayList<Customer> customers = new ArrayList<Customer>();
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            String scanned = scanner.nextLine();
            while(!scanned.equals("terminate")){
                String[] command = scanned.split(" ");
                switch(command[1]){
                    case "enters":
                        customers.add(new Customer(command[0]));
                        break;
                    case "orders":
                        String[] addOns = new String[command.length - 4];
                        for(int i = 4; i < command.length; i++){
                            addOns[i - 4] = command[i];
                        }
                        customers.stream().filter(customer -> customer.name.equals(command[0])).findFirst().get().placeOrderForDrink(command[2], Double.parseDouble(command[3]), addOns);
                        break;
                    case "print":
                        customers.stream().filter(customer -> customer.name.equals(command[0])).findFirst().get().printCustomerDetails();
                        break;
                    case "loyalty":
                        new CustomerLoyalty().sortCustomers(customers, new CustomerLoyalty.LoyaltyPolicy(){
                            public boolean compare(Customer customer1, Customer customer2){
                                return customer1.totalMoneySpent < customer2.totalMoneySpent;
                            }
                        });
                }
                scanned = scanner.nextLine();
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
}
