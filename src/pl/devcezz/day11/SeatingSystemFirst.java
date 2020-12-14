package pl.devcezz.day11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class SeatingSystemFirst {

    private static boolean CHANGED = false;

    public static List<String> readData(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Not found file: " + path);
        }
    }

    private static Field[][] prepareData(List<String> data) {
        int rows = data.size();
        int columns = data.get(0).length();

        Field[][] fields = new Field[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                fields[i][j] = new Field(data.get(i).charAt(j));
            }
        }

        return fields;
    }

    public static void main(String[] args) {
        List<String> data = readData("data/day11/task.txt");
        Field[][] fields = prepareData(data);

        Field[][] newState = fields;
        do {
            for (int i = 0; i < newState.length; i++) {
                for (int j = 0; j < newState[i].length; j++) {
                    System.out.print(newState[i][j].place.place);
                }
                System.out.println();
            }
            System.out.println("--------------------------");
            newState = newState(newState);
        } while (CHANGED);

        int counter = 0;
        for (int i = 0; i < newState.length; i++) {
            for (int j = 0; j < newState[i].length; j++) {
                if (newState[i][j].place == Place.TAKEN) {
                    counter++;
                }
            }
        }

        System.out.println(counter);
    }

    private static Field[][] newState(Field[][] fields) {
        CHANGED = false;

        Field[][] newStatefields = new Field[fields.length][fields[0].length];
        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields[i].length; j++) {
                Field currentField = fields[i][j];

                int occupied = 0;
                for (int k = -1; k < 2; k++) {
                    for (int l = -1; l < 2; l++) {
                        if ((i + k < 0 || i + k >= fields.length) || (j + l < 0 || j + l >= fields[i].length) || (k == 0 && l == 0)) {
                            continue;
                        }

                        if (fields[i + k][j + l].place == Place.TAKEN) {
                            occupied++;
                        }
                    }
                }

                Field newField = new Field(currentField.place.place);
                if (currentField.place == Place.TAKEN && occupied > 3) {
                    newField.place = Place.SEAT;
                    CHANGED = true;
                }

                if (currentField.place == Place.SEAT && occupied == 0) {
                    newField.place = Place.TAKEN;
                    CHANGED = true;
                }

                newStatefields[i][j] = newField;
            }
        }

        return newStatefields;
    }
}

enum Place {

    SEAT('L'),
    FLOOR('.'),
    TAKEN('#');

    public char place;

    Place(char place) {
        this.place = place;
    }

    public static Place of(char place) {
        return Arrays.stream(values())
                .filter(value -> value.place == place)
                .findFirst()
                .orElse(FLOOR);
    }
}

class Field {

    Place place;

    public Field(char place) {
        this.place = Place.of(place);
    }
}