package pl.devcezz.day12.important;

import java.util.Arrays;

public enum Facing {

    N(0), E(90), S(180), W(270);

    private int angle;

    Facing(int angle) {
        this.angle = angle;
    }

    private static Facing chooseFacing(int angle) {
        return Arrays.stream(values())
                .filter(facing -> facing.angle == angle)
                .findFirst()
                .orElse(N);
    }

    public Facing turn(int leftRigth, int angle) {
        int currentAngle = this.angle + (leftRigth * angle);

        if (currentAngle < 0) {
            currentAngle += 360;
        } else if (currentAngle > 360) {
            currentAngle -= 360;
        }

        return chooseFacing(currentAngle);
    }
}
