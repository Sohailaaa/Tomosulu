package application;

public class StoreBuffer {
    private int cyclesLeft;
    private int latency;
    private Bus bus;
    private boolean busy;
    private int address;
    private double v;
    private String q;
    private String tag;
    private double[] memory;

    private Instruction currentInstruction;

    public StoreBuffer(String tag, double[] memory, Bus bus, int latency) {
        this.latency = latency;
        this.bus = bus;
        this.tag = tag;
        this.busy = false;
        this.memory = memory;
    }

    public String toString() {
        return ("\n{Tag: " + (tag != null ? tag : "") + " Busy: " + busy + " Address: " + address + " Value: " + v + " Unit: " + (q != null ? q : "") + " Cycles Left: " + cyclesLeft + "}");
    }

    public boolean isBusy() {
        return busy;
    }

    public void writeToMemory() {
        memory[address] = v;
    }

    public void setBuffer(Instruction currentInstruction, int address, double value, String tag) {
        this.currentInstruction = currentInstruction;
        this.cyclesLeft = latency;
        this.v = value;
        this.q = tag;
        this.address = address;
        this.busy = true;
    }

    public void execute(int cycle) {
        if (!this.busy || currentInstruction.getIssueTime() == cycle)
            return;
        if (q == "") {
            if (cyclesLeft == latency) {
                currentInstruction.setStartExecutionTime(cycle);
            }
            if (this.cyclesLeft > 0) {
                if (cyclesLeft == 1) {
                    currentInstruction.setEndExecutionTime(cycle);
                    writeToMemory();
                }
                cyclesLeft--;
            } else {
                    this.busy = false;
                    currentInstruction.setWriteResult(cycle);
            }
        }
    }

    public int getCyclesLeft() {
        return cyclesLeft;
    }

    public int getLatency() {
        return latency;
    }

    public Bus getBus() {
        return bus;
    }

    public int getAddress() {
        return address;
    }

    public double getValue() {
        return v;
    }

    public String getTag() {
        return tag;
    }

    public double[] getMemory() {
        return memory;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public double getV() {
        return v;
    }

    public void setV(double v) {
        this.v = v;
    }
}
