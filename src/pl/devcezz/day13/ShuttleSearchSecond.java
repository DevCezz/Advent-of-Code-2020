package pl.devcezz.day13;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ShuttleSearchSecond {

    public static List<String> readData(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Not found file: " + path);
        }
    }

    public static void main(String[] args) {
        List<String> data = readData("data/day13/task.txt");

        String[] busses = data.get(1).split(",");
        List<Bus> busArrivals = IntStream.range(0, busses.length)
                .filter(index -> !busses[index].equals("x"))
                .mapToObj(minute -> {
                    int busId = Integer.parseInt(busses[minute]);
                    return new Bus(busId, busId - minute % busId);
                })
                .collect(Collectors.toList());

        long N = busArrivals.stream()
                .map(bus -> bus.busId)
                .reduce(1L, (a, b) -> a * b);

        long x = busArrivals.stream()
                .map(bus -> new Chinese(bus.minutesAfterTimestamp, N / bus.busId, bus.busId))
                .map(Chinese::bNx)
                .reduce(Long::sum)
                .orElse((long) 0);

        System.out.println(x % N);
    }
}

class Chinese {

    long b;
    long N;
    long x;

    public Chinese(long b, long n, long mod) {
        long rest = n % mod;

        long tempX = 1;
        while (rest * tempX % mod != 1) {
            tempX++;
        }

        this.b = b;
        this.N = n;
        this.x = tempX;
    }

    public long bNx() {
        return b * N * x;
    }
}

class Bus {

    long busId;
    long minutesAfterTimestamp;

    public Bus(long busId, long minutesAfterTimestamp) {
        this.busId = busId;
        this.minutesAfterTimestamp = minutesAfterTimestamp;
    }

    @Override
    public String toString() {
        return "Bus{busId=" + busId +", minutesAfterTimestamp=" + minutesAfterTimestamp + '}';
    }
}