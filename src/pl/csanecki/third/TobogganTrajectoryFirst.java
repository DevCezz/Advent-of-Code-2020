package pl.csanecki.third;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class TobogganTrajectoryFirst {

    public static final char TREE = '#';

    public List<String> readData(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Not found file: " + path);
        }
    }

    public static void main(String[] args) {
        TobogganTrajectoryFirst trajectory = new TobogganTrajectoryFirst();
        List<String> map = trajectory.readData("data/third/task.txt");

        DataSecond data = new DataSecond(map, 3, 1);
        int count = data.unavailableSpacesToLand();

        System.out.println(count);
    }
}

class DataFirst {

    public final List<String> map;
    public final int movementRight;
    public final int movementDown;

    public DataFirst(List<String> map, int movementRight, int movementDown) {
        this.map = map;
        this.movementRight = movementRight;
        this.movementDown = movementDown;
    }

    public int unavailableSpacesToLand() {
        int trees = 0;

        int mapHeight = map.size();

        int currentY = movementDown;
        int currentX = movementRight;
        while (currentY < mapHeight) {
            String mapLine = map.get(currentY);

            StringBuilder mapLineBuilder = new StringBuilder(mapLine);
            while (currentX >= mapLineBuilder.length()) {
                mapLineBuilder.append(mapLine);
            }

            mapLine = mapLineBuilder.toString();

            if (mapLine.charAt(currentX) == TobogganTrajectoryFirst.TREE) {
                trees++;
            }

            currentY += movementDown;
            currentX += movementRight;
        }

        return trees;
    }
}