import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PlayerController {
    int playersCount;
    public  volatile int playerTurn = 0;

    public Set<Integer> syncSet = Collections.synchronizedSet(new HashSet<>());
    PlayerController(int players){
        this.playersCount = players;
    }
    public synchronized int getNextTurn(){
        while (syncSet.contains(playerTurn)) {
            playerTurn = (playerTurn + 1)%playersCount;
        }
        return playerTurn;
    }


    public void nextTurn() {
        playerTurn = (playerTurn + 1)%playersCount;
    }
}
