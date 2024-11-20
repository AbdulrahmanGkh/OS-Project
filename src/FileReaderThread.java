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
                // Split the line into parts using ":" and ";"
                String[] parts = line.split(":|;");

                // Check if the line contains exactly one colon, one semicolon, and splits into 3 parts
                if (!line.contains(":") || !line.contains(";") || line.indexOf(":") >= line.indexOf(";") || parts.length != 3) {
                    System.out.println("Invalid format, skipping line: " + line);
                    continue;  // Skip this line if the format is invalid
                }

                // Parse the parts and create a PCB
                try {
                    int id = Integer.parseInt(parts[0].trim());
                    int burstTime = Integer.parseInt(parts[1].trim());
                    int memoryRequired = Integer.parseInt(parts[2].trim());

                    // Check memory constraint (should not exceed 1024 MB)
                    if (memoryRequired > 1024) {
                        System.out.println("Job " + id + " is skipped due to memory limitation.");
                        continue;  // Skip jobs that exceed memory limit
                    }

                    // Create PCB and add it to the jobQueue
                    PCB job = SystemCalls.createPCB(id, burstTime, memoryRequired);
                    synchronized (jobQueue) {
                        jobQueue.add(job);
                        jobQueue.notifyAll(); // Notify any waiting threads
                    }

                    System.out.println("Job " + id + " added to JobQueue: Burst Time = " + burstTime + " ms, Memory = " + memoryRequired + " MB.");
                } catch (NumberFormatException e) {
                    // Catch invalid number format for id, burstTime, or memoryRequired
                    System.out.println("Invalid number format, skipping: " + line);
                }
            }
        } catch (IOException e) {
            // Handle file reading errors
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}
