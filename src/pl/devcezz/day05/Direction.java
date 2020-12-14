package pl.devcezz.day05;

public enum Direction {

    VERTICAL('B', 'F'),
    HORIZONTAL('R', 'L');

    public final char min;
    public final char max;

    Direction(char min, char max) {
        this.min = min;
        this.max = max;
    }

    public boolean isValidInstruction(String instruction) {
        String regex = String.format("^[%s%s]+$", min, max);
        return instruction.matches(regex);
    }
    
    public boolean isMinDirection(char direction) {
        return min == direction;
    }

    public boolean isMaxDirection(char direction) {
        return max == direction;
    }
}
