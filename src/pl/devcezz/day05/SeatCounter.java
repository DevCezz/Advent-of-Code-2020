package pl.devcezz.day05;

public class SeatCounter {

    public int countSeatId(Instruction instruction) {
        int row = countRow(instruction.horizontalInstruction);
        int column = countColumn(instruction.verticalInstruction);

        return column * 8 + row;
    }

    private int countRow(String rowInstruction) {
        Direction direction = Direction.HORIZONTAL;

        if (!direction.isValidInstruction(rowInstruction)) {
            throw new IllegalArgumentException("Invalid instruction for vertical direction");
        }

        return count(rowInstruction, direction);
    }

    private int countColumn(String columnInstruction) {
        Direction direction = Direction.VERTICAL;

        if (!direction.isValidInstruction(columnInstruction)) {
            throw new IllegalArgumentException("Invalid instruction for horizontal direction");
        }

        return count(columnInstruction, direction);
    }

    private int count(String instruction, Direction direction) {
        int min = 0;
        int max = (int) Math.pow(2, instruction.length()) - 1;

        for (char step : instruction.toCharArray()) {
            if (direction.isMaxDirection(step)) {
                max = (int) Math.floor((min + max) / 2);
            } else if (direction.isMinDirection(step)) {
                min = (int) Math.ceil((min + max) / 2) + 1;
            }
        }

        if (min == max) {
            return min;
        } else {
            throw new RuntimeException("Error in calculation for " + direction);
        }
    }
}
