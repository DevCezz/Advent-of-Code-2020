package pl.csanecki.seventh;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class HandyHaversacksFirst {

    public static Set<String> cuonterBags = new HashSet<>();

    public static List<String> readData(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Not found file: " + path);
        }
    }

    public static void main(String[] args) {
        List<BagContainBags> bags = readData("data/seventh/task.txt").stream()
                .map(line -> line.split(" contain "))
                .map(data -> new BagContainBags(data[0], data[1]))
                .collect(Collectors.toList());

        String myBag = "shiny gold";

        iterate(bags, myBag);

        System.out.println(cuonterBags.size());
    }

    private static void iterate(List<BagContainBags> bags, String myBag) {
        for (BagContainBags bag : bags) {
            if (bag.contains(myBag)) {
                cuonterBags.add(bag.key);
                iterate(bags, bag.key);
            }
        }
    }
}

class BagContainBags {

    String key;
    Set<CanContain> values;

    public BagContainBags(String key, String values) {
        this.key = key.replace(" bags", "").replace(" bag", "");
        this.values = Arrays.stream(values.split(", "))
                .filter(value -> !value.contains("no other bags"))
                .map(value -> value.replace(" bags", ""))
                .map(value -> value.replace(" bag", ""))
                .map(value -> value.replace(".", ""))
                .map(value -> {
                    int firstSpace = value.indexOf(" ");
                    return new CanContain(Integer.parseInt(value.substring(0, firstSpace)), value.substring(firstSpace + 1));
                })
                .collect(Collectors.toSet());
    }

    public boolean contains(String bag) {
        return values.stream()
                .filter(canContain -> canContain.bagName.equals(bag))
                .count() == 1;
    }
}

class CanContain {

    int number;
    String bagName;

    public CanContain(int number, String bagName) {
        this.number = number;
        this.bagName = bagName;
    }
}