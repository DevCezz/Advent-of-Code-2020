package pl.devcezz.day01;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class ElvesExpenseReportFirst {

    public List<String> readData(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Not found file: " + path);
        }
    }

    public static void main(String[] args) {
        ElvesExpenseReportFirst report = new ElvesExpenseReportFirst();
        List<String> data = report.readData("data/day01/task.txt");
        int multiplication = report.findMultiplication(data);

        System.out.println(multiplication);
    }

    public int findMultiplication(List<String> data) {
        List<Integer> collect1 = data.stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        List<Integer> collect2 = data.stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        int finalInt = -1;

        INNER: for (int number1 : collect1) {
            for (int number2 : collect2) {
                if (number1 == number2) {
                    continue;
                }

                if (number1 + number2 == 2020) {
                    System.out.println(number1 + " " + number2);
                    finalInt = number1 * number2;
                    break INNER;
                }
            }
        }

        return finalInt;
    }
}
