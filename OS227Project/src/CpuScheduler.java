import java.util.Queue;

class CPUScheduler {
  
    public static void FCFS(Queue<PCB> readyQueue) {
        int currentTime = 0;
        System.out.println("Process ID | Burst Time | Completion Time | Turnaround Time | Waiting Time");
        for (PCB p : readyQueue) {
            p.turnaroundTime = currentTime + p.burstTime;
            p.waitingTime = p.turnaroundTime - p.burstTime;
            currentTime = p.turnaroundTime;
            System.out.printf("%9d | %12d | %10d | %15d | %16d | %12d\n", p.id, p.burstTime, p.turnaroundTime, p.waitingTime);
        }
    }

   
    // Round-Robin Scheduling (Quantum = 8ms)
    public void rrSchedule() {
       
    }

    // Shortest Job First (SJF)
    public void sjfSchedule() {
       
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
    
    public void executeProcessRR(PCB process) {
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
