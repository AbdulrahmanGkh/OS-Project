import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Queue;

public class FileReaderThread extends Thread {
    private Queue<PCB> jobQueue;
    private String fileName;

    public FileReaderThread(Queue<PCB> jobQueue, String fileName) {
        this.jobQueue = jobQueue;
        this.fileName = fileName;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":|;");
                if (parts.length == 3) {
                    int id = Integer.parseInt(parts[0]);
                    int burstTime = Integer.parseInt(parts[1]);
                    int memoryRequired = Integer.parseInt(parts[2]);

                    PCB process = new PCB(id, burstTime, memoryRequired);
                    jobQueue.add(process);
                    System.out.println("Job " + process.id + " added to job queue with burst time: " 
                                       + process.burstTime + " and memory required: " + process.memoryRequired + " MB");
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the job file: " + e.getMessage());
        }
    }
}
