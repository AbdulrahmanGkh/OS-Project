public class PCB {
    int id;                  // Process ID
    int burstTime;           // Execution time (in milliseconds)
    int remainingTime;       // Remaining burst time (for RR and SJF)
    int memoryRequired;      // Memory required in MB
    int waitingTime;         // Waiting time
    int turnaroundTime;      // Turnaround time
    String state;            // State of the process (e.g., "Ready", "Running")

    public PCB(int id, int burstTime, int memoryRequired) {
        this.id = id;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.memoryRequired = memoryRequired;
        this.state = "New"; // Initial state
    }
}
