package pl.devcezz.day18;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class OperationOrderFirst {

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

class Calculator {

    private static final char PLUS = '+';
    private static final char MULTIPLY = '*';
    private static final char LEFT_PARENTHESES = '(';
    private static final char RIGHT_PARENTHESES = ')';

    public long calculateEquation(String equation) {
        long sum = 0;
        char[] equationChars = equation.toCharArray();

        StringBuilder number = new StringBuilder();
        char currentSign = PLUS;
        for (int i = 0; i < equationChars.length; i++) {
            switch (equationChars[i]) {
                case PLUS:
                    if (number.length() != 0) {
                        if (currentSign == PLUS) {
                            sum += Integer.parseInt(number.toString());
                        } else if (currentSign == MULTIPLY) {
                            sum *= Integer.parseInt(number.toString());
                        }

                        number = new StringBuilder();
                    }

                    currentSign = PLUS;
                    break;
                case MULTIPLY:
                    if (number.length() != 0) {
                        if (currentSign == PLUS) {
                            sum += Integer.parseInt(number.toString());
                        } else if (currentSign == MULTIPLY) {
                            sum *= Integer.parseInt(number.toString());
                        }

                        number = new StringBuilder();
                    }

                    currentSign = MULTIPLY;
                    break;
                case LEFT_PARENTHESES:
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
                    long secondNumber = calculateEquation(equation.substring(i + 1, rightParenthesesIndex));
                    number.append(secondNumber);
                    i = rightParenthesesIndex;
                    break;
                default:
                    number.append(equationChars[i]);
            }
        }

        if (number.length() != 0) {
            if (currentSign == PLUS) {
                sum += Integer.parseInt(number.toString());
            } else if (currentSign == MULTIPLY) {
                sum *= Integer.parseInt(number.toString());
            }
        }

        return sum;
    }
}