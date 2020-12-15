package pl.devcezz.day15;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RambunctiousRecitationFirst {

    private static int ANSWER_INDEX = 2020;

    public static List<String> readData(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Not found file: " + path);
        }
    }

    public static void main(String[] args) {
        List<String> data = readData("data/day15/task.txt");

        List<List<Integer>> examples = data.stream()
                .map(row -> {
                    String[] split = row.split(",");
                    return Arrays.stream(split)
                            .map(Integer::parseInt)
                            .collect(Collectors.toList());
                }).collect(Collectors.toList());

        examples.forEach(RambunctiousRecitationFirst::play);

        examples.forEach(example -> System.out.println(example.get(ANSWER_INDEX - 1)));
    }

    private static void play(List<Integer> round) {
        for (int nextIndex = round.size(); nextIndex < ANSWER_INDEX; nextIndex++) {
            int lastIndex = nextIndex - 1;

            Optional<Integer> first = IntStream.range(0, lastIndex)
                    .filter(index -> round.get(index).equals(round.get(lastIndex)))
                    .boxed()
                    .max(Comparator.naturalOrder());

            if (first.isEmpty()) {
                round.add(0);
            } else {
                round.add(lastIndex - first.get());
            }
        }
    }
}
