import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class Players {
    private final List<Player> players;
    private int currentPlayer;
    private int rollStreak;
    private boolean currentPlayerHasRolled;
    private int rollsInJail;

    private final GameInterfaceI gameInterface;

    Players(GameInterfaceI gameInterface) {
        this.gameInterface = gameInterface;
        this.players = new ArrayList<>() {{
            add(new Player(0, 1500));
            add(new Player(1, 1500));
            add(new Player(2, 1500));
            add(new Player(3, 1500));
        }};
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

    public void currentPlayerBuy(GameBoard gameBoard) {
        Player currentPlayer = this.getCurrentPlayer();

        Optional<GameTileI> tileOpt = gameBoard.getTile(currentPlayer.getTilePosition());
        if (tileOpt.isPresent()) {
            GameTileI tile = tileOpt.get();

            tile.tryBuy(currentPlayer);
        }
    }

    public void currentPlayerRoll(GameBoard gameBoard) {
        Player currentPlayer = this.getCurrentPlayer();
        if (currentPlayer.isInJail() && currentPlayerHasRolled == false) {
            int firstDie = ThreadLocalRandom.current().nextInt(1, 6 + 1);
            int secondDie = ThreadLocalRandom.current().nextInt(1, 6 + 1);
            gameInterface.notifyRoll(currentPlayer, firstDie, secondDie);
            if (firstDie == secondDie) {
                leaveJail(currentPlayer, firstDie, secondDie, gameBoard);
            } else {
                gameInterface.notifyPlayerStayJail(currentPlayer);
                rollsInJail += 1;
                if (rollsInJail == 3) {
                    gameBoard.payJailFine(currentPlayer);
                    leaveJail(currentPlayer, firstDie, secondDie, gameBoard);
                    rollsInJail = 0;
                }
            }
        }
        else if (this.currentPlayerHasRolled) {
            gameInterface.notifyCannotRoll(this.getCurrentPlayer());
        } else {

            int firstDie = ThreadLocalRandom.current().nextInt(1, 6 + 1);
            int secondDie = ThreadLocalRandom.current().nextInt(1, 6 + 1);

            if (firstDie == secondDie) {
                rollStreak++;

                if (rollStreak == 3) {
                    gameBoard.sendPlayerToJail(currentPlayer);
                    this.currentPlayerHasRolled = true;
                }
            } else {
                this.currentPlayerHasRolled = true;
                rollStreak = 0;
            }

            gameInterface.notifyRoll(currentPlayer, firstDie, secondDie);
            gameBoard.advancePlayer(currentPlayer, firstDie + secondDie, this);
        }
    }

    public void removePlayer(Player player) {
        this.players.remove(player);

        this.currentPlayer = this.currentPlayer % this.players.size();
    }

    public void leaveJail(Player currentPlayer, int firstDie, int secondDie, GameBoard gameBoard) {
        currentPlayer.toggleInJail();
        gameInterface.notifyPlayerLeftJail(currentPlayer);
        gameBoard.advancePlayer(currentPlayer, firstDie + secondDie, this);
    }
}
