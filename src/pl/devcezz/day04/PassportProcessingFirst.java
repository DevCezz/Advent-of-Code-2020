package pl.devcezz.day04;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PassportProcessingFirst {

    enum PartsOfPassport {
        BYR("byr", false),
        IYR("iyr", false),
        EYR("eyr", false),
        HGT("hgt", false),
        HCL("hcl", false),
        ECL("ecl", false),
        PID("pid", false),
        CID("cid", true);

        public final String code;
        public final boolean isOptional;

        PartsOfPassport(String code, boolean isOptional) {
            this.code = code;
            this.isOptional = isOptional;
        }

        public static boolean isPassportValid(String passport) {
            return Arrays.stream(PartsOfPassport.values())
                    .allMatch(part -> {
                        if (part.isOptional) {
                            return true;
                        } else {
                            return passport.contains(part.code);
                        }
                    });
        }
    }

    public List<String> readData(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Not found file: " + path);
        }
    }

    private List<String> processPassports(List<String> data) {
        List<String> passports = new ArrayList<>();
        StringBuilder lineToAdd = new StringBuilder();

        for (String line : data) {
            if (!line.isEmpty()) {
                lineToAdd.append(" ").append(line);
            } else {
                passports.add(lineToAdd.toString());
                lineToAdd = new StringBuilder();
            }
        }

        if (lineToAdd.length() != 0) {
            passports.add(lineToAdd.toString());
        }

        return passports;
    }

    public static void main(String[] args) {
        PassportProcessingFirst processor = new PassportProcessingFirst();
        List<String> data = processor.readData("data/day04/task.txt");

        List<String> passports = processor.processPassports(data);

        long count = passports.stream()
                .filter(PartsOfPassport::isPassportValid)
                .count();

        System.out.println(count);
    }
}
