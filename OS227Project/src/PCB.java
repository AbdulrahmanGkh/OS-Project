public class PCB {
    int id;
    int burstTime;           // وقت التنفيذ الكلي
    int remainingTime;       // الوقت المتبقي (لـ Round-Robin)
    int memoryRequired;      // الذاكرة المطلوبة
    int turnaroundTime;      // وقت الاستجابة
    String state;            // حالة العملية (NEW, READY, RUNNING, TERMINATED)

    public PCB(int id, int burstTime, int memoryRequired) {
        this.id = id;
        this.burstTime = burstTime;
        this.remainingTime = burstTime; // في البداية، الوقت المتبقي يساوي وقت التنفيذ الكلي
        this.memoryRequired = memoryRequired;
        this.turnaroundTime = 0;
        this.state = "NEW"; // الحالة الافتراضية
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }
}
