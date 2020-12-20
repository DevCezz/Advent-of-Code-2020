package pl.devcezz.day19;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MonsterMessagesFirst {

    private static Map<Integer, Rule> RULES = new HashMap<>();

    public static List<String> readData(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Not found file: " + path);
        }
    }

    public static void main(String[] args) {
        List<String> data = readData("data/day19/task.txt");

        int splitterIndex = IntStream.range(0, data.size()).boxed()
                .filter(index -> data.get(index).isEmpty())
                .findFirst()
                .orElse(-1);

        List<String> rulesRaw = data.subList(0, splitterIndex);
        List<String> inputRaw = data.subList(splitterIndex + 1, data.size());

        RULES = rulesRaw.stream()
                .map(rule -> {
                    String[] numberToRule = rule.split(": ");
                    int number = Integer.parseInt(numberToRule[0]);
                    String[] split = numberToRule[1].split(" \\| ");

                    if (split.length == 2) {
                        List<Integer> left = Arrays.stream(split[0].split(" "))
                                .map(Integer::parseInt)
                                .collect(Collectors.toList());
                        List<Integer> right = Arrays.stream(split[1].split(" "))
                                .map(Integer::parseInt)
                                .collect(Collectors.toList());

                        return new Rule(number, left, right, null);
                    } else if (split[0].contains("\"")) {
                        return new Rule(number, null, null, split[0].replace("\"", ""));
                    } else {
                        List<Integer> left = Arrays.stream(split[0].split(" "))
                                .map(Integer::parseInt)
                                .collect(Collectors.toList());

                        return new Rule(number, left, null, null);
                    }
                })
                .collect(Collectors.toMap(r -> r.number, r -> r));

        List<String> answer = calculate(RULES.get(0))
                .stream()
                .map(StringBuilder::toString)
                .collect(Collectors.toList());

        long count = inputRaw.stream()
                .filter(answer::contains)
                .count();
        System.out.println(count);
    }

    private static List<StringBuilder> calculate(Rule rule) {
        List<StringBuilder> temp = new ArrayList<>();
        if (rule.isEnding()) {
            temp.add(new StringBuilder(rule.ending));
            return temp;
        } else {
            if (!rule.hasAlternative()) {
                List<StringBuilder> first = calculate(RULES.get(rule.left.get(0)));
                for (int i = 1; i < rule.left.size(); i++) {
                    Rule currentRule = RULES.get(rule.left.get(i));
                    List<StringBuilder> second = calculate(currentRule);

                    List<StringBuilder> result = new ArrayList<>();
                    for (int j = 0; j < first.size(); j++) {
                        for (int k = 0; k < second.size(); k++) {
                            result.add(new StringBuilder(first.get(j).toString() + second.get(k).toString()));
                        }
                    }

                    first = result;
                }

                return first;
            } else {
                List<StringBuilder> firstLeft = calculate(RULES.get(rule.left.get(0)));
                for (int i = 1; i < rule.left.size(); i++) {
                    Rule currentRule = RULES.get(rule.left.get(i));
                    List<StringBuilder> second = calculate(currentRule);

                    List<StringBuilder> result = new ArrayList<>();
                    for (int j = 0; j < firstLeft.size(); j++) {
                        for (int k = 0; k < second.size(); k++) {
                            result.add(new StringBuilder(firstLeft.get(j).toString() + second.get(k).toString()));
                        }
                    }

                    firstLeft = result;
                }

                List<StringBuilder> firstRight = calculate(RULES.get(rule.right.get(0)));
                for (int i = 1; i < rule.right.size(); i++) {
                    Rule currentRule = RULES.get(rule.right.get(i));
                    List<StringBuilder> second = calculate(currentRule);

                    List<StringBuilder> result = new ArrayList<>();
                    for (int j = 0; j < firstRight.size(); j++) {
                        for (int k = 0; k < second.size(); k++) {
                            result.add(new StringBuilder(firstRight.get(j).toString() + second.get(k).toString()));
                        }
                    }

                    firstRight = result;
                }

                firstLeft.addAll(firstRight);

                return firstLeft;
            }
        }
    }
}

class Rule {

    int number;
    List<Integer> left;
    List<Integer> right;
    String ending;

    public Rule(int number, List<Integer> left, List<Integer> right, String ending) {
        this.number = number;
        this.left = left;
        this.right = right;
        this.ending = ending;
    }

    public boolean isEnding() {
        return ending != null;
    }

    public boolean hasAlternative() {
        return right != null;
    }
}