package pl.csanecki.eighth;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HandheldHaltingFirst {

    public static List<String> readData(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Not found file: " + path);
        }
    }

    public static void main(String[] args) {
        List<InstructionGame> instructionGames = readData("data/eighth/task.txt").stream()
                .map(line -> {
                    String[] data = line.split(" ");
                    return new InstructionGame(data[0], data[1]);
                })
                .collect(Collectors.toList());

        int index = 0;
        int acceleration = 0;

        while(true) {
            InstructionGame instructionGame = instructionGames.get(index);
            if (instructionGame.wasExecuted) {
                break;
            }

            switch (instructionGame.type) {
                case NOP:
                    index++;
                    break;
                case ACC:
                    acceleration += instructionGame.value;
                    index++;
                    break;
                case JMP:
                    index += instructionGame.value;
            }

            instructionGame.wasExecuted = true;
        }

        System.out.println(acceleration);
    }
}

class InstructionGame {

    Type type;
    int value;
    boolean wasExecuted;

    public InstructionGame(String instruction, String value) {
        this.type = Type.of(instruction);
        this.value = Integer.parseInt(value);
        this.wasExecuted = false;
    }

    enum Type {
        ACC("acc"),
        JMP("jmp"),
        NOP("nop");

        String instruction;

        Type(String instruction) {
            this.instruction = instruction;
        }

        static Type of(String inputInstruction) {
            return Arrays.stream(Type.values())
                    .filter(type -> type.instruction.equals(inputInstruction))
                    .findFirst()
                    .orElse(null);
        }
    }
}