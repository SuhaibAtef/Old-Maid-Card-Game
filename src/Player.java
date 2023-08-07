import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

public class Player extends Thread {
    int turns = 4;
    Random random = new Random();
    public static int playerCount = 0;
    int id;
    final Lock turnLock;
    Player nextPlayer;
    List<Card> cards = new ArrayList<>();
    PlayerController playerController;
    boolean gameOver = false;
    public Player(int id, Lock turnLock,PlayerController playerController){
        this.id = id;
        this.turnLock = turnLock;
        this.playerController = playerController;
        System.out.println("Initializing Player No." + (this.id+1));
    }
    public void addCards(Card ...cardsToAdd) {
        for (Card card : cardsToAdd) {
            if (card != null) {
                cards.add(card);
            } else {
                throw new IllegalArgumentException("Null card cannot be added to the deck.");
            }
        }
    }
    private List<Pair> getDisposedCards(){
        // pair all cards that has the same color and value
        List<Pair> disposedCards = cards.stream()
                .flatMap(firstCard -> cards.stream().filter(secondCard -> (
                        !firstCard.equals(secondCard)
                        && firstCard.cardColor == secondCard.cardColor
                        && firstCard.cardValue == secondCard.cardValue
                )).map(secondCard -> new Pair<>(firstCard, secondCard))).distinct()
                .collect(Collectors.toList());;
        return disposedCards;
    }

    private void disposePairs(Pair<Card,Card> pair){
        System.out.println("Disposing Card: " + pair.getLeft().toString() + "\nDisposing Card: " + pair.getRight().toString());
        cards.remove(pair.getLeft());
        cards.remove(pair.getRight());

    }
    private void disposingCards(){
        if(!getDisposedCards().isEmpty()){
            System.out.println("Disposing Cards from Player No." + (this.id+1));
            getDisposedCards().forEach(this::disposePairs);
        }
    }
    private synchronized void playGame(){
        while (!gameOver) {
            if(playerController.getNextTurn()==this.id){
                if(turns==0){
                    gameOver=true;
                    System.out.println("Player No." + (this.id+1) + " is done");
                    break;
                }else {
                    turns--;
                    System.out.println("Player No." + (this.id+1) + " took turn");
                    playerController.nextTurn();
                }
            }
        }
        playerController.syncSet.add(this.id);
    }
    public int getCardsLength(){
        return cards.size();
    }

    public Card takeCard(int index){
        return cards.remove(index);
    }

    @Override
    public void run() {
        System.out.println("Initialized Player No." + (this.id+1) + " in thread " + Thread.currentThread().getName());
        playGame();
        System.out.println("Player No." + (this.id+1) + " in thread " + Thread.currentThread().getName()+" is done");

    }
}
