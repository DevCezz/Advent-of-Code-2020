package pl.csanecki.sixth;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class CustomCustomsSecond {

    public static List<String> readData(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Not found file: " + path);
        }
    }

    public static void main(String[] args) {
        List<String> data = readData("data/sixth/task.txt");

        Integer answer = Arrays.stream(splitToGroups(data))
                .map(CustomCustomsSecond::mapToYesAnswersIn)
                .reduce(0, Integer::sum);

        System.out.println(answer);
    }

    private static Integer mapToYesAnswersIn(String group) {
        return Arrays.stream(splitToPeopleAnswers(group))
                .map(CustomCustomsSecond::splitToQuestions)
                .reduce(CustomCustomsSecond::intersection)
                .map(HashSet::size)
                .orElse(0);
    }

    private static String[] splitToGroups(List<String> data) {
        return String.join(":", data).split("::");
    }

    private static String[] splitToPeopleAnswers(String answers) {
        return answers.split(":");
    }

    private static HashSet<String> splitToQuestions(String answers) {
        return new HashSet<>(Arrays.asList(answers.split("")));
    }

    public static HashSet<String> intersection(HashSet<String> setA, HashSet<String> setB) {
        return setA.stream()
                .distinct()
                .filter(setB::contains)
                .collect(Collectors.toCollection(HashSet::new));
    }
}
