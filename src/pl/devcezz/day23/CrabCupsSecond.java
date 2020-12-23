package pl.devcezz.day23;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class CrabCupsSecond {

    private static final Map<Long, Cup> CUPS_MAP = new HashMap<>();
    private static final int CUPS = 1_000_000;
    private static final int MOVES = 10_000_000;

    private static long MAX_CUPS_VALUE;

    public static List<String> readData(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Not found file: " + path);
        }
    }

    public static void main(String[] args) {
        List<String> data = readData("data/day23/task.txt");
        List<Long> cups = Arrays.stream(data.get(0).split(""))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        Cup head = null;
        Cup current = null;

        for (int i = 0; i < cups.size(); i++) {
            if (head == null) {
                head = new Cup(cups.get(i));
                current = head;
            } else {
                Cup next = new Cup(cups.get(i));
                current.next = next;
                current = next;
            }

            CUPS_MAP.put(current.value, current);
        }

        long max = CUPS_MAP.keySet().stream().max(Long::compareTo).orElse(-1L);

        for (long i = max + 1; i <= CUPS; i++) {
            Cup next = new Cup(i);
            current.next = next;
            current = next;
            CUPS_MAP.put(current.value, current);
        }
        current.next = head;

        MAX_CUPS_VALUE = CUPS_MAP.keySet().stream().max(Long::compareTo).orElse(-1L);
        for (int i = 0; i < MOVES; i++) {
            head = moveCups(head);
        }

        long result = CUPS_MAP.get(1L).next.value * CUPS_MAP.get(1L).next.next.value;
        System.out.println(result);
    }

    private static Cup moveCups(Cup head) {
        Cup firstChosen = head.next;
        Cup lastChosen = head.next.next.next;

        head.next = lastChosen.next;

        long targetValue = head.value - 1;

        while (targetValue < 1L || targetValue == firstChosen.value || targetValue == firstChosen.next.value || targetValue == lastChosen.value) {
            targetValue = (targetValue < 1L ? MAX_CUPS_VALUE : targetValue - 1);
        }

        Cup target = CUPS_MAP.get(targetValue);
        lastChosen.next = target.next;
        target.next = firstChosen;

        return head.next;
    }
}

class Cup {

    Long value;
    Cup next;

    public Cup(Long value) {
        this.value = value;
    }
};