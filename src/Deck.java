import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

    private final List<Card> cards;
    private final List<Card> disposableCards;
    protected Card lastPlayedCard;


    private Deck() {
        cards = new ArrayList<>();
        disposableCards = new ArrayList<>();

    }

    public int numberOfCards() {
        return cards.size();
    }

    public static Deck getInstance() {
        return InstanceHolder.instance;
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card draw() {
        if (this.cards.isEmpty()) {
            this.cards.addAll(disposableCards);
            this.disposableCards.clear();
            this.shuffle();
            this.draw();
        }
        return cards.remove(cards.size() - 1);
    }

    public Card [] drawAmount(int amount) {
        Card [] cardsDrawn = cards.subList(0, amount).toArray(new Card[amount]);
        cards.subList(0, amount).clear();
        return cardsDrawn;
    }

    public void put(Card card) {
        disposableCards.add(card);
        lastPlayedCard = card;
    }

    private static final class InstanceHolder {
        private static final Deck instance = new Deck();
    }

    public static class Builder {
        private final List<Card> cards;

        public Builder() {
            cards = new ArrayList<>();
        }

        public void addCards(Card... cardsToAdd) {
            for (Card card : cardsToAdd) {
                if (card != null) {
                    cards.add(card);
                } else {
                    throw new IllegalArgumentException("Null card cannot be added to the deck.");
                }
            }
        }

        public Deck build() throws DeckEmptyException {
            if (cards.isEmpty()) {
                throw new DeckEmptyException("Deck is empty");
            }
            Deck deck = Deck.getInstance();
            deck.cards.addAll(cards);
            return deck;
        }
    }

    public static class DeckEmptyException extends Exception {
        public DeckEmptyException(String message) {
            super(message);
        }
    }
}