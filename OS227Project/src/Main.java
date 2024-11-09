import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
	//This method is used to check if scanner input is valid number(from 1-5) any strings or invalid numbers will be rejcted 
    private static int getSafeIntegerInput(Scanner scanner) { 
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("\u001B[31m" + "Invalid input. Please enter a number!" + "\u001B[0m");
                System.out.println();
                displayMenu();
            }
        }
    }

    public static void displayMenu() {
        System.out.println("Welcome to the CPU Scheduler Simulation Program!");
        System.out.println("Please choose an option:");
        System.out.println("1. First-Come-First-Serve (FCFS) Scheduling");
        System.out.println("2. Round-Robin (RR) Scheduling");
        System.out.println("3. Shortest Job First (SJF) Scheduling");
        System.out.println("4. Display Average Waiting and Turnaround Times");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            displayMenu();
            choice = getSafeIntegerInput(scanner);
            
            switch (choice) {
                case 1:
                    System.out.println("Running First-Come-First-Serve (FCFS) Scheduling...");
                    // Call FCFS scheduling method here
                    System.out.println();
                    break;
                
                case 2:
                    System.out.println("Running Round-Robin (RR) Scheduling...");
                    // Call Round-Robin scheduling method here
                    System.out.println();
                    break;
                
                case 3:
                    System.out.println("Running Shortest Job First (SJF) Scheduling...");
                    // Call SJF scheduling method here
                    System.out.println();
                    break;
                
                case 4:
                    System.out.println("Displaying Average Waiting and Turnaround Times...");
                    // Call method to display average times here
                    System.out.println();
                    break;
                
                case 5:
                    System.out.println("Exiting the program. Goodbye!");
                    break;
                
                default:
                    System.out.println("\u001B[31m" + "Invalid choice, please try entering a number from 1 to 5." + "\u001B[0m");
                    System.out.println();
            }
            
        } while (choice != 5);
        
        scanner.close();
    }
}
