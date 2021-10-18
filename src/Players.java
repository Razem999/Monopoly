import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class Players {
    private List<Player> players;
    private int currentPlayer;
    private int rollStreak;
    private boolean currentPlayerHasRolled;

    private final GameInterfaceI gameInterface;

    Players(GameInterfaceI gameInterface) {
        this.gameInterface = gameInterface;
        this.players = Arrays.asList(
                new Player(0, 1500),
                new Player(1, 1500),
                new Player(2, 1500),
                new Player(3, 1500)
        );
        this.currentPlayer = 0;
        this.currentPlayerHasRolled = false;
        this.rollStreak = 0;
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

    public boolean canEndCurrentTurn() {
        return this.currentPlayerHasRolled;
    }

    public void nextTurn() {
        this.currentPlayer = (this.currentPlayer + 1) % players.size();
        this.currentPlayerHasRolled = false;
    }

    public boolean currentPlayerRoll(GameBoard gameBoard) {
        if (this.currentPlayerHasRolled) {
            return false;
        } else {
            Player currentPlayer = this.getCurrentPlayer();

            int firstDie = ThreadLocalRandom.current().nextInt(1, 6 + 1);
            int secondDie = ThreadLocalRandom.current().nextInt(1, 6 + 1);

            if (firstDie == secondDie) {
                rollStreak++;

                if (rollStreak == 3) {
                    gameBoard.sendPlayerToJail(currentPlayer);
                }
            } else {
                this.currentPlayerHasRolled = true;
            }

            gameInterface.notifyRoll(currentPlayer, firstDie, secondDie);
            gameBoard.advancePlayer(currentPlayer, firstDie + secondDie, this);

            return true;
        }
    }
}
