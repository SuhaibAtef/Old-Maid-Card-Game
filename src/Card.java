public class Card {
    CardType cardType;
    CardValue cardValue;
    CardColor cardColor;
    Card(CardType cardType, CardValue cardValue) {
        this.cardType = cardType;
        this.cardValue = cardValue;
        switch (cardType){
            case CLUBS:
            case SPADES:
                this.cardColor = CardColor.BLACK;
                break;
            case DIAMONDS:
            case HEARTS:
                this.cardColor = CardColor.RED;
                break;
            case JOKER:
                this.cardColor = CardColor.JOKER;
        }
    }

    @Override
    public String toString() {
        if (cardValue == CardValue.JOkER) {
            return "Joker";
        }
        return cardValue + " of " + cardType;
    }


    enum CardColor {
        RED,
        BLACK,
        JOKER
    }
    enum CardValue {
        ACE,
        TWO,
        THREE,
        FOUR,
        FIVE,
        SIX,
        SEVEN,
        EIGHT,
        NINE,
        TEN,
        JACK,
        QUEEN,
        KING,
        JOkER
    }
    enum CardType {
        CLUBS,
        DIAMONDS,
        HEARTS,
        SPADES,
        JOKER
    }
}
