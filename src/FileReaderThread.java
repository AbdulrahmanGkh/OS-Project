import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Queue;

public class FileReaderThread extends Thread {
    private Queue<PCB> jobQueue;
    private String filePath;

    public FileReaderThread(Queue<PCB> jobQueue, String filePath) {
        this.jobQueue = jobQueue;
        this.filePath = filePath;
    }

    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":|;");
                if (parts.length != 3) {
                    System.out.println("Invalid line format, skipping: " + line);
                    continue;
                }

                int id = Integer.parseInt(parts[0].trim());
                int burstTime = Integer.parseInt(parts[1].trim());
                int memoryRequired = Integer.parseInt(parts[2].trim());

                if (memoryRequired > 1024) { // Handling OverSize Processes (greater than 1024MB)
                    System.out.println("Job " + id + " isn't supported due to memory limit.");
                    continue;
                }

                PCB job = SystemCalls.createPCB(id, burstTime, memoryRequired); // Use SystemCalls to create the PCB
                synchronized (jobQueue) {
                    jobQueue.add(job);
                    jobQueue.notifyAll(); // Notify other threads waiting on jobQueue
                }
                System.out.println("Job " + id + " added to JobQueue: Burst Time = " + burstTime + " ms, Memory = " + memoryRequired + " MB.");
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}
