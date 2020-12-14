package pl.devcezz.day10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdapterArraySecond {

    public static Map<Integer, Integer> foundations = Map.of(
            2, 1,
            3, 2,
            4, 4,
            5, 7
    );

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
        numbers.addAll(readData("data/day10/task.txt").stream()
                .map(Integer::parseInt)
                .sorted()
                .collect(Collectors.toList()));
        numbers.add(numbers.get(numbers.size() - 1) + 3);

        numbers.forEach(System.out::println);

        Map<Integer, Integer> map = new HashMap<>();
        int counter = 1;
        for (int i = 0; i < numbers.size() - 1; i++) {
            int first = numbers.get(i);
            int second = numbers.get(i + 1);

            if (first + 1 == second) {
                counter++;
            } else {
                if (counter > 1) {
                    Integer integer = map.get(counter);
                    if (integer == null) {
                        integer = 1;
                    } else {
                        integer++;
                    }

                    map.put(counter, integer);
                }

                counter = 1;
            }
        }

        System.out.println(map);
        System.out.println(foundations);

        long answer = 1;
        for (Integer key : map.keySet()) {
            Integer foundation = foundations.get(key);
            Integer count = map.get(key);
            answer *= Math.pow(foundation, count);
        }

        System.out.println(answer);
    }
}