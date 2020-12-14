package pl.csanecki.thirteen;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ShuttleSearchFirst {

    public static List<String> readData(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Not found file: " + path);
        }
    }

    public static void main(String[] args) {
        List<String> data = readData("data/thirteen/task.txt");

        int arrivalTime = Integer.parseInt(data.get(0));
        List<Integer> busses = Arrays.stream(data.get(1).split(","))
                .filter(bus -> !bus.equals("x"))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        List<Integer> timeToWait = busses.stream()
                .map(bus -> (-1 * arrivalTime) % bus + bus)
                .collect(Collectors.toList());

        int min = timeToWait.stream().min(Integer::compareTo).orElse(-1);
        int index = timeToWait.indexOf(min);

        System.out.println(min * busses.get(index));
    }
}
