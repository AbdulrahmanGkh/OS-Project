import java.util.LinkedList;
import java.util.Queue;

public class Main {
    public static Queue<PCB> jobQueue = new LinkedList<>();
    public static Queue<PCB> readyQueue = new LinkedList<>();
    static MemoryManagment memory = new MemoryManagment(1024);

    public static void main(String[] args) {
        // مسار الملف
        String filePath = "job.txt";

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
        LoadToReadyQueue loader = new LoadToReadyQueue(jobQueue, readyQueue , memory);
        loader.start();

        /*try {
            loader.join();
            System.out.println("LoadToReadyQueue has completed loading jobs.");
        } catch (InterruptedException e) {
            System.out.println("LoadToReadyQueue was interrupted.");
            return;
        }*/

        // إنشاء كائن ExecuteReadyQueue
        ExecuteReadyQueue scheduler = new ExecuteReadyQueue(jobQueue, readyQueue, 8 , memory); // تمرير readyQueue و quantum

        // تنفيذ الجدولة
        scheduler.fcfsSchedule(); // خوارزمية FCFS
        // scheduler.sjfSchedule(); // خوارزمية SJF
        // scheduler.rrSchedule();  // خوارزمية Round-Robin
    }
}
