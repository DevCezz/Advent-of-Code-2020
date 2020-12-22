package pl.devcezz.day21;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class AllergenAssessmentSecond {

    public static List<String> readData(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Not found file: " + path);
        }
    }

    public static void main(String[] args) {
        List<String> data = readData("data/day21/task.txt");

        List<Product> products = data.stream()
                .map(line -> {
                    String[] pairs = line.split(" \\(contains ");
                    Set<String> ingredients = new HashSet<>(Arrays.asList(pairs[0].split(" ")));
                    Set<String> allergens = new HashSet<>(Arrays.asList(pairs[1].replace(")", "").split(", ")));

                    return new Product(ingredients, allergens);
                })
                .collect(Collectors.toList());

        Set<String> allAllergens = products.stream()
                .map(product -> product.allergens)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());

        Set<String> allIngredients = products.stream()
                .map(product -> product.ingredients)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());

        Map<String, Set<String>> allergenToPossibleIngredients = allAllergens.stream()
                .collect(Collectors.toMap(allergen -> allergen, allergen -> new HashSet<>()));

        for (String allergen : allAllergens) {
            Set<String> possibleIngredients = new HashSet<>(allIngredients);

            products.stream()
                .filter(product -> product.containsAllergen(allergen))
                .map(product -> product.ingredients)
                .forEach(possibleIngredients::retainAll);

            allergenToPossibleIngredients.get(allergen).addAll(possibleIngredients);
        }

        Set<String> safeIngredients = products.stream()
                .map(product -> product.ingredients)
                .flatMap(Set::stream)
                .filter(ingredient -> !allergenToPossibleIngredients.values().stream()
                        .flatMap(Set::stream)
                        .collect(Collectors.toSet())
                        .contains(ingredient))
                .collect(Collectors.toSet());

        boolean allHaveOne = false;

        do {
            Set<String> matchedIngredients = allergenToPossibleIngredients.keySet().stream()
                    .filter(allergen -> allergenToPossibleIngredients.get(allergen).size() == 1)
                    .map(allergen -> allergenToPossibleIngredients.get(allergen))
                    .flatMap(Set::stream)
                    .collect(Collectors.toSet());

            Set<String> notMatched = allergenToPossibleIngredients.keySet().stream()
                    .filter(allergen -> allergenToPossibleIngredients.get(allergen).size() > 1)
                    .collect(Collectors.toSet());

            notMatched.forEach(allergen -> allergenToPossibleIngredients.get(allergen).removeAll(matchedIngredients));

            allHaveOne = allergenToPossibleIngredients.keySet().stream()
                    .allMatch(allergen -> allergenToPossibleIngredients.get(allergen).size() == 1);
        } while (!allHaveOne);

        TreeMap<String, Set<String>> sorted = new TreeMap<>(allergenToPossibleIngredients);

        String answer = sorted.keySet().stream()
                .map(sorted::get)
                .map(ArrayList::new)
                .flatMap(List::stream)
                .collect(Collectors.joining(","));

        System.out.println(answer);
    }
}