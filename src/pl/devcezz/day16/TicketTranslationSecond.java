package pl.devcezz.day16;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TicketTranslationSecond {

    public static List<String> readData(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Not found file: " + path);
        }
    }

    public static void main(String[] args) {
        Data data = new Data(readData("data/day16/task.txt"));
        List<ValidatorV2> validators = IntStream.range(0, data.requirements.size())
                .boxed()
                .map(index -> {
                    String[] info = data.requirements.get(index).split(": ");
                    String[] ranges = info[1].split(" or ");
                    int a = Integer.parseInt(ranges[0].split("-")[0]);
                    int b = Integer.parseInt(ranges[0].split("-")[1]);
                    int c = Integer.parseInt(ranges[1].split("-")[0]);
                    int d = Integer.parseInt(ranges[1].split("-")[1]);
                    return new ValidatorV2(info[0], a, b, c, d);
                }).collect(Collectors.toList());

        List<String> validTickets = getValidTickets(data, validators);

        Map<Integer, List<Integer>> columnToValues = IntStream.range(0, validators.size())
                .boxed()
                .collect(Collectors.toMap(i -> i, i -> new ArrayList<>()));

        validTickets.forEach(
                ticket -> {
                    List<Integer> numbers = Arrays.stream(ticket.split(","))
                            .map(Integer::parseInt)
                            .collect(Collectors.toList());

                    IntStream.range(0, numbers.size())
                            .boxed()
                            .forEach(i -> columnToValues.get(i).add(numbers.get(i)));
                }
        );

        for (Integer column : columnToValues.keySet()) {
            List<Integer> values = columnToValues.get(column);

            validators.forEach(validator -> {
                        if (values.stream().allMatch(value -> validator.predicate.test(value))) {
                            validator.add(column);
                        }
                    });
        }

        validators.sort(Comparator.comparingInt(a -> a.orderNumbers.size()));

        validators.forEach(validator -> {
            Integer no = validator.orderNumbers.get(0);
            validators.forEach(otherValidator -> {
                if (!otherValidator.equals(validator)) {
                    otherValidator.orderNumbers.remove(no);
                }
            });
        });

        List<Integer> indexes = validators.stream()
                .filter(validator -> validator.name.startsWith("departure"))
                .map(validator -> validator.orderNumbers.get(0))
                .collect(Collectors.toList());

        List<Long> myTicketValues = Arrays.stream(data.myTicket.get(0).split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        Long answer = IntStream.range(0, myTicketValues.size())
                .boxed()
                .filter(indexes::contains)
                .map(myTicketValues::get)
                .reduce(1L, (a, b) -> a * b);

        System.out.println(answer);
    }




    private static List<String> getValidTickets(Data data, List<ValidatorV2> validators) {
        return data.othersTicket.stream()
                .filter(ticket -> {
                    String[] numbers = ticket.split(",");
                    List<Integer> ticketNumbers = Arrays.stream(numbers)
                            .map(Integer::parseInt)
                            .collect(Collectors.toList());

                    return ticketNumbers.stream().allMatch(number -> validators.stream().anyMatch(validator -> validator.predicate.test(number)));
                })
                .collect(Collectors.toList());
    }
}



class ValidatorV2 {

    List<Integer> orderNumbers;
    String name;
    Predicate<Integer> predicate;

    public ValidatorV2(String name, int a, int b, int c, int d) {
        this.orderNumbers = new ArrayList<>();
        this.name = name;
        this.predicate = x -> (x >= a && x <= b) || (x >= c && x <= d);
    }

    public void add(Integer no) {
        this.orderNumbers.add(no);
    }
}