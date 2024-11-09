import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Queue;

class FileReaderThread extends Thread {
    private Queue<PCB> jobQueue;

    public FileReaderThread(Queue<PCB> jobQueue) {
        this.jobQueue = jobQueue;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new FileReader("job.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":|;"); // Spliting each process's info: "ID:burstTime;memory"
                if (parts.length == 3) {
                    int id = Integer.parseInt(parts[0]); // parts[0] = process id 
                    int burstTime = Integer.parseInt(parts[1]); // parts[1] = burst time
                    int memoryRequired = Integer.parseInt(parts[2]); // parts[2] = memoryRequired

                    // Now we have the essential information about a process , create a PCB
                    PCB process = new PCB(id, burstTime, memoryRequired);
                    jobQueue.add(process);

                    System.out.println("Job " + process.id + " added to job queue with burst time: " + process.burstTime + " and memory required: " + process.memoryRequired + "MB");
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the job file: " + e.getMessage());
        }
    }
}
