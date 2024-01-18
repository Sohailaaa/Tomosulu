package application;

import java.util.Arrays;
import java.util.Locale;

public class Parser {


    public static Instruction parse(String line) {
        String[] tokens = line.split(" ");
        InstructionType type = null;
        String op1="",op2="",dest="";
        String instructionName = tokens[0].split("\\.")[0];
        String operands[] = tokens[1].split(",");
        if (instructionName.equalsIgnoreCase("add")) {
            type = InstructionType.ADD;
            op1 = operands[1];
            op2 = operands[2];
            dest = operands[0];
        }
        if(instructionName.equalsIgnoreCase("sub")){
            type = InstructionType.SUB;
            op1 = operands[1];
            op2 = operands[2];
            dest = operands[0];
        }
        if(instructionName.equalsIgnoreCase("mul")){
            type = InstructionType.MULTIPLY;
            op1 = operands[1];
            op2 = operands[2];
            dest = operands[0];
        }
        if(instructionName.equalsIgnoreCase("div")){
            type = InstructionType.DIVIDE;
            op1 = operands[1];
            op2 = operands[2];
            dest = operands[0];
        }
        if(instructionName.equalsIgnoreCase("l")){
            type = InstructionType.LOAD;
            op1 = operands[0];
            op2 = "";
            dest = operands[1];
        }
        if(instructionName.equalsIgnoreCase("s")){
            type = InstructionType.STORE;
            op1 = operands[0];
            op2 = "";
            dest = operands[1];
        }
        if(instructionName.equalsIgnoreCase("bnez")){
            type = InstructionType.BNEZ;
            op1 = operands[0];
            op2 = "";
            dest = operands[1];
        }
        if(instructionName.equalsIgnoreCase("addi")){
            type = InstructionType.ADDI;
            op1 = operands[1];
            op2 = operands[2];
            dest = operands[0];
        }
        return new Instruction(type, op1, op2, dest);
    }
}
