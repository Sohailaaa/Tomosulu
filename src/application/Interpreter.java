package application;

import java.util.ArrayList;
import java.util.Scanner; // Import the Scanner class to read text files

public class Interpreter {
    private ArrayList<Instruction> instructionQueue;
    private Parser parser;

    public Interpreter() {
        this.parser = new Parser();
        this.instructionQueue = new ArrayList<Instruction>();
    }


    public ArrayList<Instruction> loadInstructions(String instructions) {
        Scanner myReader = new Scanner(instructions);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            instructionQueue.add(parser.parse(data));
        }
        myReader.close();
        return instructionQueue;
    }
}
