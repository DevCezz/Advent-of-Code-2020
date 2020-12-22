package pl.devcezz.day22;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class CrabCombatFirst {

    public static List<String> readData(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Not found file: " + path);
        }
    }

    private static List<List<String>> prepareData(List<String> rawData) {
        List<List<String>> data = new ArrayList<>();
        List<String> row = new ArrayList<>();
        for (int i = 0; i < rawData.size(); i++) {
            if (rawData.get(i).isEmpty()) {
                data.add(row);
                row = new ArrayList<>();
            } else {
                row.add(rawData.get(i));
            }
        }
        if (!row.isEmpty()) {
            data.add(row);
        }

        return data;
    }

    public static void main(String[] args) {
        List<String> data = readData("data/day22/task.txt");
        List<List<String>> playersData = prepareData(data);

        List<PlayerDeck> players = playersData.stream()
                .map(playerData -> {
                    String playerName = playerData.get(0).replace(":", "");

                    List<Integer> deck = playerData.subList(1, playerData.size()).stream()
                            .map(Integer::parseInt)
                            .collect(Collectors.toList());

                    return new PlayerDeck(playerName, deck);
                })
                .collect(Collectors.toList());

        PlayerDeck player1 = players.get(0);
        PlayerDeck player2 = players.get(1);

        while (player1.deckHasCards() && player2.deckHasCards()) {
            Integer playerCard1 = player1.deck.pollFirst();
            Integer playerCard2 = player2.deck.pollFirst();

            if (playerCard1 > playerCard2) {
                player1.deck.add(playerCard1);
                player1.deck.add(playerCard2);
            } else {
                player2.deck.add(playerCard2);
                player2.deck.add(playerCard1);
            }
        }

        long result = 0;
        if (player1.deckHasCards()) {
            int multiplayer = 1;
            while (player1.deckHasCards()) {
                result += multiplayer++ * player1.deck.pollLast();
            }
        } else {
            int multiplayer = 1;
            while (player2.deckHasCards()) {
                result += multiplayer++ * player2.deck.pollLast();
            }
        }

        System.out.println(result);
    }
}

class PlayerDeck {

    String playerName;
    LinkedList<Integer> deck;

    public PlayerDeck(String playerName, List<Integer> deck) {
        this.playerName = playerName;
        this.deck = new LinkedList<>(deck);
    }

    public boolean deckHasCards() {
        return !deck.isEmpty();
    }
}