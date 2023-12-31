import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OldMaidGame {
    public static int numberOfPlayers = -1;
    static Deck deck;
    List<Player> players = new ArrayList<>();
    Lock turnLock = new ReentrantLock();
    PlayerController playerController;

    public OldMaidGame() {
        initializePlayers();
        initializeDeck();
        dealCards();
    }

    private void initializePlayers() {
        // get input from user for the number of players
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the number of players:");
        while (!input.hasNextInt()) {
            System.out.println("Please enter a number");
            input.next();
        }
        numberOfPlayers = input.nextInt();
        playerController = new PlayerController(numberOfPlayers, players);
        for (int i = 0; i < numberOfPlayers; i++) {
            players.add(new Player(i, turnLock, playerController));
        }

        System.out.println("Initialized All Players");

    }

    private void dealCards() {
        int cardsPerPlayer = deck.numberOfCards() / numberOfPlayers;
        int remainderCards = deck.numberOfCards() % numberOfPlayers;
        for (Player player : players) {
            turnLock.lock();
            player.addCards(deck.drawAmount(cardsPerPlayer));
            if (remainderCards > 0) {
                player.addCards(deck.draw());
                remainderCards--;
            }
            turnLock.unlock();
        }
        System.out.println("Dealt Cards for all players");

    }

    private void initializeDeck() {
        Deck.Builder deckBuilder = new Deck.Builder();

        for (Card.CardType cardType : Card.CardType.values()) {
            if (cardType == Card.CardType.JOKER) {
                deckBuilder.addCards(new Card(cardType, Card.CardValue.JOkER));
                continue;
            }
            for (Card.CardValue cardValue : Card.CardValue.values()) {
                if (cardValue == Card.CardValue.JOkER) {
                    continue;
                }
                deckBuilder.addCards(new Card(cardType, cardValue));
            }
        }
        try {
            deck = deckBuilder.build();
        } catch (Deck.DeckEmptyException e) {
            throw new RuntimeException(e);
        }
        deck.shuffle();
        System.out.println("Initialized Deck with " + deck.numberOfCards() + " cards");
    }

    public void start() {
        players.forEach(Thread::start);
        System.out.println("[MAIN] Game Started");
        while (playerController.syncSet.size() != numberOfPlayers) {

        }
        System.out.println("[MAIN] Game Ended");
    }

}
