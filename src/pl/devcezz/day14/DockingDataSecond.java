package pl.devcezz.day14;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class DockingDataSecond {

    public static List<String> readData(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Not found file: " + path);
        }
    }

    public static void main(String[] args) {
        List<String> data = readData("data/day14/task.txt");

        MaskV2 mask = null;
        List<MaskV2> masks = new ArrayList<>();
        for (String currentRow : data) {
            if (currentRow.startsWith("mask")) {
                if (mask != null) {
                    masks.add(mask);
                }

                mask = new MaskV2(currentRow.substring("mask = ".length()));
            } else {
                String[] split = currentRow.split(" = ");
                String temp = split[0].substring("mem[".length());

                String address = Long.toBinaryString(Long.parseLong(temp.substring(0, temp.length() - 1)));
                long value = Long.parseLong(split[1]);

                mask.add(address, value);
            }
        }
        masks.add(mask);

        masks.forEach(MaskV2::throughMask);

        Map<Long, Long> finalMap = new LinkedHashMap<>();

        masks.forEach(currMask -> finalMap.putAll(currMask.values));

        Long sum = finalMap.values().stream().reduce(Long::sum).orElse(-1L);

        System.out.println(sum);
    }
}

class MaskV2 {

    String mask;
    Map<String, Long> spaceToValue = new LinkedHashMap<>();
    Map<Long, Long> values = new LinkedHashMap<>();

    public MaskV2(String mask) {
        this.mask = mask;
    }

    public void add(String address, Long value) {
        String format = String.format("%0" + (36 - address.length()) + "d%s", 0, address);
        spaceToValue.put(format, value);
    }

    public void throughMask() {
        for (String address : spaceToValue.keySet()) {

            Long value = spaceToValue.get(address);
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < mask.length(); i++) {
                char maskChar = mask.charAt(i);
                char addressChar = address.charAt(i);

                switch (maskChar) {
                    case '1':
                        builder.append('1');
                        break;
                    case 'X':
                        builder.append('X');
                        break;
                    case '0':
                        builder.append(addressChar);
                        break;
                }
            }

            String newMask = builder.toString();
            long numberOfFloats = Arrays.stream(newMask.split(""))
                    .filter(sign -> sign.equals("X"))
                    .count();

            for (int i = 0; i < numberOfFloats; i++) {
                populate(newMask.replaceFirst("X", "1"), value);
                populate(newMask.replaceFirst("X", "0"), value);
            }
        }
    }

    private void populate(String newMask, long value) {
        int indexOfX = newMask.indexOf("X");

        if (indexOfX >= 0) {
            populate(newMask.replaceFirst("X", "1"), value);
            populate(newMask.replaceFirst("X", "0"), value);
        } else {
            long address = Long.parseLong(newMask, 2);
            values.put(address, value);
        }
    }
}