import java.util.LinkedList;
import java.util.Queue;

public class Main {
    public static Queue<PCB> jobQueue = new LinkedList<>();
    public static Queue<PCB> readyQueue = new LinkedList<>();
    public static int availableMemory = 1024; // إجمالي الذاكرة المتاحة (بالـ MB)

    public static void main(String[] args) {
        // مسار الملف
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

        // تحميل العمليات إلى الذاكرة
        LoadToReadyQueue loader = new LoadToReadyQueue(jobQueue);
        loader.start();

        try {
            loader.join();
            System.out.println("LoadToReadyQueue has completed loading jobs.");
        } catch (InterruptedException e) {
            System.out.println("LoadToReadyQueue was interrupted.");
            return;
        }

        // إنشاء كائن ExecuteReadyQueue
        ExecuteReadyQueue scheduler = new ExecuteReadyQueue(readyQueue, 8); // تمرير readyQueue و quantum

        // تنفيذ الجدولة
        scheduler.fcfsSchedule(); // خوارزمية FCFS
        // scheduler.sjfSchedule(); // خوارزمية SJF
        // scheduler.rrSchedule();  // خوارزمية Round-Robin
    }
}
