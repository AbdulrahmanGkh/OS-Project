import java.util.*;

public class LoadToReadyQueue extends Thread {
    private Queue<PCB> jobQueue;
    private Queue<PCB> readyQueue;
    private MemoryManagment memory;
    private int schedulingChoice; // خيار الجدولة
    private int quantum; // يستخدم في Round Robin فقط

    public LoadToReadyQueue(Queue<PCB> jobQueue, Queue<PCB> readyQueue, MemoryManagment memory, int schedulingChoice, int quantum) {
        this.jobQueue = jobQueue;
        this.readyQueue = readyQueue;
        this.memory = memory;
        this.schedulingChoice = schedulingChoice; // 1 = FCFS, 2 = SJF, 3 = RR
        this.quantum = quantum; // افتراضي لـ Round Robin
    }

    @Override
    public void run() {
        synchronized (jobQueue) {
            switch (schedulingChoice) {
                case 1: // FCFS
                    loadFCFS();
                    break;
                case 2: // SJF
                    loadSJF();
                    break;
                case 3: // Round Robin
                    loadRR();
                    break;
                default:
                    System.out.println("Invalid scheduling choice.");
            }
        }
    }

    // FCFS Loader
    private void loadFCFS() {
        System.out.println("Loading jobs using FCFS...");
        while (!jobQueue.isEmpty()) {
            PCB job = jobQueue.peek();  // Access job from the copied queue

            // Check if memory is available and load the job
            if (job.memoryRequired <= memory.getAvailableMemory()) {
                jobQueue.remove(); // Remove the job from tempQueue, not jobQueue
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

    // SJF Loader
    private void loadSJF() {
        System.out.println("Loading jobs using SJF...");
        PriorityQueue<PCB> sjfQueue = new PriorityQueue<>(Comparator.comparingInt(p -> p.burstTime));

        // نقل العمليات إلى PriorityQueue
        sjfQueue.addAll(jobQueue);
        jobQueue.clear();

        while (!sjfQueue.isEmpty()) {
            PCB job = sjfQueue.poll(); // استخراج العملية ذات أقل Burst Time

            if (job.memoryRequired <= memory.getAvailableMemory()) {
                readyQueue.add(job);
                memory.allocateMemory(job.memoryRequired);
                job.changeState("READY");
                System.out.println("Job " + job.id + " loaded for SJF. Remaining Memory: " + memory.getAvailableMemory() + " MB.");
            } else {
                try {
                    System.out.println("Not enough memory for Job " + job.id + ". Waiting...");
                    jobQueue.wait(); // انتظار تحرير الذاكرة
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    // Round Robin Loader
    private void loadRR() {
        System.out.println("Loading jobs using Round Robin with Quantum " + quantum + "...");
        while (!jobQueue.isEmpty()) {
            PCB job = jobQueue.poll();

            if (job.memoryRequired <= memory.getAvailableMemory()) {
                readyQueue.add(job);
                memory.allocateMemory(job.memoryRequired);
                job.changeState("READY");
                System.out.println("Job " + job.id + " loaded for Round Robin. Remaining Memory: " + memory.getAvailableMemory() + " MB.");
            } else {
                try {
                    System.out.println("Not enough memory for Job " + job.id + ". Waiting...");
                    jobQueue.wait(); // انتظار تحرير الذاكرة
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }
}
