package pl.devcezz.day12.important;

public class Position {

    int x = 0;
    int y = 0;
    Facing facing = Facing.E;

    @Override
    public String toString() {
        return "x -> " + x + ", y -> " + y + ", facing -> " + facing;
    }

    public int count() {
        int positiveX = x > 0 ? x : x * -1;
        int positiveY = y > 0 ? y : y * -1;

        return positiveX + positiveY;
    }
}
