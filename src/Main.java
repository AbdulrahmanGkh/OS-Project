import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Queue<PCB> jobQueue = new LinkedList<>(); // Queue to hold jobs from the file
        Queue<PCB> readyQueue = new LinkedList<>(); // Queue for ready jobs to be scheduled
        String fileName = "C:\\Users\\Othman\\Desktop\\job.txt"; // Input file name for jobs

        // Initialize the file reader thread to read jobs from the file
        FileReaderThread fileReader = new FileReaderThread(jobQueue, fileName);
        fileReader.start();

        // Wait for FileReaderThread to finish reading jobs
        try {
            fileReader.join();
        } catch (InterruptedException e) {
            System.out.println("Error: FileReaderThread interrupted.");
        }

        // Move all jobs from jobQueue to readyQueue
        readyQueue.addAll(jobQueue);

        // Initialize the CPU scheduler
        CPUScheduler cpuScheduler = new CPUScheduler(readyQueue);

        // Display menu for scheduling algorithm selection
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select a CPU Scheduling Algorithm:");
        System.out.println("1. First-Come-First-Serve (FCFS)");
        System.out.println("2. Round-Robin (RR) with Quantum = 8ms");
        System.out.println("3. Shortest Job First (SJF)");
        System.out.print("Enter your choice (1, 2, or 3): ");
        int choice = scanner.nextInt();
        scanner.close();

        // Run the selected scheduling algorithm
        switch (choice) {
            case 1:
                System.out.println("\n--- First-Come-First-Serve (FCFS) Scheduling ---");
                cpuScheduler.fcfsSchedule();
                break;
            case 2:
                System.out.println("\n--- Round-Robin (RR) Scheduling with Quantum = 8ms ---");
                cpuScheduler.rrSchedule();
                break;
            case 3:
                System.out.println("\n--- Shortest Job First (SJF) Scheduling ---");
                cpuScheduler.sjfSchedule();
                break;
            default:
                System.out.println("Invalid choice. Exiting the program.");
                break;
        }
    }
}
