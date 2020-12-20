package pl.devcezz.day19;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MonsterMessagesSecond {

    private static Map<Integer, Rule> RULES = new HashMap<>();

    public static List<String> readData(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Not found file: " + path);
        }
    }

    public static void main(String[] args) {
        List<String> data = readData("data/day19/task1.txt");

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
                .filter(input -> {
                    for (String a : answer) {
                        Pattern pattern = Pattern.compile(a);
                        Matcher matcher = pattern.matcher(input);
                        if (matcher.matches()) {
                            return true;
                        }
                    }

                    return false;
                })
                .count();
        System.out.println(count);
    }

    private static List<StringBuilder> calculate(Rule rule) {
        if (rule.number == 8 || rule.number == 11) {
            System.out.println();
        }

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

                    StringBuilder builder = new StringBuilder();
                    builder.append("(");
                    for (int j = 0; j < first.size(); j++) {
                        for (int k = 0; k < second.size(); k++) {
                            builder.append(first.get(j).toString()).append(second.get(k).toString());
                            if (k < second.size() - 1) {
                                builder.append("|");
                            }
                        }
                        if (j < first.size() - 1) {
                            builder.append("|");
                        }
                    }

                    builder.append(")");
                    List<StringBuilder> builders = Collections.singletonList(builder);
                    first = new ArrayList<>(builders);
                }

                return first;
            } else {
                List<StringBuilder> firstLeft = calculate(RULES.get(rule.left.get(0)));
                for (int i = 1; i < rule.left.size(); i++) {
                    Rule currentRule = RULES.get(rule.left.get(i));
                    List<StringBuilder> second = calculate(currentRule);

                    StringBuilder builder = new StringBuilder();
                    builder.append("(");
                    for (int j = 0; j < firstLeft.size(); j++) {
                        for (int k = 0; k < second.size(); k++) {
                            builder.append(firstLeft.get(j).toString()).append(second.get(k).toString());
                            if (k < second.size() - 1) {
                                builder.append("|");
                            }
                        }
                        if (j < firstLeft.size() - 1) {
                            builder.append("|");
                        }
                    }

                    builder.append(")");
                    List<StringBuilder> builders = Collections.singletonList(builder);
                    firstLeft = new ArrayList<>(builders);
                }

                List<StringBuilder> firstRight = calculate(RULES.get(rule.right.get(0)));
                for (int i = 1; i < rule.right.size(); i++) {
                    Rule currentRule = RULES.get(rule.right.get(i));
                    if (rule.number != currentRule.number) {
                        List<StringBuilder> second = calculate(currentRule);

                        StringBuilder builder = new StringBuilder();
                        builder.append("(");
                        for (int j = 0; j < firstRight.size(); j++) {
                            for (int k = 0; k < second.size(); k++) {
                                builder.append(firstRight.get(j).toString()).append(second.get(k).toString());
                                if (k < second.size() - 1) {
                                    builder.append("|");
                                }
                            }
                            if (j < firstRight.size() - 1) {
                                builder.append("|");
                            }
                        }
                        builder.append(")");
                        List<StringBuilder> builders = Collections.singletonList(builder);
                        firstRight = new ArrayList<>(builders);
                    } else {
                        StringBuilder builder = new StringBuilder();
                        builder.append("(");
                        for (int j = 0; j < firstRight.size(); j++) {
                            for (int k = 0; k < firstRight.size(); k++) {
                                builder.append(firstRight.get(j).toString()).append("[").append(firstRight.get(k).toString()).append("]+");
                                if (k < firstRight.size() - 1) {
                                    builder.append("|");
                                }
                            }
                            if (j < firstRight.size() - 1) {
                                builder.append("|");
                            }
                        }
                        builder.append(")");
                        List<StringBuilder> builders = Collections.singletonList(builder);
                        firstRight = new ArrayList<>(builders);
                    }
                }

                firstLeft.addAll(firstRight);

                return firstLeft;
            }
        }
    }
}

