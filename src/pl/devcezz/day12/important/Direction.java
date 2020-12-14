package pl.devcezz.day12.important;

import java.util.Arrays;
import java.util.function.BiConsumer;

public enum Direction {

    N(((position, number) -> position.y += number)),
    S(((position, number) -> position.y -= number)),
    E(((position, number) -> position.x += number)),
    W(((position, number) -> position.x -= number)),
    L(((position, number) -> position.facing = position.facing.turn(-1, number))),
    R(((position, number) -> position.facing = position.facing.turn(1, number))),
    F(((position, number) -> {
        switch (position.facing) {
            case N:
                Direction.N.move.accept(position, number);
                break;
            case E:
                Direction.E.move.accept(position, number);
                break;
            case S:
                Direction.S.move.accept(position, number);
                break;
            case W:
                Direction.W.move.accept(position, number);
                break;
        }
    }));

    public BiConsumer<Position, Integer> move;

    Direction(BiConsumer<Position, Integer> move) {
        this.move = move;
    }

    public static Direction chooseDirection(String letter) {
        return Arrays.stream(values())
                .filter(direction -> direction.name().equalsIgnoreCase(letter))
                .findFirst()
                .orElse(N);
    }
}
