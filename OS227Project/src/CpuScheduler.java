import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Comparator;

class CPUScheduler {
    private Queue<PCB> readyQueue;
    private LongTermScheduler longTermScheduler; // Reference to long-term scheduler to release memory

    public CPUScheduler(Queue<PCB> readyQueue, LongTermScheduler longTermScheduler) {
        this.readyQueue = readyQueue;
        this.longTermScheduler = longTermScheduler;
    }

   
    public void fcfsSchedule() {
    ...
    }

    public void rrSchedule() {
    ...
    }

 
    // Shortest Job First (SJF)
    public void sjfSchedule() {
    ...
    }

    // Method to execute a process (simulating execution time)
    public void executeProcess(PCB process) {
        process.state = "Running";
        System.out.println("Executing Process " + process.id + " with Burst Time: " + process.burstTime);

        // Simulate the process execution (sleeping for the burst time duration)
        try {
            Thread.sleep(process.burstTime);  // Sleep for the burst time in milliseconds
        } catch (InterruptedException e) {
            System.out.println("Process " + process.id + " execution was interrupted.");
        }

        process.state = "Terminated";
        System.out.println("Process " + process.id + " completed execution and terminated.");
    }
    
    public void executeProcessRR(PCB process) { // Execution processes for Round-Robin ( not necessory to terminate )
        process.state = "Running";
        System.out.println("Executing Process " + process.id + " with Burst Time: " + process.burstTime);

        // Simulate the process execution (sleeping for the burst time duration)
        try {
            Thread.sleep(Math.min(8, process.remainingTime));  // Sleep for the burst time in milliseconds
        } catch (InterruptedException e) {
            System.out.println("Process " + process.id + " execution was interrupted.");
        }
        
        if(process.remainingTime > 0) {
        	
        	process.state = "Ready";
        } else {
        	
        	process.state = "Terminated";
        	System.out.println("Process " + process.id + " completed execution and terminated.");
        }
    }
}
