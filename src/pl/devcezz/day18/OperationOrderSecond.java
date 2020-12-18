package pl.devcezz.day18;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class OperationOrderSecond {

    public static List<String> readData(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Not found file: " + path);
        }
    }

    public static void main(String[] args) {
        List<Long> data = readData("data/day18/task.txt").stream()
                .map(line -> line.replace(" ", ""))
                .map(line -> new CalculatorV2().calculateEquation(line))
                .collect(Collectors.toList());

        Long sum = data.stream().reduce(0L, Long::sum);
        System.out.println(sum);
    }
}

class CalculatorV2 {

    private static final char PLUS = '+';
    private static final char MULTIPLY = '*';
    private static final char LEFT_PARENTHESES = '(';
    private static final char RIGHT_PARENTHESES = ')';

    public long calculateEquation(String equation) {
        long sum = 0;

        char[] equationChars = equation.toCharArray();
        if (equation.contains(Character.toString(LEFT_PARENTHESES))) {
            for (int i = 0; i < equationChars.length; i++) {
                if (equationChars[i] == LEFT_PARENTHESES) {
                    int pairs = 1;
                    int rightParenthesesIndex;
                    for (rightParenthesesIndex = i + 1; rightParenthesesIndex < equationChars.length; rightParenthesesIndex++) {
                        if (equationChars[rightParenthesesIndex] == LEFT_PARENTHESES) {
                            pairs++;
                        } else if (equationChars[rightParenthesesIndex] == RIGHT_PARENTHESES) {
                            pairs--;
                        }

                        if (pairs == 0) {
                            break;
                        }
                    }
                    String subEquation = equation.substring(i, rightParenthesesIndex + 1);
                    long result = calculateEquation(equation.substring(i + 1, rightParenthesesIndex));
                    equation = equation.replace(subEquation, Long.toString(result));
                    i = 0;
                    equationChars = equation.toCharArray();
                }
            }
        }

        List<Long> numbers = Arrays.stream(equation.split("[+*]"))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        List<String> signs = getSigns(equation);

        List<Long> toMultiply = new ArrayList<>();
        long tempSum = numbers.get(0);
        for (int i = 1; i < numbers.size(); i++) {
            if (signs.get(i - 1).equals(Character.toString(PLUS))) {
                tempSum += numbers.get(i);
            } else {
                toMultiply.add(tempSum);
                tempSum = numbers.get(i);
            }
        }
        toMultiply.add(tempSum);

        return toMultiply.stream()
                .reduce(1L, (a, b) -> a * b);
    }

    private List<String> getSigns(String equation) {
        List<String> signs = new ArrayList<>();
        Pattern signsPattern = Pattern.compile("[\\+\\*]");
        Matcher matcher = signsPattern.matcher(equation);
        while (matcher.find()) {
            signs.add(matcher.group());
        }
        return signs;
    }
}