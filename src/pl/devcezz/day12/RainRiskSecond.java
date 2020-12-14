package pl.devcezz.day12;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RainRiskSecond {

    public static List<String> readData(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Not found file: " + path);
        }
    }

    public static void main(String[] args) {
        List<ShipInstruction> instructions = readData("data/day12/task.txt").stream()
                .map(ShipInstruction::new)
                .collect(Collectors.toList());
        Position position = new Position();

        instructions.forEach(position::applyInstruction);

        System.out.println(position);
        System.out.println(position.count());
    }
}

class Position {

    int x = 0;
    int y = 0;
    Waypoint waypoint = new Waypoint();

    @Override
    public String toString() {
        return "x -> " + x + ", y -> " + y;
    }

    public int count() {
        int positiveX = x > 0 ? x : x * -1;
        int positiveY = y > 0 ? y : y * -1;

        return positiveX + positiveY;
    }

    public void applyInstruction(ShipInstruction instruction) {
        if (instruction.turn == Turn.NULL && instruction.course == Course.NULL) {
            if (waypoint.first.isVertical()) {
                switch (waypoint.first.course) {
                    case N:
                        y += instruction.number * waypoint.first.number;
                        break;
                    case S:
                        y -= instruction.number * waypoint.first.number;
                        break;
                }
            } else {
                switch (waypoint.first.course) {
                    case E:
                        x += instruction.number * waypoint.first.number;
                        break;
                    case W:
                        x -= instruction.number * waypoint.first.number;
                        break;
                }
            }

            if (waypoint.second.isVertical()) {
                switch (waypoint.second.course) {
                    case N:
                        y += instruction.number * waypoint.second.number;
                        break;
                    case S:
                        y -= instruction.number * waypoint.second.number;
                        break;
                }
            } else {
                switch (waypoint.second.course) {
                    case E:
                        x += instruction.number * waypoint.second.number;
                        break;
                    case W:
                        x -= instruction.number * waypoint.second.number;
                        break;
                }
            }
        } else if (instruction.turn != Turn.NULL) {
            waypoint.turnWaypoint(instruction.turn, instruction.number);
        } else {
            waypoint.move(instruction.course, instruction.number);
        }
    }
}

class Waypoint {

    Pair first = new Pair(Course.N, 1);
    Pair second = new Pair(Course.E, 10);

    public void turnWaypoint(Turn turn, int number) {
        first = new Pair(first.course.turn(turn, number), first.number);
        second = new Pair(second.course.turn(turn, number), second.number);
    }

    public void move(Course course, int number) {
        if ((first.course.angle % 180 == 0 && course.angle % 180 == 0) || (first.course.angle % 180 == 90 && course.angle % 180 == 90)) {
            if (first.course == course) {
                first.number += number;
            } else {
                first.number -= number;
            }

            if (first.number < 0) {
                first = new Pair(Course.getOpposite(first.course), -first.number);
            }
        } else {
            if (second.course == course) {
                second.number += number;
            } else {
                second.number -= number;
            }

            if (second.number < 0) {
                second = new Pair(Course.getOpposite(second.course), -second.number);
            }
        }
    }

    static class Pair {

        Course course;
        int number;

        public Pair(Course course, int number) {
            this.course = course;
            this.number = number;
        }

        public boolean isVertical() {
            return course == Course.N || course == Course.S;
        }

        public boolean isHorizontal() {
            return course == Course.E || course == Course.W;
        }
    }
}

class ShipInstruction {

    Course course = Course.NULL;
    Turn turn = Turn.NULL;
    int number = 0;

    public ShipInstruction(String instruction) {
        String letter = instruction.substring(0, 1);
        String value = instruction.substring(1);

        this.course = Course.chooseDirectionByName(letter);
        if (this.course == Course.NULL) {
            this.turn = Turn.chooseTurnByName(letter);
        }
        this.number = Integer.parseInt(value);
    }
}

enum Course {

    N(0), E(90), S(180), W(270), NULL(-1);

    int angle;

    public static Course getOpposite(Course course) {
        switch (course) {
            case N:
                return S;
            case E:
                return W;
            case S:
                return N;
            case W:
                return E;
        }

        return NULL;
    }

    Course(int angle) {
        this.angle = angle;
    }

    public static Course chooseDirectionWaypoint(int angle) {
        return Arrays.stream(values())
                .filter(course -> course.angle == angle)
                .findFirst()
                .orElse(NULL);
    }

    public static Course chooseDirectionByName(String name) {
        return Arrays.stream(values())
                .filter(course -> course.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(NULL);
    }

    public Course turn(Turn turn, int number) {
        int newCourse = this.angle + turn.leftRight * number;

        if (newCourse < 0) {
            newCourse += 360;
        } else if (newCourse >= 360) {
            newCourse -= 360;
        }

        return chooseDirectionWaypoint(newCourse);
    }
}

enum Turn {

    R(1), L(-1), NULL(0);

    int leftRight;

    Turn(int leftRight) {
        this.leftRight = leftRight;
    }

    public static Turn chooseTurnByName(String name) {
        return Arrays.stream(values())
                .filter(turn -> turn.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(NULL);
    }
}
























