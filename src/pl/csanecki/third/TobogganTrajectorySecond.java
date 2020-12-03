package pl.csanecki.third;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class TobogganTrajectorySecond {

    public static final char TREE = '#';

    public List<String> readData(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Not found file: " + path);
        }
    }

    public static void main(String[] args) {
        TobogganTrajectorySecond trajectory = new TobogganTrajectorySecond();
        List<String> map = trajectory.readData("data/third/task.txt");

        int count = 1;

        DataSecond data1 = new DataSecond(map, 1, 1);
        count *= data1.unavailableSpacesToLand();

        DataSecond data2 = new DataSecond(map, 3, 1);
        count *= data2.unavailableSpacesToLand();

        DataSecond data3 = new DataSecond(map, 5, 1);
        count *= data3.unavailableSpacesToLand();

        DataSecond data4 = new DataSecond(map, 7, 1);
        count *= data4.unavailableSpacesToLand();

        DataSecond data5 = new DataSecond(map, 1, 2);
        count *= data5.unavailableSpacesToLand();

        System.out.println(count);
    }
}

class DataSecond {

    public final List<String> map;
    public final int movementRight;
    public final int movementDown;

    public DataSecond(List<String> map, int movementRight, int movementDown) {
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