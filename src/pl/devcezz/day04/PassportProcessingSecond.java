package pl.devcezz.day04;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;

public class PassportProcessingSecond {

    enum PartsOfPassport {
        BYR("byr", false, (line) -> {
            int year = Integer.parseInt(line);
            return year >= 1920 && year <= 2002;
        }),
        IYR("iyr", false, (line) -> {
            int year = Integer.parseInt(line);
            return year >= 2010 && year <= 2020;
        }),
        EYR("eyr", false, (line) -> {
            int year = Integer.parseInt(line);
            return year >= 2020 && year <= 2030;
        }),
        HGT("hgt", false, (line) -> {
            if (line.contains("cm")) {
                String[] height = line.split("cm");
                int value = Integer.parseInt(height[0]);
                return value >= 150 && value <= 193;
            } else if (line.contains("in")) {
                String[] height = line.split("in");
                int value = Integer.parseInt(height[0]);
                return value >= 59 && value <= 76;
            } else {
                return false;
            }
        }),
        HCL("hcl", false, (line) -> {
            return line.matches("#[0-9a-f]{6}$");
        }),
        ECL("ecl", false, (line) -> {
            List<String> values = Arrays.asList("amb", "blu", "brn", "gry", "grn", "hzl", "oth");
            return values.contains(line);
        }),
        PID("pid", false, (line) -> {
            return line.matches("^[0-9]{9}$");
        }),
        CID("cid", true, (line) -> true);

        public final String code;
        public final boolean isOptional;
        public final Predicate<String> validate;

        PartsOfPassport(String code, boolean isOptional, Predicate<String> validate) {
            this.code = code;
            this.isOptional = isOptional;
            this.validate = validate;
        }

        public static boolean isPassportValid(Map<PartsOfPassport, String> parts) {
            return Arrays.stream(PartsOfPassport.values())
                    .allMatch(part -> {
                        if (part.isOptional) {
                            return true;
                        } else {
                            return parts.containsKey(part) && part.validate.test(parts.get(part));
                        }
                    });
        }

        public static PartsOfPassport getPassportPart(String code) {
            return Arrays.stream(PartsOfPassport.values())
                    .filter(partCode -> partCode.code.equals(code))
                    .findFirst()
                    .orElse(null);
        }
    }

    public List<String> readData(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Not found file: " + path);
        }
    }

    private List<Map<PartsOfPassport, String>> processPassports(List<String> data) {
        List<Map<PartsOfPassport, String>> passports = new ArrayList<>();
        StringBuilder lineToAdd = new StringBuilder();

        for (String line : data) {
            if (!line.isEmpty()) {
                lineToAdd.append(" ").append(line);
            } else {
                extractPassport(passports, lineToAdd);
                lineToAdd = new StringBuilder();
            }
        }

        if (lineToAdd.length() != 0) {
            extractPassport(passports, lineToAdd);
        }

        return passports;
    }

    private void extractPassport(List<Map<PartsOfPassport, String>> passports, StringBuilder lineToAdd) {
        String passport = lineToAdd.toString();
        String[] parts = passport.split(" ");

        Map<PartsOfPassport, String> partsOfPassports = new HashMap<>();
        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }

            String[] passportParts = part.split(":");
            partsOfPassports.put(PartsOfPassport.getPassportPart(passportParts[0]), passportParts[1]);
        }


        passports.add(partsOfPassports);
    }

    public static void main(String[] args) {
        PassportProcessingSecond processor = new PassportProcessingSecond();
        List<String> data = processor.readData("data/day04/task.txt");

        List<Map<PartsOfPassport, String>> passports = processor.processPassports(data);

        long count = passports.stream()
                .filter(PartsOfPassport::isPassportValid)
                .count();

        System.out.println(count);
    }
}
