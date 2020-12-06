package pl.csanecki.sixth;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CustomCustomsFirst {

    public static List<String> readData(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Not found file: " + path);
        }
    }

    public static void main(String[] args) {
        List<String> data = readData("data/sixth/task.txt");

        Integer yesAnswers = Arrays.stream(
                String.join(":", data)
                .split("::")
        ).map(group -> Arrays.stream(group.split(":"))
                .flatMap(answers -> Arrays.stream(answers.split("")))
                .collect(Collectors.toSet())
                .size())
                .reduce(0, Integer::sum);

        System.out.println(yesAnswers);
    }
}
