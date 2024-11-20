import java.util.Queue;

public class LoadToReadyQueue extends Thread {
    private Queue<PCB> jobQueue;
    private Queue<PCB> readyQueue;
    private MemoryManagment memory;

    public LoadToReadyQueue(Queue<PCB> jobQueue, Queue<PCB> readyQueue, MemoryManagment memory) {
        this.jobQueue = jobQueue;
        this.memory = memory;
        this.readyQueue = readyQueue;
    }

    @Override
    public void run() {
        synchronized (jobQueue) {
            while (!jobQueue.isEmpty()) {
                PCB job = jobQueue.peek();

                // Check if memory is available and load the job
                if (job.memoryRequired <= memory.getAvailableMemory()) {
                    jobQueue.remove();
                    readyQueue.add(job);
                    memory.allocateMemory(job.memoryRequired);
                    job.changeState("READY");
                    System.out.println("Job " + job.id + " loaded into memory. Remaining Memory: " + memory.getAvailableMemory() + " MB.");
                } else {
                    try {
                        // Wait for memory to become available
                        System.out.println("Not enough memory, waiting...");
                        jobQueue.wait(); // Wait for memory to be released
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }

                try {
                    Thread.sleep(100); // Simulate delay between operations
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }
}
