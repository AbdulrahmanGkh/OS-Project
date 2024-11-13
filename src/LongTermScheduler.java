import java.util.Queue;

public class LongTermScheduler extends Thread {
    private Queue<PCB> jobQueue;
    private Queue<PCB> readyQueue;

    public LongTermScheduler(Queue<PCB> jobQueue, Queue<PCB> readyQueue) {
        this.jobQueue = jobQueue;
        this.readyQueue = readyQueue;
    }

    @Override
    public void run() {
        while (true) {
            loadJobsToReadyQueue();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("LongTermScheduler interrupted.");
                break;
            }
        }
    }

    private void loadJobsToReadyQueue() {
        while (!jobQueue.isEmpty()) {
            PCB job = jobQueue.poll();
            job.state = "Ready";
            readyQueue.add(job);
            System.out.println("Job " + job.id + " loaded into Ready Queue.");
        }
    }
}
