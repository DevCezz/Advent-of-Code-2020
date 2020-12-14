package pl.devcezz.day07;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class HandyHaversacksSecond {

    public static int counter = 0;

    public static List<String> readData(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Not found file: " + path);
        }
    }

    public static void main(String[] args) {
        List<BagContainBags> bags = readData("data/day07/task.txt").stream()
                .map(line -> line.split(" contain "))
                .map(data -> new BagContainBags(data[0], data[1]))
                .collect(Collectors.toList());

        String myBag = "shiny gold";

        iterate(bags, myBag, 1);

        System.out.println(counter);
    }

    private static void iterate(List<BagContainBags> bags, String myBag, int multiplier) {
        for (BagContainBags bag : bags) {
            if (bag.key.equals(myBag)) {
                for (CanContain canContain : bag.values) {
                    counter += multiplier * canContain.number;
                    iterate(bags, canContain.bagName, multiplier * canContain.number);
                }
            }
        }
    }
}

