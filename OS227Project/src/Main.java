import java.util.Scanner;
import java.util.*;
public class Main {
    public static Queue<PCB> jobQueue = new LinkedList<>();
    public static Queue<PCB> readyQueue = new LinkedList<>();
    static MemoryManagment memory = new MemoryManagment(1024);

    public static void main(String[] args) {
        String filePath = "C:\\Users\\HP\\Documents\\job.txt.txt";

        // قراءة العمليات
        FileReaderThread fileReader = new FileReaderThread(jobQueue, filePath);
        fileReader.start();

        try {
            fileReader.join();
            System.out.println("FileReaderThread has completed reading jobs.");
        } catch (InterruptedException e) {
            System.out.println("FileReaderThread was interrupted.");
            return;
        }

        // اختيار نوع الجدولة
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose Scheduling Algorithm:");
        System.out.println("1. FCFS");
        System.out.println("2. SJF");
        System.out.println("3. RR");
        int choice = scanner.nextInt();

        int quantum = 0;
        if (choice == 3) {
            System.out.print("Enter Quantum for RR: ");
            quantum = scanner.nextInt();
        }

        // تحميل العمليات بناءً على اختيار المستخدم
        LoadToReadyQueue loader = new LoadToReadyQueue(jobQueue, readyQueue, memory, choice, quantum);
        loader.start();

        try {
            loader.join();
        } catch (InterruptedException e) {
            System.out.println("Loader was interrupted.");
        }

        // جدولة العمليات
        ExecuteReadyQueue scheduler = new ExecuteReadyQueue(jobQueue, readyQueue, quantum, memory);

        switch (choice) {
            case 1:
                scheduler.fcfsSchedule();
                break;
            case 2:
                scheduler.sjfSchedule();
                break;
            case 3:
                scheduler.rrSchedule();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }
}
