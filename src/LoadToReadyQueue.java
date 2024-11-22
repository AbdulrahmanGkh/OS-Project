import java.util.*;

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
        // Create a shallow copy of jobQueue into TempQueue
        Queue<PCB> tempQueue = new LinkedList<>(jobQueue);  // Shallow copy of jobQueue

        synchronized (jobQueue) {
            while (!tempQueue.isEmpty()) {
                PCB job = tempQueue.peek();  // Access job from the copied queue

                // Check if memory is available and load the job
                if (job.memoryRequired <= memory.getAvailableMemory()) {
                    tempQueue.remove(); // Remove the job from tempQueue, not jobQueue
                    readyQueue.add(job); // Add job to readyQueue
                    memory.allocateMemory(job.memoryRequired);
                    job.changeState("READY");
                    System.out.println("Job " + job.id + " loaded into memory. Remaining Memory: " + memory.getAvailableMemory() + " MB.");
                } else {
                    try {
                        // Wait for memory to become available
                        System.out.println("Not enough memory, waiting... process: " + job.id );
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
