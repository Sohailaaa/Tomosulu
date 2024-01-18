package application;

public class Instruction {
    private InstructionType type;
    private String op1;
    private String op2;
    private String dest;

    private int issueTime = -1;
    private int startExecutionTime = -1;
    private int endExecutionTime = -1;
    private int writeResult = -1;
    public int branchPC = -1;

    public Instruction(InstructionType type, String op1, String op2, String dest) {
        this.type = type;
        this.op1 = op1;
        this.op2 = op2;
        this.dest = dest;
    }
    public Instruction(Instruction instruction){
        this.type = instruction.type;
        this.op1 = instruction.op1;
        this.op2 = instruction.op2;
        this.dest = instruction.dest;

    }

    public InstructionType getType() {
        return type;
    }

    public String getOp1() {
        return op1;
    }

    public String getOp2() {
        return op2;
    }

    public String getDest() {
        return dest;
    }


    public int getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(int issueTime) {
        this.issueTime = issueTime;
    }

    public int getStartExecutionTime() {
        return startExecutionTime;
    }

    public void setStartExecutionTime(int startExecutionTime) {
        this.startExecutionTime = startExecutionTime;
    }

    public int getEndExecutionTime() {
        return endExecutionTime;
    }

    public void setEndExecutionTime(int endExecutionTime) {
        this.endExecutionTime = endExecutionTime;
    }

    public int getWriteResult() {
        return writeResult;
    }

    public void setWriteResult(int writeResult) {
        this.writeResult = writeResult;
    }

    public String toString() {
        return ("\n{Type: " + type.toString() + " Op 1: " + op1 + " Op 2: " + op2 + " Dest: " + dest + " Issued at: " + issueTime + " Started execution at: " + startExecutionTime + " Finished execution at: " + endExecutionTime + " Wrote back at: " + writeResult + "}");
    }
}
