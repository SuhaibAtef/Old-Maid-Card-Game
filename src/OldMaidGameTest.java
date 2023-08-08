import org.junit.jupiter.api.RepeatedTest;
import java.io.ByteArrayInputStream;

class OldMaidGameTest {

    @RepeatedTest(value = 100)
    void start() {
        System.setIn(new ByteArrayInputStream("3".getBytes()));
        OldMaidGame oldMaidGame = new OldMaidGame();
        oldMaidGame.start();
    }
}