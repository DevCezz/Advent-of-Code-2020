package pl.devcezz.day15;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RambunctiousRecitationSecond {

    private static final int ANSWER_INDEX = 30_000_000;

    public static String readData(String path) {
        try {
            return Files.readString(Path.of(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Not found file: " + path);
        }
    }

    public static void main(String[] args) {
        String data = readData("data/day15/task.txt");
        List<Long> inputNumbers = Arrays.stream(data.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        Map<Long, Integer> progression = IntStream.range(0, inputNumbers.size()).boxed()
                .collect(Collectors.toMap(inputNumbers::get, i -> i + 1));

        play(progression);

        progression.entrySet().stream()
                .filter(entry -> entry.getValue().equals(ANSWER_INDEX))
                .map(Map.Entry::getKey)
                .findFirst()
                .ifPresent(System.out::println);
    }

    private static void play(Map<Long, Integer> progression) {
        Long nextNumber = 0L;
        for (int nextIndex = progression.size() + 1; nextIndex <= ANSWER_INDEX; nextIndex++) {
            if (progression.containsKey(nextNumber)) {
                Integer currentIndex = progression.get(nextNumber);
                progression.put(nextNumber, nextIndex);
                nextNumber = (long) (nextIndex - currentIndex);
            } else {
                progression.put(nextNumber, nextIndex);
                nextNumber = 0L;
            }
        }
    }
}
