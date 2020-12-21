package pl.devcezz.day20;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class JurassicJigsawFirst {

    public static List<String> readData(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Not found file: " + path);
        }
    }

    public static void main(String[] args) {
        List<Tile> tiles = matchPiecesOfMap("data/day20/task.txt");

        Long result = tiles.stream()
                .filter(tile -> tile.neighbours.size() == 2)
                .map(tile -> (long) tile.id)
                .reduce(1L, (a, b) -> a * b);

        System.out.println(result);
    }

    public static List<Tile> matchPiecesOfMap(String path) {
        List<String> rawData = readData(path);
        List<List<String>> data = prepareData(rawData);

        List<Tile> tiles = data.stream()
                .map(input -> {
                    String[][] pieceOfMap = new String[input.get(1).length()][input.get(1).length()];
                    Pattern pattern = Pattern.compile("\\d+");
                    Matcher matcher = pattern.matcher(input.get(0));
                    matcher.find();
                    int id = Integer.parseInt(matcher.group());

                    for (int i = 1; i < input.size(); i++) {
                        String[] split = input.get(i).split("");
                        for (int j = 0; j < split.length; j++) {
                            pieceOfMap[i - 1][j] = split[j];
                        }
                    }

                    return new Tile(id, pieceOfMap);
                })
                .collect(Collectors.toList());

        tiles.get(0).murBeton = true;

        boolean done = false;
        while (!done) {
            findMatch(tiles);
            match(tiles);
            done = tiles.stream()
                .allMatch(tile -> tile.murBeton);
        }
        matchAll(tiles);
        return tiles;
    }

    private static void matchAll(List<Tile> tiles) {
        for (Tile one : tiles) {
            for (Tile two : tiles) {
                if (two == one) {
                    continue;
                }

                int side = one.pieceOfMap.length;

                boolean matchesRight = true;
                for (int j = 0; j < side; j++) {
                    if (!one.pieceOfMap[j][side - 1].equals(two.pieceOfMap[j][0])) {
                        matchesRight = false;
                        break;
                    }
                }
                if (matchesRight) {
                    one.neighbours.put(Neighbour.RIGHT, two.id);
                    two.neighbours.put(Neighbour.LEFT, one.id);
                    continue;
                }

                boolean matchesDown = true;
                for (int j = 0; j < side; j++) {
                    if (!one.pieceOfMap[side - 1][j].equals(two.pieceOfMap[0][j])) {
                        matchesDown = false;
                        break;
                    }
                }
                if (matchesDown) {
                    one.neighbours.put(Neighbour.DOWN, two.id);
                    two.neighbours.put(Neighbour.UP, one.id);
                    continue;
                }

                boolean matchesLeft = true;
                for (int j = 0; j < side; j++) {
                    if (!one.pieceOfMap[j][0].equals(two.pieceOfMap[j][side - 1])) {
                        matchesLeft = false;
                        break;
                    }
                }
                if (matchesLeft) {
                    one.neighbours.put(Neighbour.LEFT, two.id);
                    two.neighbours.put(Neighbour.RIGHT, one.id);
                    continue;
                }

                boolean matchesUp = true;
                for (int j = 0; j < side; j++) {
                    if (!one.pieceOfMap[0][j].equals(two.pieceOfMap[side - 1][j])) {
                        matchesUp = false;
                        break;
                    }
                }
                if (matchesUp) {
                    one.neighbours.put(Neighbour.UP, two.id);
                    two.neighbours.put(Neighbour.DOWN, one.id);
                }
            }
        }
    }

    private static void findMatch(List<Tile> tiles) {
        List<Tile> tilesMurBeton = tiles.stream()
                .filter(tile -> tile.murBeton)
                .collect(Collectors.toList());
        List<Tile> tilesToMatch = tiles.stream()
                .filter(tile -> !tile.murBeton)
                .collect(Collectors.toList());

        for (Tile tileMurBeton : tilesMurBeton) {
            TO_MATCH: for (Tile tileToMatch : tilesToMatch) {
                if (tileToMatch.murBeton) {
                    continue;
                }

                ROTATE: for (int i = 0; i < 8; i++) {
                    int side = tileMurBeton.pieceOfMap.length;

                    boolean matchesRight = true;
                    for (int j = 0; j < side; j++) {
                        if (!tileMurBeton.pieceOfMap[j][side - 1].equals(tileToMatch.pieceOfMap[j][0])) {
                            matchesRight = false;
                            break;
                        }
                    }
                    if (matchesRight) {
                        tileMurBeton.neighbours.put(Neighbour.RIGHT, tileToMatch.id);
                        tileToMatch.neighbours.put(Neighbour.LEFT, tileMurBeton.id);
                        tileToMatch.murBeton = true;
                        break TO_MATCH;
                    }

                    boolean matchesDown = true;
                    for (int j = 0; j < side; j++) {
                        if (!tileMurBeton.pieceOfMap[side - 1][j].equals(tileToMatch.pieceOfMap[0][j])) {
                            matchesDown = false;
                            break;
                        }
                    }
                    if (matchesDown) {
                        tileMurBeton.neighbours.put(Neighbour.DOWN, tileToMatch.id);
                        tileToMatch.neighbours.put(Neighbour.UP, tileMurBeton.id);
                        tileToMatch.murBeton = true;
                        break TO_MATCH;
                    }

                    boolean matchesLeft = true;
                    for (int j = 0; j < side; j++) {
                        if (!tileMurBeton.pieceOfMap[j][0].equals(tileToMatch.pieceOfMap[j][side - 1])) {
                            matchesLeft = false;
                            break;
                        }
                    }
                    if (matchesLeft) {
                        tileMurBeton.neighbours.put(Neighbour.LEFT, tileToMatch.id);
                        tileToMatch.neighbours.put(Neighbour.RIGHT, tileMurBeton.id);
                        tileToMatch.murBeton = true;
                        break ROTATE;
                    }

                    boolean matchesUp = true;
                    for (int j = 0; j < side; j++) {
                        if (!tileMurBeton.pieceOfMap[0][j].equals(tileToMatch.pieceOfMap[side - 1][j])) {
                            matchesUp = false;
                            break;
                        }
                    }
                    if (matchesUp) {
                        tileMurBeton.neighbours.put(Neighbour.UP, tileToMatch.id);
                        tileToMatch.neighbours.put(Neighbour.DOWN, tileMurBeton.id);
                        tileToMatch.murBeton = true;
                        break ROTATE;
                    }

                    if (i == 4) {
                        tileToMatch.flip();
                    }
                    tileToMatch.rotate();
                }
            }
        }
    }
    
    private static void match(List<Tile> tiles) {
        List<Tile> tilesMurBeton = tiles.stream()
                .filter(tile -> tile.murBeton)
                .collect(Collectors.toList());

        for (Tile tileMurBeton : tilesMurBeton) {
            for (Neighbour neighbour : tileMurBeton.neighbours.keySet()) {
                Integer id = tileMurBeton.neighbours.get(neighbour);
                Optional<Tile> first = tiles.stream()
                        .filter(tile -> tile.id == id)
                        .findFirst();

                first.ifPresent(tile -> tile.neighbours.put(Neighbour.opposite(neighbour), tileMurBeton.id));
            }
        }
    }

    private static List<List<String>> prepareData(List<String> rawData) {
        List<List<String>> data = new ArrayList<>();
        List<String> row = new ArrayList<>();
        for (int i = 0; i < rawData.size(); i++) {
            if (rawData.get(i).isEmpty()) {
                data.add(row);
                row = new ArrayList<>();
            } else {
                row.add(rawData.get(i));
            }
        }
        if (!row.isEmpty()) {
            data.add(row);
        }

        return data;
    }
}

class Tile {

    int id;
    String[][] pieceOfMap;
    Map<Neighbour, Integer> neighbours = new HashMap<>();
    boolean murBeton = false;

    public Tile(int id, String[][] pieceOfMap) {
        this.id = id;
        this.pieceOfMap = pieceOfMap;
    }

    public void rotate() {
        String[][] rotated = new String[pieceOfMap.length][pieceOfMap[0].length];
        for (int i = 0; i < pieceOfMap.length; i++) {
            for (int j = 0; j < pieceOfMap[0].length; j++) {
                rotated[j][pieceOfMap[0].length - 1 - i] = pieceOfMap[i][j];
            }
        }
        pieceOfMap = rotated;
    }

    public void flip() {
        String[][] flipped = new String[pieceOfMap.length][pieceOfMap[0].length];
        for (int i = 0; i < pieceOfMap[0].length; i++) {
            for (int j = 0; j < pieceOfMap[0].length; j++) {
                flipped[i][pieceOfMap[0].length - 1 - j] = pieceOfMap[i][j];
            }
        }
        pieceOfMap = flipped;
    }

    public boolean hasRight() {
        return neighbours.get(Neighbour.RIGHT) != null;
    }

    public boolean hasDown() {
        return neighbours.get(Neighbour.DOWN) != null;
    }

    public void print() {
        System.out.println("Tile: " + id);
        for (int i = 0; i < pieceOfMap.length; i++) {
            for (int j = 0; j < pieceOfMap[i].length; j++) {
                System.out.print(pieceOfMap[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }
}

enum Neighbour {
    LEFT, RIGHT, UP, DOWN;

    public static Neighbour opposite(Neighbour main) {
        switch (main) {
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
        }

        return null;
    }
}