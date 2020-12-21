package pl.devcezz.day20;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class JurassicJigsawSecond {

//    private static final String[][] MONSTER = new String[][] {
//            { " ", " ", "#", "#" },
//            { " ", " ", " ", "#" },
//            { "#", "#", "#", " " }
//    };

    private static final String[][] MONSTER = new String[][] {
            { " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", "#", " " },
            { "#", " ", " ", " ", " ", "#", "#", " ", " ", " ", " ", "#", "#", " ", " ", " ", " ", "#", "#", "#" },
            { " ", "#", " ", " ", "#", " ", " ", "#", " ", " ", "#", " ", " ", "#", " ", " ", "#", " ", " ", " " }
    };

    public static List<String> readData(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Not found file: " + path);
        }
    }

    public static void main(String[] args) {
        List<Tile> tiles = JurassicJigsawFirst.matchPiecesOfMap("data/day20/task.txt");

        String[][] map = makeMap(tiles);

        boolean properPosition = false;

        for (int i = 0; i < 8; i++) {
            System.out.println(i);
            print(map);

            for (int topLeftMonsterY = 0; topLeftMonsterY < map.length - MONSTER.length + 1; topLeftMonsterY++) {
                for (int topLeftMonsterX = 0; topLeftMonsterX < map[0].length - MONSTER[0].length + 1; topLeftMonsterX++) {
                    boolean match = true;
                    for (int y = 0; y < MONSTER.length; y++) {
                        for (int x = 0; x < MONSTER[0].length; x++) {
                            if (MONSTER[y][x].equals("#") && !MONSTER[y][x].equals(map[topLeftMonsterY + y][topLeftMonsterX + x])) {
                                match = false;
                                break;
                            }
                        }
                    }

                    if (match) {
                        properPosition = true;
                        for (int y = 0; y < MONSTER.length; y++) {
                            for (int x = 0; x < MONSTER[y].length; x++) {
                                if (MONSTER[y][x].equals("#")) {
                                    map[topLeftMonsterY + y][topLeftMonsterX + x] = "O";
                                }
                            }
                        }
                    }
                }
            }

            if (properPosition) {
                print(map);
                break;
            }

            if (i == 3) {
                map = flip(map);
            } else {
                map = rotate(map);
            }
        }

        System.out.println(count(map));
    }

    private static String[][] rotate(String[][] map) {
        String[][] rotated = new String[map.length][map[0].length];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                rotated[j][map[0].length - 1 - i] = map[i][j];
            }
        }
        return rotated;
    }

    private static String[][] flip(String[][] map) {
        String[][] flipped = new String[map.length][map[0].length];
        for (int i = 0; i < map[0].length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                flipped[i][map[0].length - 1 - j] = map[i][j];
            }
        }
        return flipped;
    }

    private static String[][] makeMap(List<Tile> tiles) {
        Tile first = tiles.stream()
                .filter(tile -> tile.neighbours.size() == 2 && tile.neighbours.get(Neighbour.DOWN) != null && tile.neighbours.get(Neighbour.RIGHT) != null)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

        int sqrt = (int) Math.sqrt(tiles.size());
        int side = tiles.get(0).pieceOfMap[0].length - 2;
        String[][] map = new String[sqrt * side][sqrt * side];
        int x = 0;
        int y = 0;

        while (first != null) {
            Tile right = first;
            while (right != null) {
                for (int i = 0; i < side; i++) {
                    for (int j = 0; j < side; j++) {
                        map[x + i][y + j] = right.pieceOfMap[i + 1][j + 1];
                    }
                }
                y += side;

                Integer rightId = right.neighbours.get(Neighbour.RIGHT);
                if (rightId == null) {
                    break;
                }
                right = tiles.stream()
                        .filter(tile -> tile.id == rightId)
                        .findFirst()
                        .orElse(null);
            }

            x += side;
            y = 0;
            Integer downId = first.neighbours.get(Neighbour.DOWN);
            if (downId == null) {
                break;
            }
            first = tiles.stream()
                    .filter(tile -> tile.id == downId)
                    .findFirst()
                    .orElse(null);
        }
        return map;
    }

    public static void print(String[][] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                System.out.print(map[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    public static long count(String[][] map) {
        long count = 0;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j].equals("#")) {
                    count++;
                }
            }
        }
        return count;
    }
}