package pl.devcezz.day05;

public class Instruction {

    public final String verticalInstruction;
    public final String horizontalInstruction;

    public Instruction(String instruction, int delimiter) {

        if (delimiter >= instruction.length()) {
            throw new IllegalArgumentException("Delimiter cannot be greater than length of instruction");
        }

        String firstInstruction = instruction.substring(0, delimiter).toUpperCase();
        String secondInstruction = instruction.substring(delimiter).toUpperCase();

        validateInstructions(firstInstruction, secondInstruction);

        this.verticalInstruction = firstInstruction;
        this.horizontalInstruction = secondInstruction;
    }

    private void validateInstructions(String firstInstruction, String secondInstruction) {
        if (!Direction.VERTICAL.isValidInstruction(firstInstruction)) {
            throw new IllegalArgumentException("Not valid instruction in vertical");
        }

        if (!Direction.HORIZONTAL.isValidInstruction(secondInstruction)) {
            throw new IllegalArgumentException("Not valid instruction in horizontal");
        }
    }
}