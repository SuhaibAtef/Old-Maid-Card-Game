import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

public class Player extends Thread {
    final Lock turnLock;
    Random random = new Random();
    int id;
    List<Card> cards = new ArrayList<>();
    PlayerController playerController;

    public Player(int id, Lock turnLock, PlayerController playerController) {
        this.id = id;
        this.turnLock = turnLock;
        this.playerController = playerController;
        System.out.println("Initializing Player No." + (this.id + 1));
    }

    public void addCards(Card... cardsToAdd) {
        for (Card card : cardsToAdd) {
            if (card != null) {
                cards.add(card);
            } else {
                throw new IllegalArgumentException("Null card cannot be added to the deck.");
            }
        }
        disposingCards();
    }

    private List<Pair> getDisposedCards() {
        // pair all cards that has the same color and value
        List<Pair> disposedCards = cards.stream()
                .flatMap(firstCard -> cards.stream().filter(secondCard -> (
                        !firstCard.equals(secondCard)
                                && firstCard.cardColor == secondCard.cardColor
                                && firstCard.cardValue == secondCard.cardValue
                )).map(secondCard -> new Pair<>(firstCard, secondCard))).distinct()
                .collect(Collectors.toList());
        return disposedCards;
    }

    private void disposePairs(Pair<Card, Card> pair) {
        System.out.println("Disposing Card: " + pair.left().toString() + "\nDisposing Card: " + pair.right().toString());
        cards.remove(pair.left());
        cards.remove(pair.right());

    }

    private void disposingCards() {
        if (!getDisposedCards().isEmpty()) {
            System.out.println("Disposing Cards from Player No." + (this.id + 1));
            getDisposedCards().forEach(this::disposePairs);
            System.out.println("[Player No." + (this.id + 1) + "] Cards left: " + cards.size());
        }
    }

    private synchronized void playGame() {
        while (true) {
            if (playerController.getNextTurn() == this.id) {
                turnLock.lock();
                if (cards.isEmpty()) {
                    System.out.println("Player No." + (this.id + 1) + " is done");
                    playerController.syncSet.add(this.id);
                    playerController.nextTurn();
                    turnLock.unlock();
                    break;
                }
                playerController.nextTurn();
                disposingCards();
                if (!cards.isEmpty()) {
                    playerController.getNextTurn();
                    Player nextPlayer = playerController.getPlayer(playerController.getNextTurn());
                    if (nextPlayer == this) {
                        System.out.println("Player No." + (this.id + 1) + " is the old maid");
                        playerController.syncSet.add(this.id);
                        turnLock.unlock();
                        break;
                    }
                    int lengthCards = nextPlayer.getCardsLength();
                    if (lengthCards == 0) {
                        playerController.getNextTurn();
                        lengthCards = nextPlayer.getCardsLength();
                    }
                    System.out.println("Player No." + (this.id + 1) + " takes card from Player No." + (nextPlayer.id + 1) + " " + nextPlayer.getCardsLength());
                    int cardIndex = random.nextInt(lengthCards);
                    Card cardTaken = nextPlayer.takeCard(cardIndex);
                    System.out.println("Player No." + (this.id + 1) + " took Card: " + cardTaken.toString() + " from Player No." + (nextPlayer.id + 1));
                    cards.add(cardTaken);
                    disposingCards();
                    if (cards.isEmpty()) {
                        System.out.println("Player No." + (this.id + 1) + " is done");
                        playerController.syncSet.add(this.id);
                        turnLock.unlock();
                        break;
                    }
                } else {
                    System.out.println("Player No." + (this.id + 1) + " is done");
                    playerController.syncSet.add(this.id);
                    turnLock.unlock();
                    break;
                }

                turnLock.unlock();
            }
        }

    }

    public int getCardsLength() {
        return cards.size();
    }

    public Card takeCard(int index) {
        return cards.remove(index);
    }

    @Override
    public void run() {
        System.out.println("Initialized Player No." + (this.id + 1) + " in thread " + Thread.currentThread().getName());
        playGame();
        System.out.println("Player No." + (this.id + 1) + " in thread " + Thread.currentThread().getName() + " is done");

    }
}
