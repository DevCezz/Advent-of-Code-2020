package pl.devcezz.day22;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class CrabCombatSecond {

    private static int LEVEL = 1;

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

        List<Integer> momentoDeck1 = new LinkedList<>(player1.deck);
        List<Integer> momentoDeck2 = new LinkedList<>(player2.deck);

        Set<Integer> momento = new HashSet<>();
        while (player1.deckHasCards() && player2.deckHasCards()) {

            if (!momento.add(player1.deck.hashCode() * 45 + player2.deck.hashCode() * 23)) {
                player1.deck.addAll(player2.deck);
                player2.deck.clear();
                break;
            }

            Integer playerCard1 = player1.deck.pollFirst();
            Integer playerCard2 = player2.deck.pollFirst();

            if (playerCard1 <= player1.deck.size() && playerCard2 <= player2.deck.size()) {
                int winner = playSubGame(new LinkedList<>(player1.deck.subList(0, playerCard1)), new LinkedList<>(player2.deck.subList(0, playerCard2)));

                if (winner == 1) {
                    player1.deck.add(playerCard1);
                    player1.deck.add(playerCard2);
                } else {
                    player2.deck.add(playerCard2);
                    player2.deck.add(playerCard1);
                }
            } else {
                if (playerCard1 > playerCard2) {
                    player1.deck.add(playerCard1);
                    player1.deck.add(playerCard2);
                } else {
                    player2.deck.add(playerCard2);
                    player2.deck.add(playerCard1);
                }
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

    private static int playSubGame(LinkedList<Integer> playerDeck1, LinkedList<Integer> playerDeck2) {
        Set<Integer> momento = new HashSet<>();

        while (!playerDeck1.isEmpty() && !playerDeck2.isEmpty()) {
            if (!momento.add(playerDeck1.hashCode() * 45 + playerDeck2.hashCode() * 23)) {
                return 1;
            }

            Integer playerCard1 = playerDeck1.pollFirst();
            Integer playerCard2 = playerDeck2.pollFirst();

            if (playerCard1 <= playerDeck1.size() && playerCard2 <= playerDeck2.size()) {
                int winner = playSubGame(new LinkedList<>(playerDeck1.subList(0, playerCard1)), new LinkedList<>(playerDeck2.subList(0, playerCard2)));
                if (winner == 1) {
                    playerDeck1.add(playerCard1);
                    playerDeck1.add(playerCard2);
                } else {
                    playerDeck2.add(playerCard2);
                    playerDeck2.add(playerCard1);
                }
            } else {
                if (playerCard1 > playerCard2) {
                    playerDeck1.add(playerCard1);
                    playerDeck1.add(playerCard2);
                } else {
                    playerDeck2.add(playerCard2);
                    playerDeck2.add(playerCard1);
                }
            }
        }

        if (playerDeck1.isEmpty()) {
            return 2;
        } else {
            return 1;
        }
    }
}