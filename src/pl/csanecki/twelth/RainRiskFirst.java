package pl.csanecki.twelth;

import pl.csanecki.twelth.important.Instruction;
import pl.csanecki.twelth.important.Position;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class RainRiskFirst {

    public static List<String> readData(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Not found file: " + path);
        }
    }

    public static void main(String[] args) {
        List<Instruction> instructions = readData("data/twelfth/task.txt").stream()
                .map(Instruction::new)
                .collect(Collectors.toList());
        Position position = new Position();

        instructions.forEach(instruction -> instruction.apply(position));

        System.out.println(position);
        System.out.println(position.count());
    }
}

