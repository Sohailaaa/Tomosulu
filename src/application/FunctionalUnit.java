package application;

import java.util.ArrayList;

public class FunctionalUnit {

    private int divideLatency;
    private int subLatency;
    private int mulLatency;
    private int addLatency;
    private int latency;
    private InstructionType op;
    private int cyclesLeft;
    private boolean busy;
    private String tag;
    private Bus bus;
    private double vj;
    private double vk;
    private String qj;
    private String qk;

    private Instruction currentInstruction;


    public FunctionalUnit(int subLatency, int addLatency, int divideLatency, int mulLatency, String tag, Bus bus) {
        this.tag = tag;
        this.divideLatency = divideLatency;
        this.addLatency = addLatency;
        this.subLatency = subLatency;
        this.mulLatency = mulLatency;
        this.busy = false;
        this.bus = bus;
    }

    public void setUnit(Instruction currentInstruction, InstructionType op, double vj, double vk, String qj, String qk) {
        this.currentInstruction = currentInstruction;
        this.busy = true;
        this.op = op;
        this.vj = vj;
        this.vk = vk;
        this.qj = qj;
        this.qk = qk;
        switch (op) {
            case MULTIPLY -> {
                latency = mulLatency;
                break;
            }
            case DIVIDE -> {
                latency = divideLatency;
                break;
            }
            case SUB -> {
                latency = subLatency;
                break;
            }
            case ADD -> {
                latency = addLatency;
                break;
            }
            case BNEZ , ADDI -> {
                latency = 1;
                break;
            }

        }
        this.cyclesLeft = latency;
    }

    public String toString() {
        return ("\n{Tag: " + tag + " Busy: " + busy + " Op: " + (op != null ? op.toString() : "") + " Vj: " + vj + " Vk: " + vk + " Qj: " + (qj != null ? qj : "") + " Qk: " + (qk != null ? qk : "") + " Cycles Left: " + cyclesLeft + "}");
    }

    public void execute(int cycle) {
        if (!this.busy || currentInstruction.getIssueTime() == cycle)
            return;
        if (qj == "" && qk == "") {
            if (cyclesLeft == latency) {
                currentInstruction.setStartExecutionTime(cycle);
            }
            if (this.cyclesLeft > 0) {
                if (cyclesLeft == 1) {
                    currentInstruction.setEndExecutionTime(cycle);
                }
                cyclesLeft--;
            } else {
                if (!bus.isValueWritten()) {
                    this.busy = false;
                    switch (op) {
                        case ADD , ADDI -> {
                            writeToBus(tag, vj + vk);
                            break;
                        }
                        case SUB -> {
                            writeToBus(tag, vj - vk);
                            break;
                        }
                        case DIVIDE -> {
                            writeToBus(tag, vj / vk);
                            break;
                        }
                        case MULTIPLY -> {
                            writeToBus(tag, vj * vk);
                            break;
                        }
                        case BNEZ -> {
                            CPU.stopIssuing = false;
                            if(vj != 0) {
                                if(vk < currentInstruction.branchPC) {
                                    int end = currentInstruction.branchPC;
                                    ArrayList<Instruction> newInstructions = new ArrayList<Instruction>();
                                    for (int i = (int) vk; i <= end; i++) {
                                        newInstructions.add(new Instruction(CPU.instructionQueue.get(i)));
                                    }
                                    int insertIndex = CPU.PC;
                                    for (Instruction instruction : newInstructions) {
                                        CPU.instructionQueue.add(insertIndex, instruction);
                                        insertIndex++;
                                    }
                                }
                                else{
                                    CPU.PC = (int) vk;
                                    System.out.println("tracer");
                                }
                            }
                            break;
                        }
                    }
                    currentInstruction.setWriteResult(cycle);
                }

            }
        }

    }

    public int getLatency() {
        return latency;
    }

    public InstructionType getOp() {
        return op;
    }

    public int getCyclesLeft() {
        return cyclesLeft;
    }

    public boolean isBusy() {
        return busy;
    }

    public String getTag() {
        return tag;
    }

    public Bus getBus() {
        return bus;
    }

    public double getVj() {
        return vj;
    }

    public double getVk() {
        return vk;
    }

    public String getQj() {
        return qj;
    }

    public String getQk() {
        return qk;
    }

    public void setVj(double vj) {
        this.vj = vj;
    }

    public void setVk(double vk) {
        this.vk = vk;
    }

    public void setQj(String qj) {
        this.qj = qj;
    }

    public void setQk(String qk) {
        this.qk = qk;
    }

    public void writeToBus(String tag, double value) {
        bus.writeValue(value, tag);
    }
}
