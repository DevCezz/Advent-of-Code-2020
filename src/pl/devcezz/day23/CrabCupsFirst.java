package pl.devcezz.day23;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class CrabCupsFirst {

    private static final int MOVES = 100;

    public static List<String> readData(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Not found file: " + path);
        }
    }

    public static void main(String[] args) {
        List<String> data = readData("data/day23/task.txt");
        LinkedList<Integer> cups = Arrays.stream(data.get(0).split(""))
                .map(Integer::parseInt)
                .collect(Collectors.toCollection(LinkedList::new));

        for (int i = 0; i < MOVES; i++) {
            Integer head = cups.pollFirst();

            List<Integer> toMove = new ArrayList<>(cups.subList(0, 3));
            cups.removeAll(toMove);

            Integer nextHead = head - 1;
            while (!cups.contains(nextHead)) {
                nextHead = nextHead - 1;
                if (nextHead <= 0) {
                    nextHead = 9;
                }
            }

            int nextHeadIndex = cups.indexOf(nextHead);
            nextHeadIndex++;
            cups.addAll(nextHeadIndex, toMove);
            cups.add(head);
        }

        while (cups.peek() != 1) {
            int moving = cups.pollFirst();
            cups.add(moving);
        }
        cups.removeFirst();

        String order = cups.stream()
                .map(Object::toString)
                .collect(Collectors.joining(""));

        System.out.println(order);
    }
}
