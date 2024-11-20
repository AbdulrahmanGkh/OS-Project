import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Comparator;

class CPUScheduler {
    private Queue<PCB> readyQueue;
    private final int totalMemory = 1024;
    private int usedMemory = 0;

    public CPUScheduler(Queue<PCB> readyQueue) {
        this.readyQueue = readyQueue;
    }

    
    //Approved by Professor AbdulRahman
    private boolean canExecute(PCB process) {
        return (usedMemory + process.memoryRequired) <= totalMemory;
    } 

    //Approved by Professor AbdulRahman
    private void allocateMemory(PCB process) {
        usedMemory += process.memoryRequired;
        System.out.println("Allocated memory for Process " + process.id + ": " + process.memoryRequired + " MB");//why we print this messege?
    } 

    
    //Approved by Professor AbdulRahman
    private void releaseMemory(PCB process) {
        usedMemory -= process.memoryRequired;
        System.out.println("Released memory for Process " + process.id + ": " + process.memoryRequired + " MB"); //why print?
    } 

    // First-Come-First-Serve (FCFS) Scheduling
    //Approved by Professor AbdulRahman, yet im not sure what should we print in terms of time
    public void fcfsSchedule() {
        int currentTime = 0;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;
        int processCount = 0;  // Counter to track number of processed jobs

        while (!readyQueue.isEmpty()) {
            PCB process = readyQueue.poll();
            if (canExecute(process)) {
                allocateMemory(process);
                calculateWaitingTimeFCFS(process, currentTime);
                executeProcess(process);
                calculateTurnaroundTimeFCFS(process);

                // Accumulate times for averages
                totalWaitingTime += process.waitingTime;
                totalTurnaroundTime += process.turnaroundTime;
                processCount++;

                releaseMemory(process);
                currentTime += process.burstTime;
            } else {
                System.out.println("Insufficient memory for Process " + process.id + ". Skipping execution.");
            }
        }

        // Calculate and display averages if any processes were executed
        if (processCount > 0) {
            double avgWaitingTime = (double) totalWaitingTime / processCount;
            double avgTurnaroundTime = (double) totalTurnaroundTime / processCount;
            System.out.println("Average Waiting Time (FCFS): " + avgWaitingTime + " ms");
            System.out.println("Average Turnaround Time (FCFS): " + avgTurnaroundTime + " ms");
        } else {
            System.out.println("No processes were executed in FCFS.");
        }
    } 
    

    // Shortest Job First (SJF) Scheduling

     
        //Rejected by Professor AbdulRahman
        // Using poll removes the process from sjfQueue when memory is unavailable.
        // This prevents the process from being re-added.
       // Use peek instead to keep the process in the queue.
      //مرفوضة يا معلم 
    public void sjfSchedule() {
        int currentTime = 0;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;
        int processCount = 0;

        PriorityQueue<PCB> sjfQueue = new PriorityQueue<>(Comparator.comparingInt(p -> p.burstTime));
        sjfQueue.addAll(readyQueue);

        while (!sjfQueue.isEmpty()) {
            PCB process = sjfQueue.poll();
            if (canExecute(process)) {
                allocateMemory(process);
                calculateWaitingTimeSJF(process, currentTime);
                executeProcess(process);
                calculateTurnaroundTimeSJF(process);

                totalWaitingTime += process.waitingTime;
                totalTurnaroundTime += process.turnaroundTime;
                processCount++;

                releaseMemory(process);
                currentTime += process.burstTime;
            } else {
                System.out.println("Insufficient memory for Process " + process.id + ". Skipping execution.");
            }
        }

        if (processCount > 0) {
            double avgWaitingTime = (double) totalWaitingTime / processCount;
            double avgTurnaroundTime = (double) totalTurnaroundTime / processCount;
            System.out.println("Average Waiting Time (SJF): " + avgWaitingTime + " ms");
            System.out.println("Average Turnaround Time (SJF): " + avgTurnaroundTime + " ms");
        } else {
            System.out.println("No processes were executed in SJF.");
        } 
       

    // Round-Robin (RR) Scheduling with Quantum = 8ms
    public void rrSchedule() {
        int currentTime = 0;
        int quantum = 8;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;
        int processCount = 0;

        //why here linkedList instaed of queue or Array
        Queue<PCB> rrQueue = new LinkedList<>(readyQueue); // Declare rrQueue as a Queue for flexibility (any queue implementation can be used) Initialize it as a LinkedList because LinkedList efficiently handles frequent additions and removals.

        while (!rrQueue.isEmpty()) {
            PCB process = rrQueue.poll();
            if (canExecute(process)) {
                int timeSlice = Math.min(quantum, process.remainingTime);
                allocateMemory(process);
                calculateWaitingTimeRR(process, currentTime);
                executeProcessWithTimeSlice(process, timeSlice);
                releaseMemory(process);
                currentTime += timeSlice; //وقفت هنا

                if (process.remainingTime > 0) {
                    rrQueue.add(process);  // Re-queue if not yet completed
                } else {
                    calculateTurnaroundTimeRR(process, currentTime);
                    totalWaitingTime += process.waitingTime;
                    totalTurnaroundTime += process.turnaroundTime;
                    processCount++;
                }
            } else {
                System.out.println("Insufficient memory for Process " + process.id + ". Skipping execution.");
            }
        }

        if (processCount > 0) {
            double avgWaitingTime = (double) totalWaitingTime / processCount;
            double avgTurnaroundTime = (double) totalTurnaroundTime / processCount;
            System.out.println("Average Waiting Time (RR): " + avgWaitingTime + " ms");
            System.out.println("Average Turnaround Time (RR): " + avgTurnaroundTime + " ms");
        } else {
            System.out.println("No processes were executed in RR.");
        }
    }

    // Executes a process with a specified time slice (used for Round-Robin)
    private void executeProcessWithTimeSlice(PCB process, int timeSlice) {
        process.state = "Running";
        System.out.println("Executing Process " + process.id + " with Time Slice: " + timeSlice + " ms");

        try {
            Thread.sleep(timeSlice);
        } catch (InterruptedException e) {
            System.out.println("Process " + process.id + " execution was interrupted.");
        }

        process.remainingTime -= timeSlice;
        if (process.remainingTime <= 0) {
            process.state = "Terminated";
            System.out.println("Process " + process.id + " completed execution and terminated.");
        } else {
            process.state = "Waiting";
            System.out.println("Process " + process.id + " paused with remaining time: " + process.remainingTime + " ms");
        }
    }

    // Method to execute a process (simulating execution time)
    public void executeProcess(PCB process) {
        process.state = "Running";
        System.out.println("Executing Process " + process.id + " with Burst Time: " + process.burstTime);

        try {
            Thread.sleep(process.burstTime);  // Simulate process execution
        } catch (InterruptedException e) {
            System.out.println("Process " + process.id + " execution was interrupted.");
        }

        process.state = "Terminated";
        System.out.println("Process " + process.id + " completed execution and terminated.");
    }

    // Methods to calculate waiting and turnaround times for each scheduling algorithm
    private void calculateWaitingTimeFCFS(PCB process, int currentTime) {
        process.waitingTime = currentTime;
    }

    private void calculateTurnaroundTimeFCFS(PCB process) {
        process.turnaroundTime = process.burstTime + process.waitingTime;
    }

    
   //This is not complete, also we have to add variable for Last excution time
    // we have to Declare Last excution time in PCB
    //مرفوضة يامعلم
     //Rejected by Professor AbdulRahman
    private void calculateWaitingTimeRR(PCB process, int currentTime) {
        if (process.waitingTime == 0) {
            process.waitingTime = currentTime;
        }
    }



    //مرفوضة يا معلم
     //Rejected by Professor AbdulRahman
    //process.turnaroundTime = currentTime;
    private void calculateTurnaroundTimeRR(PCB process, int currentTime) {
        process.turnaroundTime = currentTime - process.waitingTime;
    }

    

//Approved by Professor AbdulRahman
    private void calculateWaitingTimeSJF(PCB process, int currentTime) {
        process.waitingTime = currentTime;
    }

    //Approved by Professor AbdulRahman
    private void calculateTurnaroundTimeSJF(PCB process) {
        process.turnaroundTime = process.burstTime + process.waitingTime;
    }
}