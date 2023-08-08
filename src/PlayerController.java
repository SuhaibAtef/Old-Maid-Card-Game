import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlayerController {
    public volatile int playerTurn = 0;
    public volatile Set<Integer> syncSet = Collections.synchronizedSet(new HashSet<>());
    int playersCount;
    List<Player> players;

    PlayerController(int players, List<Player> playersList) {
        this.playersCount = players;
        this.players = playersList;
    }

    public synchronized int getNextTurn() {
        while (syncSet.contains(playerTurn)) {
            playerTurn = (playerTurn + 1) % playersCount;
        }
        return playerTurn;
    }

    public Player getPlayer(int player) {
        return players.get(player);
    }

    public void nextTurn() {
        playerTurn = (playerTurn + 1) % playersCount;
    }
}
