package pl.devcezz.day02;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class PasswordPhilosophySecond {

    public List<String> readData(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Not found file: " + path);
        }
    }

    public static void main(String[] args) {
        PasswordPhilosophySecond philosophy = new PasswordPhilosophySecond();
        long count = philosophy.readData("data/day02/task.txt")
                .stream().map(DataSecond::new)
                .filter(DataSecond::passwordIsValid)
                .count();

        System.out.println(count);
    }
}

class DataSecond {

    public final int firstIndex;
    public final int secondIndex;
    public final String letter;
    public final String input;

    public DataSecond(String line) {
        String[] data = line.split(" ");

        String[] indexes = data[0].split("-");
        String[] letter = data[1].split(":");

        this.firstIndex = Integer.parseInt(indexes[0]) - 1;
        this.secondIndex = Integer.parseInt(indexes[1]) - 1;

        this.letter = letter[0];
        this.input = data[2];
    }

    public boolean passwordIsValid() {
        String[] split = input.split("");

        return split[firstIndex].equals(letter) ^ split[secondIndex].equals(letter);
    }
}