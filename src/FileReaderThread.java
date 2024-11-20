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
                // Check if the line contains exactly one colon and one semicolon
                if (line.contains(":") && line.contains(";") && line.indexOf(":") < line.indexOf(";")) {
                    String[] parts = line.split(":|;");

                    // Only accept lines with exactly three parts (id, burstTime, memoryRequired)
                    if (parts.length == 3) {
                        try {
                            int id = Integer.parseInt(parts[0].trim());
                            int burstTime = Integer.parseInt(parts[1].trim());
                            int memoryRequired = Integer.parseInt(parts[2].trim());

                            // Check if memory is within the valid range (1024 MB limit)
                            if (memoryRequired > 1024) {
                                System.out.println("Job " + id + " isn't supported due to memory limit.");
                                continue;
                            }

                            // Create PCB and add it to the jobQueue
                            PCB job = SystemCalls.createPCB(id, burstTime, memoryRequired);
                            synchronized (jobQueue) {
                                jobQueue.add(job);
                                jobQueue.notifyAll(); // Notify other threads waiting on jobQueue
                            }

                            System.out.println("Job " + id + " added to JobQueue: Burst Time = " + burstTime + " ms, Memory = " + memoryRequired + " MB.");

                        } catch (NumberFormatException e) {
                            System.out.println("Invalid number format in line, skipping: " + line);
                        }
                    } else {
                        // Skip lines with incorrect number of parts (id:bt:mr should have exactly 3 parts)
                        System.out.println("Invalid number format in line, skipping: " + line);
                    }
                } else {
                    // Skip lines that do not have exactly one colon and one semicolon
                    System.out.println("Invalid number format in line, skipping: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}
