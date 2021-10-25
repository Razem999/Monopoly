import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;
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
        this.players = createPlayerList(4, 1500);

        this.currentPlayer = 0;
        this.currentPlayerHasRolled = false;
        this.rollStreak = 0;
    }
    public List<Player> getPlayersList() {
        return new ArrayList<>(this.players);
    }

    private List<Player> createPlayerList(int numPlayers, int defaultMoney) {
        PriorityQueue<PlayerWithRoll> playersPriority = new PriorityQueue<>(numPlayers);
        for (int i = 0; i < numPlayers; i++) {
            int firstDie = ThreadLocalRandom.current().nextInt(1, 6 + 1);
            int secondDie = ThreadLocalRandom.current().nextInt(1, 6 + 1);

            playersPriority.add(new PlayerWithRoll(new Player(i, defaultMoney), firstDie + secondDie));
        }

        List<Player> players = new ArrayList<>();
        while (!playersPriority.isEmpty()) {
            players.add(playersPriority.poll().player);
        }

        return players;
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

/**
 * The PlayerWithRoll class represents each player with their given priority based on how they roll
 * at the beginning of the game. This class help to determine turn order
 *
 * @version 1.0
 * @since 2021-10-25
 */
class PlayerWithRoll implements Comparable<PlayerWithRoll> {
    public Player player;
    public int roll;

    /**This is the constructor of PlayerWithRoll with parameters
     * @param player the player who has just roll
     * @param roll the value of the players roll
     */
    public PlayerWithRoll(Player player, int roll) {
        this.player = player;
        this.roll = roll;
    }

    /**This function compares a player with a roll to a new roll by another player
     * @param player This provides the player who has a roll already assigned to them
     */
    public int compareTo(PlayerWithRoll player) {
        return Integer.compare(this.roll, player.roll);
    }
}
