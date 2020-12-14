package pl.devcezz.day12.important;

public class Instruction {

    Direction direction;
    int number;

    public Instruction(String line) {
        String course = line.substring(0, 1);
        String movement = line.substring(1);

        this.direction = Direction.chooseDirection(course);
        this.number = Integer.parseInt(movement);
    }

    public void apply(Position position) {
        direction.move.accept(position, number);
    }
}
