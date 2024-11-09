public class PCB {
	
		int id;                  // Process ID
	    int burstTime;           // Execution time (in milliseconds)
	    int memoryRequired;      // Memory required in MB
	    int waitingTime;
	    int turnaroundTime;
	    String state;            // State of the process (e.g., "ready", "running"), initiated as "new".

	PCB(int id, int burstTime, int memoryRequired) {
		
	    this.id = id;
	    this.burstTime = burstTime;
	    this.memoryRequired = memoryRequired;
	    this.state = "new"; // initial state
	    
	    }
}
