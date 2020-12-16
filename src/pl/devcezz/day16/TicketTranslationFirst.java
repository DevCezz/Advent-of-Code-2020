package pl.devcezz.day16;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TicketTranslationFirst {

    public static List<String> readData(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Not found file: " + path);
        }
    }

    public static void main(String[] args) {
        Data data = new Data(readData("data/day16/task.txt"));
        List<Validator> validators = data.requirements.stream()
                .map(requirement -> {
                    String[] info = requirement.split(": ");
                    String[] ranges = info[1].split(" or ");
                    int a = Integer.parseInt(ranges[0].split("-")[0]);
                    int b = Integer.parseInt(ranges[0].split("-")[1]);
                    int c = Integer.parseInt(ranges[1].split("-")[0]);
                    int d = Integer.parseInt(ranges[1].split("-")[1]);
                    return new Validator(a, b, c, d);
                }).collect(Collectors.toList());

        Integer result = data.othersTicket.stream()
                .map(ticket -> {
                    String[] numbers = ticket.split(",");
                    return Arrays.stream(numbers)
                            .map(Integer::parseInt)
                            .collect(Collectors.toList());
                })
                .flatMap(List::stream)
                .filter(number -> validators.stream().noneMatch(validator -> validator.predicate.test(number)))
                .reduce(0, Integer::sum);

        System.out.println(result);
    }
}

class Validator {

    Predicate<Integer> predicate;

    public Validator(int a, int b, int c, int d) {
        this.predicate = x -> (x >= a && x <= b) || (x >= c && x <= d);
    }
}

class Data {

    List<String> requirements;
    List<String> myTicket;
    List<String> othersTicket;

    public Data(List<String> data) {
        List<Integer> splitters = splitters(data);

        requirements = data.subList(0, splitters.get(0));
        myTicket = data.subList(splitters.get(0) + 2, splitters.get(1));
        othersTicket = data.subList(splitters.get(1) + 2, data.size());
    }

    private List<Integer> splitters(List<String> data) {
        return IntStream.range(0, data.size())
                .boxed()
                .map(index -> {
                    if (data.get(index).equals("")) {
                        return index;
                    }

                    return -1;
                })
                .filter(index -> index != -1)
                .collect(Collectors.toList());
    }
}