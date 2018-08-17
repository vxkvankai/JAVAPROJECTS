import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static GroceryList groceryList = new GroceryList();

    public static void main(String[] args)
    {
        boolean quit = false;
        int choice = 0;
        printInstructions();
        while (!quit)
        {
            System.out.println("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice)
            {
                case 0:
                    printInstructions();
                    break;
                case 1:
                    groceryList.printGroceryList();
                    break;
                case 2:
                    addItem();
                    break;
                case 3:
                    modifyItem();
                    break;
                case 4:
                    removeItem();
                    break;
                case 5:
                    searchForItem();
                    break;
            }

        }
    }

    public static void printInstructions(){
        System.out.println("\nPresss");
        System.out.println("\t 0 - To print choice Options.  ");
        System.out.println("\t 1 - To print the grocery list items.  ");
        System.out.println("\t 2 - To add an item to the list.  ");
        System.out.println("\t 3 - To modify an itme in the list.  ");
        System.out.println("\t 4 - To remove an item in the list.  ");
        System.out.println("\t 5 - To find an item in the list.  ");
        System.out.println("\t 6 - To quit the application.  2");

    }
    public static void addItem(){
        System.out.print("please enter the grocery item: ");
        groceryList.addGroceryItem(scanner.nextLine());
    }

    public static void modifyItem(){
        System.out.print("Enter item number: ");
        int itemNo = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter replacement item: ");
        String newItem = scanner.nextLine();
        groceryList.modifyGroceryItem(itemNo-1, newItem);
    }

    public static void removeItem(){
        System.out.print("Enter item number: ");
        int itemNo = scanner.nextInt();
        scanner.nextLine();
        groceryList.removeGroceryItem(itemNo-1);
    }

    public static void searchForItem(){
        System.out.print("Enter item number: ");
        String searchItem = scanner.nextLine();
        if (groceryList.findItems(searchItem) != null)
        {
            System.out.println("Found " + searchItem + " in our grocery list");
        }
        else
        {
            System.out.println(searchItem + "is not in the shopping list");
        }
    }































}
