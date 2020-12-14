package pl.devcezz.day11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class SeatingSystemSecond {

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
        Field[][] newState = prepareData(data);
        do {
            printFields(newState);
            newState = newState(newState);
        } while (CHANGED);

        printOccupiedFields(newState);
    }

    private static Field[][] newState(Field[][] fields) {
        CHANGED = false;

        Field[][] newStatefields = new Field[fields.length][fields[0].length];

        int size = Math.max(fields.length, fields[0].length);
        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields[i].length; j++) {
                Field currentField = fields[i][j];

                int occupied = 0;
                // LT
                for (int k = 1; k < size; k++) {
                    if (i - k < 0 || j - k < 0) {
                        break;
                    }

                    if (fields[i - k][j - k].place == Place.SEAT) {
                        break;
                    }

                    if (fields[i - k][j - k].place == Place.TAKEN) {
                        occupied++;
                        break;
                    }
                }

                // T
                for (int k = 1; k < size; k++) {
                    if (j - k < 0) {
                        break;
                    }

                    if (fields[i][j - k].place == Place.SEAT) {
                        break;
                    }

                    if (fields[i][j - k].place == Place.TAKEN) {
                        occupied++;
                        break;
                    }
                }

                // RT
                for (int k = 1; k < size; k++) {
                    if (i + k >= fields.length || j - k < 0) {
                        break;
                    }

                    if (fields[i + k][j - k].place == Place.SEAT) {
                        break;
                    }

                    if (fields[i + k][j - k].place == Place.TAKEN) {
                        occupied++;
                        break;
                    }
                }

                // L
                for (int k = 1; k < size; k++) {
                    if (i - k < 0) {
                        break;
                    }

                    if (fields[i - k][j].place == Place.SEAT) {
                        break;
                    }

                    if (fields[i - k][j].place == Place.TAKEN) {
                        occupied++;
                        break;
                    }
                }

                // R
                for (int k = 1; k < size; k++) {
                    if (i + k >= fields.length) {
                        break;
                    }

                    if (fields[i + k][j].place == Place.SEAT) {
                        break;
                    }

                    if (fields[i + k][j].place == Place.TAKEN) {
                        occupied++;
                        break;
                    }
                }

                // LD
                for (int k = 1; k < size; k++) {
                    if (i - k < 0 || j + k >= fields[i].length) {
                        break;
                    }

                    if (fields[i - k][j + k].place == Place.SEAT) {
                        break;
                    }

                    if (fields[i - k][j + k].place == Place.TAKEN) {
                        occupied++;
                        break;
                    }
                }

                // D
                for (int k = 1; k < size; k++) {
                    if (j + k >= fields[i].length) {
                        break;
                    }

                    if (fields[i][j + k].place == Place.SEAT) {
                        break;
                    }

                    if (fields[i][j + k].place == Place.TAKEN) {
                        occupied++;
                        break;
                    }
                }

                // RD
                for (int k = 1; k < size; k++) {
                    if (i + k >= fields.length || j + k >= fields[i].length) {
                        break;
                    }

                    if (fields[i + k][j + k].place == Place.SEAT) {
                        break;
                    }

                    if (fields[i + k][j + k].place == Place.TAKEN) {
                        occupied++;
                        break;
                    }
                }

                Field newField = new Field(currentField.place.place);
                if (currentField.place == Place.TAKEN && occupied > 4) {
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

    private static void printFields(Field[][] newState) {
        for (int i = 0; i < newState.length; i++) {
            for (int j = 0; j < newState[i].length; j++) {
                System.out.print(newState[i][j].place.place);
            }
            System.out.println();
        }
        System.out.println("--------------------------");
    }

    private static void printOccupiedFields(Field[][] newState) {
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
}