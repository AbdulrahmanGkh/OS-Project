
public class LongTermScheuduler {

    private Queue<PCB> readyQueue;

    public Scheduler(Queue<PCB> readyQueue) {
        this.readyQueue = readyQueue;
    }

    public void scheduleFCFS() {
        int currentTime = 0;
        for (PCB pcb : readyQueue) {
            pcb.waitingTime = currentTime;
            pcb.turnaroundTime = currentTime + pcb.burstTime;
            currentTime += pcb.burstTime;
            System.out.println("Process " + pcb.processId + " starts at " + pcb.waitingTime 
                                + " and finishes at " + pcb.turnaroundTime);
        }

        ...
    }
}  
