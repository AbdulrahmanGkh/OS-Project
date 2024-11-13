import java.util.Iterator;
import java.util.Queue;

class LongTermScheduler extends Thread { // INFINITE LOOOOOOOOOOOOOOOOOOOOOOOOOP Class
    private Queue<PCB> jobQueue;
    private Queue<PCB> readyQueue;
    private final int totalMemory = 1024;
    private int usedMemory = 0;

    public LongTermScheduler(Queue<PCB> jobQueue, Queue<PCB> readyQueue) {
        this.jobQueue = jobQueue;
        this.readyQueue = readyQueue;
    }

    // Run method to load jobs into the ready queue in a separate thread
    @Override
    public void run() {
        while (true) {
            loadJobsToReadyQueue();
            try {
                // Adding a small sleep to prevent busy waiting and allow CPU Scheduler to work
                Thread.sleep(100); 
            } catch (InterruptedException e) {
                System.out.println("LongTermScheduler interrupted.");
                break; // Exit the loop if the thread is interrupted
            }
        }
    }

    // Modified method to load jobs from jobQueue to readyQueue if memory is available
    private void loadJobsToReadyQueue() {
        Iterator<PCB> iterator = jobQueue.iterator();

        // Use iterator to safely check each job and remove if loaded
        while (iterator.hasNext()) {
            PCB job = iterator.next();
            if (usedMemory + job.memoryRequired <= totalMemory) {
                usedMemory += job.memoryRequired;
                job.state = "Ready";
                readyQueue.add(job);
                System.out.println("Job " + job.id + " loaded into Ready Queue with memory: " + job.memoryRequired);
                
                // Remove the job from jobQueue as it has been loaded
                iterator.remove();
            }
        }
    }

    // Method to release memory when a job is terminated
    public void releaseMemory(int memoryReleased) {
        usedMemory -= memoryReleased;
    }
}
