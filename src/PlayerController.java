import java.util.*;

public class PlayerController {
    int playersCount;
    public  volatile int playerTurn = 0;
    List<Player> players;

    public volatile Set<Integer> syncSet = Collections.synchronizedSet(new HashSet<>());
    PlayerController(int players, List<Player> playersList){
        this.playersCount = players;
        this.players = playersList;
    }
    public synchronized int getNextTurn(){
        while (syncSet.contains(playerTurn)) {
            playerTurn = (playerTurn + 1)%playersCount;
        }
        return playerTurn;
    }
    public Player getPlayer(int player){
        return players.get(player);
    }

    public void nextTurn() {
        playerTurn = (playerTurn + 1)%playersCount;
    }
}
