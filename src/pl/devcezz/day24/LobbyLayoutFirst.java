package pl.devcezz.day24;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LobbyLayoutFirst {

    private static final Map<String, Boolean> TILES = new HashMap<>();

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
            String key = String.format("x=%s,y=%s", x, y);
            if (TILES.containsKey(key)) {
                boolean isBlack = TILES.get(key);
                if (isBlack) {
                    TILES.put(key, false);
                } else {
                    TILES.put(key, true);
                }
            } else {
                TILES.put(key, true);
            }
        }

        long result = TILES.values().stream()
                .filter(isBlack -> isBlack)
                .count();

        System.out.println(result);
    }
}