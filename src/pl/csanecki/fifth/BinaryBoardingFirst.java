package pl.csanecki.fifth;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class BinaryBoardingFirst {

    public static List<String> readData(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Not found file: " + path);
        }
    }

    public static void main(String[] args) {
        SeatCounter seatCounter = new SeatCounter();
        Optional<Integer> max = readData("data/fifth/task.txt")
                .stream()
                .map(line -> new Instruction(line, 7))
                .map(seatCounter::countSeatId)
                .max(Integer::compareTo);

        max.ifPresent(System.out::println);
    }
}