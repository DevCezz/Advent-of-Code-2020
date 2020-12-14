package pl.devcezz.day14;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DockingDataFirst {

    public static List<String> readData(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Not found file: " + path);
        }
    }

    public static void main(String[] args) {
        List<String> data = readData("data/day14/task.txt");

        Mask mask = null;
        List<Mask> masks = new ArrayList<>();
        for (String currentRow : data) {
            if (currentRow.startsWith("mask")) {
                if (mask != null) {
                    masks.add(mask);
                }

                mask = new Mask(currentRow.substring("mask = ".length()));
            } else {
                String[] split = currentRow.split(" = ");
                String temp = split[0].substring("mem[".length());

                long address = Long.parseLong(temp.substring(0, temp.length() - 1));
                String value = Integer.toBinaryString(Integer.parseInt(split[1]));

                mask.add(address, value);
            }
        }
        masks.add(mask);

        masks.forEach(Mask::throughMask);

        Map<Long, Long> finalMap = new LinkedHashMap<>();

        masks.forEach(currMask -> finalMap.putAll(currMask.values));

        Long sum = finalMap.values().stream().reduce(Long::sum).orElse(-1L);

        System.out.println(sum);
    }
}

class Mask {

    String mask;
    Map<Long, String> spaceToValue = new LinkedHashMap<>();
    Map<Long, Long> values = new LinkedHashMap<>();

    public Mask(String mask) {
        this.mask = mask;
    }

    public void add(Long address, String value) {
        String format = String.format("%0" + (36 - value.length()) + "d%s", 0, value);
        spaceToValue.put(address, format);
    }

    public void throughMask() {
        for (Long address : spaceToValue.keySet()) {
            String value = spaceToValue.get(address);
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < mask.length(); i++) {
                char maskChar = mask.charAt(i);
                char valueChar = value.charAt(i);

                switch (maskChar) {
                    case '1':
                        builder.append('1');
                        break;
                    case 'X':
                        builder.append(valueChar);
                        break;
                    case '0':
                        builder.append('0');
                        break;
                }
            }

            values.put(address, Long.parseLong(builder.toString(), 2));
        }
    }
}