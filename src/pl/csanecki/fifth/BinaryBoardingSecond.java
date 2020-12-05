package pl.csanecki.fifth;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BinaryBoardingSecond {

    public static List<String> readData(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Not found file: " + path);
        }
    }

    public static void main(String[] args) {
        SeatCounter seatCounter = new SeatCounter();
        List<Integer> sorted = readData("data/fifth/task.txt")
                .stream()
                .map(line -> new Instruction(line, 7))
                .map(seatCounter::countSeatId)
                .sorted()
                .collect(Collectors.toList());

        Integer minSeatId = sorted.get(0);
        Integer maxSeatId = sorted.get(sorted.size() - 1);

        OptionalInt emptySeatId = IntStream.range(minSeatId, maxSeatId)
                .filter(seatId -> !sorted.contains(seatId))
                .findFirst();

        emptySeatId.ifPresent(System.out::println);
    }
}
