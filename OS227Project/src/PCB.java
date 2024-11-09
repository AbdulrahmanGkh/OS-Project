class PCB {
    int processId;
    int burstTime;
    int memoryRequired;
    int waitingTime;
    int turnaroundTime;
    String state;

   
    public PCB(int processId, int burstTime, int memoryRequired) {
        this.processId = processId;
        this.burstTime = burstTime;
        this.memoryRequired = memoryRequired;
        this.state = "New"; 
    }
    

}