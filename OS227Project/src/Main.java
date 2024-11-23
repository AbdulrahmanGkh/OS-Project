import java.util.*;

public class Main {
    public static Queue<PCB> jobQueue = new LinkedList<>();
    
    public static Queue<PCB> sjfQueue = new LinkedList<>();

    public static Queue<PCB>  readyQueue = new LinkedList<>();
    static MemoryManagment memory = new MemoryManagment(1024);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String filePath = "C:\\Users\\HP\\Documents\\job.txt.txt";

        boolean continueProgram = true; // للتحكم في استمرار تشغيل البرنامج

        while (continueProgram) {
            // إعادة تعيين الـ jobQueue و readyQueue في كل مرة
            jobQueue.clear();
            readyQueue.clear();
            memory = new MemoryManagment(1024); // إعادة تعيين الذاكرة

            // قراءة العمليات من الملف
            FileReaderThread fileReader = new FileReaderThread(jobQueue, filePath);
            fileReader.start();

        

            // عرض قائمة الجدولة
            System.out.println("Choose Scheduling Algorithm:");
            System.out.println("1. FCFS");
            System.out.println("2. SJF");
            System.out.println("3. RR");
            System.out.println("4. Exit");
            int choice = scanner.nextInt();

            if (choice == 4) {
                continueProgram = false; // إنهاء البرنامج
                System.out.println("Exiting the program. Goodbye!");
                break;
            }

            int quantum = 0;
            if (choice == 3) {
                System.out.print("Enter Quantum for RR: ");
                quantum = scanner.nextInt();
            }

            // تحميل العمليات بناءً على اختيار المستخدم
            LoadToReadyQueue loader = new LoadToReadyQueue(sjfQueue,readyQueue, memory, choice, quantum);
            loader.start();
           

            // إنشاء كائن الجدولة
            //ExecuteReadyQueue scheduler = new ExecuteReadyQueue(jobQueue, readyQueue, quantum, memory);
            ExecuteReadyQueue scheduler = new ExecuteReadyQueue(jobQueue, readyQueue, quantum, memory);

            // تطبيق الجدولة بناءً على الخيار
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
                    System.out.println("Invalid choice. Please try again.");
            }

            System.out.println("\nScheduling completed. Returning to main menu...\n");
        }

        scanner.close(); // إغلاق الـ Scanner
    }
}
