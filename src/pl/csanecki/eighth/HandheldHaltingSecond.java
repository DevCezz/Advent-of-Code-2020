package pl.csanecki.eighth;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HandheldHaltingSecond {

    public static List<String> readData(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Not found file: " + path);
        }
    }

    public static void main(String[] args) {
        List<InstructionGame> instructionGames = getData();


        List<InstructionGame> properInstructionsToEnd = getFiniteInstructions(instructionGames);

        int index = 0;
        int acceleration = 0;

        clear(properInstructionsToEnd);

        while(true) {
            InstructionGame instructionGame = properInstructionsToEnd.get(index);
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

            if (index == properInstructionsToEnd.size()) {
                break;
            }

            instructionGame.wasExecuted = true;
        }

        System.out.println(acceleration);
    }

    private static List<InstructionGame> getData() {
        return readData("data/eighth/task.txt").stream()
                .map(line -> {
                    String[] data = line.split(" ");
                    return new InstructionGame(data[0], data[1]);
                })
                .collect(Collectors.toList());
    }

    private static List<InstructionGame> getFiniteInstructions(List<InstructionGame> instructionGames) {
        int testIndex = 0;
        while(true) {
            InstructionGame instructionGame = instructionGames.get(testIndex);
            if (instructionGame.wasExecuted) {
                return new ArrayList<>();
            }

            if (instructionGame.type == InstructionGame.Type.JMP || instructionGame.type == InstructionGame.Type.NOP) {
                List<InstructionGame> properInstructionsToEnd = copyWithChange(instructionGames, testIndex);
                if (!isInfinite(properInstructionsToEnd)) {
                    return properInstructionsToEnd;
                }
            }

            testIndex = testIndex + valueToAddToIndex(instructionGame);

            instructionGame.wasExecuted = true;
        }
    }

    private static void clear(List<InstructionGame> copy) {
        copy.forEach(value -> value.wasExecuted = false);
    }

    private static List<InstructionGame> copyWithChange(List<InstructionGame> original, int index) {
        return IntStream.range(0, original.size())
                .mapToObj(currentIndex -> {
                    InstructionGame current = original.get(currentIndex);
                    InstructionGame.Type newType = current.type;

                    if (index == currentIndex) {
                        if (newType == InstructionGame.Type.JMP) {
                            newType = InstructionGame.Type.NOP;
                        } else if (newType == InstructionGame.Type.NOP) {
                            newType = InstructionGame.Type.JMP;
                        }
                    }

                    return new InstructionGame(newType.instruction, String.valueOf(current.value));
                }).collect(Collectors.toList());
    }

    private static boolean isInfinite(List<InstructionGame> data) {
        int index = 0;

        while(true) {
            InstructionGame instructionGame = data.get(index);
            if (instructionGame.wasExecuted) {
                return true;
            }

            index = index + valueToAddToIndex(instructionGame);

            instructionGame.wasExecuted = true;

            if (index == data.size() - 1) {
                return false;
            }
        }
    }

    private static int valueToAddToIndex(InstructionGame instructionGame) {
        switch (instructionGame.type) {
            case NOP:
            case ACC:
                return 1;
            case JMP:
                return instructionGame.value;
            default:
                return 0;
        }
    }
}