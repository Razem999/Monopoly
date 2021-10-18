import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Players {
    List<Player> players;
    int currentPlayer;

    Players() {
        this.players = Arrays.asList(
                new Player(0, 1500),
                new Player(1, 1500),
                new Player(2, 1500),
                new Player(3, 1500)
        );
        this.currentPlayer = 0;
    }

    public Optional<Player> getPlayerByID(int id) {
        for (Player player : this.players) {
            if (player.hasID(id)) {
                return Optional.of(player);
            }
        }

        return Optional.empty();
    }

    public Player getCurrentPlayer() {
        return this.players.get(currentPlayer);
    }

    public void nextTurn() {
        this.currentPlayer = (this.currentPlayer + 1) % players.size();
    }
}
