package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

public class CPU {
    public static int PC = 0;
    private Instruction[] instructionInfo;
    int cycle;
    private Bus bus;
    private double[] memory;
    private ReservationStation adderStation;
    private ReservationStation multiplierStation;
    public static ArrayList<Instruction> instructionQueue;
    private StoreBuffer[] storeBuffers;
    private LoadBuffer[] loadBuffers;
    private RegisterFile registerFile;
    private Interpreter interpreter;
    private boolean allInstructionsIssued = false;
    private boolean allInstructionsExecuted = false;
    public static boolean stopIssuing = false;


    public CPU(String instructions, int mulLatency, int addLatency, int subLatency, int divLatency, int loadLatency, int storeLatency, int addStation, int mulStation, int loadBufferin, int storeBufferin) {
        cycle = 1;
        bus = new Bus();
        memory = new double[100];
        memory[0] = -4;
        memory[1] = 8;
        interpreter = new Interpreter();
        instructionQueue = interpreter.loadInstructions(instructions);
        instructionInfo = new Instruction[instructionQueue.size()];
        int j = 0;
        for (Instruction inst : instructionQueue) {
            instructionInfo[j++] = inst;
        }
        adderStation = new ReservationStation(addStation, bus, subLatency, addLatency, divLatency, mulLatency, InstructionType.ADD);
        multiplierStation = new ReservationStation(mulStation, bus, subLatency, addLatency, divLatency, mulLatency, InstructionType.MULTIPLY);
        storeBuffers = new StoreBuffer[storeBufferin];
        loadBuffers = new LoadBuffer[loadBufferin];
        for (int i = 0; i < storeBufferin; i++) {
            storeBuffers[i] = new StoreBuffer("S" + (i + 1), memory, bus, storeLatency);
        }
        for (int i = 0; i < loadBufferin; i++) {
            loadBuffers[i] = new LoadBuffer("L" + (i + 1), memory, loadLatency, bus);
        }
        registerFile = new RegisterFile();
    }

    public String runCycle() {
        StringBuilder result = new StringBuilder();

        result.append("Cycle: ").append(cycle).append("\n");
        result.append("Instruction Queue: \n").append(Arrays.toString(instructionQueue.toArray())).append("\n");
        //result.append("Instruction Info: \n").append(Arrays.toString(instructionInfo)).append("\n");
        result.append("Adder Registration Station: \n").append(Arrays.toString(adderStation.getStations())).append("\n");
        result.append("Multiplier Registration Station: \n").append(Arrays.toString(multiplierStation.getStations())).append("\n");
        result.append("Load Buffers: \n").append(Arrays.toString(loadBuffers)).append("\n");
        result.append("Store Buffers: \n").append(Arrays.toString(storeBuffers)).append("\n");
        result.append("Register File: \n").append(Arrays.toString(registerFile.getRegisters())).append("\n");
        result.append("Memory: \n").append(Arrays.toString(memory)).append("\n");
        result.append("Bus Value: <").append(bus.getTag()).append(",").append(bus.getValue()).append(">").append("\n\n");

        if (PC < instructionQueue.size()) {
            Instruction currentInstruction = instructionQueue.get(PC);
            issue(currentInstruction);
        } else {
            allInstructionsIssued = true;
        }

        if (!allStationsEmpty()) {
            execute();
        } else {
            allInstructionsExecuted = true;
        }

        result.append("Register File After Execute: \n").append(Arrays.toString(registerFile.getRegisters())).append("\n");
        updateBus();
        cycle++;

        return result.toString();
    }

    private boolean allStationsEmpty() {
        for (FunctionalUnit unit : adderStation.getStations()) {
            if (unit.isBusy()) {
                return false;
            }
        }
        for (FunctionalUnit unit : multiplierStation.getStations()) {
            if (unit.isBusy()) {
                return false;
            }
        }
        for (LoadBuffer loadBuffer : loadBuffers) {
            if (loadBuffer.isBusy()) {
                return false;
            }
        }
        for (StoreBuffer storeBuffer : storeBuffers) {
            if (storeBuffer.isBusy()) {
                return false;
            }
        }
        return true;
    }

    private void issue(Instruction currentInstruction) {
        InstructionType type = currentInstruction.getType();
        if(stopIssuing){return;}
        if(type.equals(InstructionType.BNEZ)){ stopIssuing = true;}
        switch (type) {
            case ADD, SUB -> {
                FunctionalUnit temp;
                if ((temp = adderStation.issueFunctionalUnit()) != null) {
                    Instruction currentInstructionReference = instructionQueue.get(PC);
                    PC++;
                    currentInstructionReference.setIssueTime(cycle);
                    String op1 = currentInstruction.getOp1();
                    String op2 = currentInstruction.getOp2();
                    String qj = "";
                    String qk = "";
                    double vj = 0;
                    double vk = 0;
                    String dest = currentInstruction.getDest();
                    if (registerFile.isReady(op1)) {
                        vj = registerFile.getRegisterContent(op1);
                    } else {
                        qj = registerFile.getRegisterQi(op1);
                    }
                    if (registerFile.isReady(op2)) {
                        vk = registerFile.getRegisterContent(op2);
                    } else {
                        qk = registerFile.getRegisterQi(op2);
                    }
                    temp.setUnit(currentInstructionReference, type, vj, vk, qj, qk);
                    registerFile.setRegisterQi(dest, temp.getTag());
                }
                break;
            }
            case MULTIPLY, DIVIDE -> {
                FunctionalUnit temp;
                if ((temp = multiplierStation.issueFunctionalUnit()) != null) {
                    Instruction currentInstructionReference = instructionQueue.get(PC);
                    PC++;
                    currentInstructionReference.setIssueTime(cycle);
                    String op1 = currentInstruction.getOp1();
                    String op2 = currentInstruction.getOp2();
                    String qj = "";
                    String qk = "";
                    double vj = 0;
                    double vk = 0;
                    String dest = currentInstruction.getDest();
                    if (registerFile.isReady(op1)) {
                        vj = registerFile.getRegisterContent(op1);
                    } else {
                        qj = registerFile.getRegisterQi(op1);
                    }
                    if (registerFile.isReady(op2)) {
                        vk = registerFile.getRegisterContent(op2);
                    } else {
                        qk = registerFile.getRegisterQi(op2);
                    }
                    temp.setUnit(currentInstructionReference, type, vj, vk, qj, qk);
                    registerFile.setRegisterQi(dest, temp.getTag());
                }
                break;
            }
            case LOAD -> {
                LoadBuffer temp;
                if ((temp = issueLoadBuffer()) != null) {
                    Instruction currentInstructionReference = instructionQueue.get(PC);
                    PC++;
                    currentInstructionReference.setIssueTime(cycle);
                    String op1 = currentInstruction.getOp1();
                    registerFile.setRegisterQi(op1, temp.getTag());
                    int address = Integer.parseInt(currentInstruction.getDest());
                    temp.setBuffer(currentInstructionReference, address);
                }
                break;
            }
            case STORE -> {
                StoreBuffer temp;
                if ((temp = issueStoreBuffer()) != null) {
                    Instruction currentInstructionReference = instructionQueue.get(PC);
                    PC++;
                    currentInstructionReference.setIssueTime(cycle);
                    String op1 = currentInstruction.getOp1();
                    double v = 0;
                    String q = "";
                    if (registerFile.isReady(op1)) {
                        v = registerFile.getRegisterContent(op1);
                    } else {
                        q = registerFile.getRegisterQi(op1);
                    }
                    int address = Integer.parseInt(currentInstruction.getDest());
                    temp.setBuffer(currentInstructionReference, address, v, q);
                }
                break;

            }
            case BNEZ -> {
                FunctionalUnit temp;
                if ((temp = adderStation.issueFunctionalUnit()) != null) {
                    Instruction currentInstructionReference = instructionQueue.get(PC);
                    currentInstructionReference.branchPC = PC;
                    PC++;
                    currentInstructionReference.setIssueTime(cycle);
                    String op1 = currentInstruction.getOp1();
                    String qj = "";
                    double vj = 0;
                    String dest = currentInstruction.getDest();
                    if (registerFile.isReady(op1)) {
                        vj = registerFile.getRegisterContent(op1);
                    } else {
                        qj = registerFile.getRegisterQi(op1);
                    }
                    int vk = Integer.parseInt(dest);
                    temp.setUnit(currentInstructionReference, type, vj, vk, qj, "");
                }
            }
            case ADDI -> {
                FunctionalUnit temp;
                if ((temp = adderStation.issueFunctionalUnit()) != null) {
                    Instruction currentInstructionReference = instructionQueue.get(PC); PC++;
                    currentInstructionReference.setIssueTime(cycle);
                    String op1 = currentInstruction.getOp1();
                    String op2 = currentInstruction.getOp2();
                    String qj = "";
                    String qk = "";
                    double vj = 0;
                    double vk = Double.parseDouble(op2);
                    String dest = currentInstruction.getDest();
                    if (registerFile.isReady(op1)) {
                        vj = registerFile.getRegisterContent(op1);
                    } else {
                        qj = registerFile.getRegisterQi(op1);
                    }
                    temp.setUnit(currentInstructionReference, type, vj, vk, qj, qk);
                    registerFile.setRegisterQi(dest, temp.getTag());
                }
                break;
            }

        }
    }

    private void execute() {
        for (LoadBuffer loadBuffer : loadBuffers) {
            loadBuffer.execute(cycle);
        }
        for (StoreBuffer storeBuffer : storeBuffers) {
            storeBuffer.execute(cycle);
        }
        for (FunctionalUnit unit : adderStation.getStations()) {
            unit.execute(cycle);
        }
        for (FunctionalUnit unit : multiplierStation.getStations()) {
            unit.execute(cycle);
        }


    }

    private void updateBus() {
        bus.setIsValueWritten(false);
        for (StoreBuffer storeBuffer : storeBuffers) {
            if (storeBuffer.isBusy() && storeBuffer.getQ().equals(bus.getTag())) {
                storeBuffer.setV(bus.getValue());
                storeBuffer.setQ("");
            }
        }
        for (FunctionalUnit unit : adderStation.getStations()) {
            if (unit.isBusy() && bus.getTag().equals(unit.getQj())) {
                unit.setVj(bus.getValue());
                unit.setQj("");
            }
            if (unit.isBusy() && bus.getTag().equals(unit.getQk())) {
                unit.setVk(bus.getValue());
                unit.setQk("");
            }
        }
        for (FunctionalUnit unit : multiplierStation.getStations()) {
            if (unit.isBusy() && bus.getTag().equals(unit.getQj())) {
                unit.setVj(bus.getValue());
                unit.setQj("");
            }
            if (unit.isBusy() && bus.getTag().equals(unit.getQk())) {
                unit.setVk(bus.getValue());
                unit.setQk("");
            }
        }
        for (Register register : registerFile.getRegisters()) {
            if (register.getQi() != "") {
                if (register.getQi().equals(bus.getTag())) {
                    register.setContent(bus.getValue());
                    register.setQi("");
                }
            }
        }
        bus.setTag("Balabeezo");
        bus.setContent(0);
    }

    private LoadBuffer issueLoadBuffer() {
        for (LoadBuffer loadBuffer : loadBuffers) {
            if (!loadBuffer.isBusy()) {
                return loadBuffer;
            }
        }
        return null;
    }

    private StoreBuffer issueStoreBuffer() {
        for (StoreBuffer storeBuffer : storeBuffers) {
            if (!storeBuffer.isBusy()) {
                return storeBuffer;
            }
        }
        return null;
    }

    public boolean isAllInstructionsIssued() {
        return allInstructionsIssued;
    }

    public boolean isAllInstructionsExecuted() {
        return allInstructionsExecuted;
    }
}
