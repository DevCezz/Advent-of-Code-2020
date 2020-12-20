package pl.devcezz.day19;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MonsterMessagesFirst {

    public static List<String> readData(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Not found file: " + path);
        }
    }

    public static void main(String[] args) {
        List<String> data = readData("data/day19/task.txt");

        List<String> input = data.stream()
                .filter(line -> line.matches("[ab]+"))
                .collect(Collectors.toList());

        Map<Integer, String> rules = data.stream()
                .filter(line -> line.contains(": "))
                .map(line -> line.split(": "))
                .collect(Collectors.toMap(i -> Integer.parseInt(i[0]), i -> i[1].replace("\"", "")));

        String regex = regex(rules, 0);

        long count = input.stream()
                .filter(line -> line.matches(regex))
                .count();
        System.out.println(count);
    }

    public static String regex(Map<Integer, String> rules, int index) {
        String rule = rules.get(index);
        while (rule.matches(".*\\d+.*")) {
            StringBuilder builder = new StringBuilder();
            String[] data = rule.split(" ");
            for (String input : data) {
                if (input.matches("\\d+")) {
                    String newRule = rules.get(Integer.parseInt(input));
                    if (newRule.matches("[ab]")) {
                        builder.append(newRule);
                    } else {
                        builder.append("( ").append(newRule).append(" )");
                    }
                } else {
                    builder.append(input);
                }
            }
            rule = builder.toString();
        }

        return String.format("^%s$", rule);
    }
}