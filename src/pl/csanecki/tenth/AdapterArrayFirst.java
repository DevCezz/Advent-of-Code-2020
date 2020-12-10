package pl.csanecki.tenth;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdapterArrayFirst {

    public static List<String> readData(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Not found file: " + path);
        }
    }

    public static void main(String[] args) {
        List<Integer> numbers = new ArrayList<>();
        numbers.add(0);

        numbers.addAll(readData("data/tenth/task.txt").stream()
                .map(Integer::parseInt)
                .sorted()
                .collect(Collectors.toList()));

        numbers.add(numbers.get(numbers.size() - 1) + 3);

        int one = 0;
        int two = 0;
        int three = 0;

        for (int i = 0; i < numbers.size() - 1; i++) {
            Integer first = numbers.get(i);
            Integer second = numbers.get(i + 1);

            int difference = second - first;

            switch (difference) {
                case 1:
                    one++;
                    break;
                case 2:
                    two++;
                    break;
                case 3:
                    three++;
                    break;
            }
        }

        System.out.println(one + " " + two + " " + three);
        System.out.println(one * three);
    }
}