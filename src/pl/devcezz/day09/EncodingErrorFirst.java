package pl.devcezz.day09;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class EncodingErrorFirst {

    private static final int PREAMBLE = 25;

    public static List<String> readData(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Not found file: " + path);
        }
    }

    public static void main(String[] args) {
        List<Long> numbers = readData("data/day09/task.txt").stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());

        int preamble = PREAMBLE;

        long answer = 0;
        for (int i = preamble; i < numbers.size(); i++) {
            Long numberToCheck = numbers.get(i);

            if (cannotBeGetFromNumbers(numberToCheck, numbers.subList(preamble - PREAMBLE, preamble))) {
                answer = numberToCheck;
                break;
            }

            preamble++;
        }

        System.out.println(answer);
    }

    private static boolean cannotBeGetFromNumbers(Long numberToCheck, List<Long> subList) {
        for (int i = 0; i < subList.size(); i++) {
            for (int j = i + 1; j < subList.size(); j++) {
                if (subList.get(i) + subList.get(j) == numberToCheck) {
                    return false;
                }
            }
        }

        return true;
    }
}