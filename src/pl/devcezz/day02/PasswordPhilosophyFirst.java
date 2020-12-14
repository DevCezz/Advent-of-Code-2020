package pl.devcezz.day02;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class PasswordPhilosophyFirst {

    public List<String> readData(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Not found file: " + path);
        }
    }

    public static void main(String[] args) {
        PasswordPhilosophyFirst philosophy = new PasswordPhilosophyFirst();
        long count = philosophy.readData("data/day02/task.txt")
                .stream().map(DataFirst::new)
                .filter(DataFirst::passwordIsValid)
                .count();

        System.out.println(count);
    }
}

class DataFirst {

    public final int lowerBound;
    public final int upperBound;
    public final String letter;
    public final String input;

    public DataFirst(String line) {
        String[] data = line.split(" ");

        String[] bounds = data[0].split("-");
        String[] letter = data[1].split(":");

        this.lowerBound = Integer.parseInt(bounds[0]);
        this.upperBound = Integer.parseInt(bounds[1]);

        this.letter = letter[0];
        this.input = data[2];
    }

    public boolean passwordIsValid() {
        String[] split = input.split("");

        long count = Arrays.stream(split)
                .filter(letter -> letter.equals(this.letter))
                .count();

        return lowerBound <= count && count <= upperBound;
    }
}