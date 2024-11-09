import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Queue;

class FileReaderThread extends Thread {
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
                String[] parts = line.split(":|;"); // Assuming format "ID:burstTime;memory"
                if (parts.length == 3) {
                    int id = Integer.parseInt(parts[0]);
                    int burstTime = Integer.parseInt(parts[1]);
                    int memoryRequired = Integer.parseInt(parts[2]);

                    // Create a new PCB object and add it to the job queue
                    PCB job = new PCB(id, burstTime, memoryRequired);
                    jobQueue.add(job);

                    System.out.println("Job " + job.id + " added to job queue with burst time: " + job.burstTime + " and memory required: " + job.memoryRequired + "MB");
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the job file: " + e.getMessage());
        }
    }
}
