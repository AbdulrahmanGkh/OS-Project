import java.util.Queue;

class CPUScheduler {
    private Queue<PCB> readyQueue;
    private LongTermScheduler longTermScheduler; // Reference to long-term scheduler to release memory

    public CPUScheduler(Queue<PCB> readyQueue, LongTermScheduler longTermScheduler) {
        this.readyQueue = readyQueue;
        this.longTermScheduler = longTermScheduler;
    }

    // Simulate the execution of a process
    public void executeProcess() {
        if (!readyQueue.isEmpty()) {
            PCB process = readyQueue.poll();
            process.state = "Running";
            System.out.println("Executing Process " + process.id + " with Burst Time: " + process.burstTime);

            // Burst time Simulation
            try {
                Thread.sleep(process.burstTime); // Sleep for the burst time duration in milliseconds
            } catch (InterruptedException e) {
                System.out.println("Process " + process.id + " execution was interrupted.");
            }

            process.state = "Terminated";
            System.out.println("Process " + process.id + " completed execution and terminated.");

            // Release memory and notify the long-term scheduler
            longTermScheduler.releaseMemory(process.memoryRequired);
        } else {
            System.out.println("No processes in the Ready Queue to execute.");
        }
    }
}
