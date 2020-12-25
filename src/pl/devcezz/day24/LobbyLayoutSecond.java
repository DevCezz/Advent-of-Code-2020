package pl.devcezz.day24;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class LobbyLayoutSecond {

    private static Map<Coordinates, Boolean> TILES = new HashMap<>();

    public static List<String> readData(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Not found file: " + path);
        }
    }

    public static void main(String[] args) {
        List<String> instructions = readData("data/day24/task.txt");

        for (String instruction : instructions) {
            double x = 0;
            double y = 0;
            while (instruction.length() > 0) {
                if (instruction.startsWith("nw")) {
                    x -= 0.5;
                    y++;
                    instruction = instruction.substring(2);
                } else if (instruction.startsWith("ne")) {
                    x += 0.5;
                    y++;
                    instruction = instruction.substring(2);
                } else if (instruction.startsWith("w")) {
                    x--;
                    instruction = instruction.substring(1);
                } else if (instruction.startsWith("e")) {
                    x++;
                    instruction = instruction.substring(1);
                } else if (instruction.startsWith("sw")) {
                    x -= 0.5;
                    y--;
                    instruction = instruction.substring(2);
                } else if (instruction.startsWith("se")) {
                    x += 0.5;
                    y--;
                    instruction = instruction.substring(2);
                }
            }
            Coordinates coordinates = new Coordinates(x, y);
            if (TILES.containsKey(coordinates)) {
                boolean isBlack = TILES.get(coordinates);
                if (isBlack) {
                    TILES.put(coordinates, false);
                } else {
                    TILES.put(coordinates, true);
                }
            } else {
                TILES.put(coordinates, true);
            }
        }

        double maxX = TILES.keySet().stream()
                .map(coordinates -> coordinates.x)
                .max(Double::compareTo)
                .orElse(-1.0) + 1;
        double maxY = TILES.keySet().stream()
                .map(coordinates -> coordinates.y)
                .max(Double::compareTo)
                .orElse(-1.0) + 1;
        double minX = TILES.keySet().stream()
                .map(coordinates -> coordinates.x)
                .min(Double::compareTo)
                .orElse(-1.0) - 1;
        double minY = TILES.keySet().stream()
                .map(coordinates -> coordinates.y)
                .min(Double::compareTo)
                .orElse(-1.0) - 1;

        if (String.valueOf(minX).endsWith(".5")) {
            minX -= 0.5;
        }
        if (String.valueOf(maxX).endsWith(".5")) {
            maxX -= 0.5;
        }

        for (int i = 1; i <= 100; i++) {
            Map<Coordinates, Boolean> newTiles = new HashMap<>();

            for (double y = minY; y <= maxY; y++) {
                for (double x = minX; x <= maxX; x++) {
                    Coordinates coordinates;
                    if (Math.abs(y) % 2 == 1) {
                        coordinates = new Coordinates(x - 0.5, y);
                    } else {
                        coordinates = new Coordinates(x, y);
                    }

                    Boolean isBlack = TILES.getOrDefault(coordinates, false);
                    long blacks = coordinates.nearCoordinates().stream()
                            .filter(oldCoordinates -> TILES.containsKey(oldCoordinates))
                            .filter(oldCoordinates -> TILES.get(oldCoordinates))
                            .count();

                    if (isBlack) {
                        if (blacks == 0 || blacks > 2) {
                            newTiles.put(coordinates, false);
                        } else {
                            newTiles.put(coordinates, true);
                        }
                    } else {
                        if (blacks == 2) {
                            newTiles.put(coordinates, true);
                        } else {
                            newTiles.put(coordinates, false);
                        }
                    }
                }
            }

            TILES = newTiles;
            maxX++;
            minX--;
            maxY++;
            minY--;
        }

        long result = TILES.values().stream()
                .filter(isBlack -> isBlack)
                .count();

        System.out.println(result);
    }

    private static class Coordinates {

        final double x;
        final double y;

        public Coordinates(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public List<Coordinates> nearCoordinates() {
            List<Coordinates> tiles = new ArrayList<>();
            tiles.add(new Coordinates(x - 0.5, y + 1));
            tiles.add(new Coordinates(x + 0.5, y + 1));
            tiles.add(new Coordinates(x - 1, y));
            tiles.add(new Coordinates(x + 1, y));
            tiles.add(new Coordinates(x - 0.5, y - 1));
            tiles.add(new Coordinates(x + 0.5, y - 1));
            return tiles;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coordinates that = (Coordinates) o;
            return Double.compare(that.x, x) == 0 &&
                    Double.compare(that.y, y) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}