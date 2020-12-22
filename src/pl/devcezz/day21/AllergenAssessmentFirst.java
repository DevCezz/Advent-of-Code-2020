package pl.devcezz.day21;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class AllergenAssessmentFirst {

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

        long count = products.stream()
                .map(product -> product.ingredients)
                .flatMap(Set::stream)
                .filter(ingredient -> !allergenToPossibleIngredients.values().stream()
                        .flatMap(Set::stream)
                        .collect(Collectors.toSet())
                        .contains(ingredient))
                .count();

        System.out.println(count);
    }
}

class Product {

    Set<String> ingredients;
    Set<String> allergens;

    public Product(Set<String> ingredients, Set<String> allergens) {
        this.ingredients = ingredients;
        this.allergens = allergens;
    }

    public boolean containsAllergen(String allergen) {
        return allergens.contains(allergen);
    }
}