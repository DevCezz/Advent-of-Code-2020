package pl.csanecki.ninth;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EncodingErrorSecond {

    private static final int PREAMBLE = 25;

    public static List<String> readData(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Not found file: " + path);
        }
    }

    public static void main(String[] args) {
        List<Long> numbers = readData("data/ninth/task.txt").stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());


        Answer answer = getAnswer(numbers);

        List<Long> used = null;
        INNER: for (int i = 0; i < answer.index; i++) {
            List<Long> summarize = new ArrayList<>();
            summarize.add(numbers.get(i));

            for (int j = i + 1; j < answer.index; j++) {
                summarize.add(numbers.get(j));

                Long sum = summarize.stream()
                        .reduce(Long::sum)
                        .orElse(Long.parseLong("0"));
                if (sum == answer.number) {
                    used = summarize;
                    break INNER;
                } else if (sum > answer.number){
                    continue INNER;
                }
            }
        }

        List<Long> sorted = used.stream().sorted().collect(Collectors.toList());

        System.out.println(sorted.get(0) + sorted.get(sorted.size() - 1));
    }

    private static Answer getAnswer(List<Long> numbers) {
        int preamble = PREAMBLE;

        for (int i = preamble; i < numbers.size(); i++) {
            Long numberToCheck = numbers.get(i);

            if (cannotBeGetFromNumbers(numberToCheck, numbers.subList(preamble - PREAMBLE, preamble))) {
                return new Answer(i, numberToCheck);
            }

            preamble++;
        }

        return null;
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

class Answer {

    int index;
    long number;

    public Answer(int index, long number) {
        this.index = index;
        this.number = number;
    }
}