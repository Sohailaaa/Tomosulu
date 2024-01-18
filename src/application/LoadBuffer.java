package application;

public class LoadBuffer {


    private int latency;
    private int cyclesLeft;
    private boolean busy;
    private int address;
    private String tag;
    private double[] memory;
    private Bus bus;
    private Instruction currentInstruction;



    public LoadBuffer(String tag, double[] memory, int latency, Bus bus) {
        this.latency = latency;
        this.tag = tag;
        this.busy = false;
        this.bus = bus;
        this.memory = memory;
    }

    public void execute(int cycle) {
        if (!this.busy || currentInstruction.getIssueTime() == cycle)return;
        if (cyclesLeft == latency) {
            currentInstruction.setStartExecutionTime(cycle);
        }
        if (this.cyclesLeft > 0) {
            if (this.cyclesLeft == 1)
                currentInstruction.setEndExecutionTime(cycle);
            cyclesLeft--;
        } else {
            if (!bus.isValueWritten()) {
                this.busy = false;
                bus.writeValue(readFromMemory(), tag);
                currentInstruction.setWriteResult(cycle);
            }
        }

    }

    public double readFromMemory() {
        return memory[address];
    }

    public String toString() {
        return ("\n{Tag: " + tag + " Busy: " + busy + " Address: " + address + " Cycles Left: " + cyclesLeft + "}");
    }

    public void setBuffer(Instruction currentInstruction, int address) {
        this.currentInstruction = currentInstruction;
        this.cyclesLeft = latency;
        this.address = address;
        this.busy = true;
    }

    public int getLatency() {
        return latency;
    }

    public int getCyclesLeft() {
        return cyclesLeft;
    }

    public boolean isBusy() {
        return busy;
    }

    public int getAddress() {
        return address;
    }

    public String getTag() {
        return tag;
    }

    public double[] getMemory() {
        return memory;
    }

    public Bus getBus() {
        return bus;
    }
}
