import java.util.LinkedList;
import java.util.Queue;

public class Main {
    public static Queue<PCB> jobQueue = new LinkedList<>();
    public static Queue<PCB> readyQueue = new LinkedList<>();
    static MemoryManagment memory = new MemoryManagment(1024);
    
    		private static void trackQueue(Queue<PCB> queue) {
    				if(queue.isEmpty()) {
    						System.out.println("Queue is empty , happy or sad?");
    						return;
    				}
    	
    				for (PCB job : queue) {
    					System.out.println("Job ID: " + job.id);
    				}
    		}

    public static void main(String[] args) {
   
        String filePath = "job.txt.txt";
        FileReaderThread fileReader = new FileReaderThread(jobQueue, filePath);
        fileReader.start();

      try {
            fileReader.join();
            System.out.println("FileReaderThread has completed reading jobs.");
        } catch (InterruptedException e) {
            System.out.println("FileReaderThread was interrupted.");
            return;
        }

        LoadToReadyQueue loader = new LoadToReadyQueue(jobQueue, readyQueue , memory);
        loader.start();

        ExecuteReadyQueue scheduler = new ExecuteReadyQueue(jobQueue, readyQueue, 8 , memory); // تمرير readyQueue و quantum

        scheduler.fcfsSchedule();
        
        System.out.println("Job IDs in jobQueue:");
        trackQueue(jobQueue);

        System.out.println("Job IDs in readyQueue:");
        trackQueue(readyQueue);
    }
    
}
