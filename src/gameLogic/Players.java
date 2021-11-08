package gameLogic;

import gameInterface.GameInterfaceI;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The Players class represents all the players currently playing the game
 *
 * @version 1.0
 * @since 2021-10-25
 */
public class Players {
    private final List<Player> players;
    private int currentPlayer;
    private boolean currentPlayerHasRolled;
    private boolean currentPlayerHasActed;

    private final GameInterfaceI gameInterface;

    /**This is the constructor of Players with parameters
     * @param gameInterface This provides text for each action the player takes
     */
    public Players(GameInterfaceI gameInterface) {
        this.gameInterface = gameInterface;
        this.players = createPlayerList(4, 1500);

        this.currentPlayer = 0;
        this.currentPlayerHasRolled = false;
        this.currentPlayerHasActed = false;
    }

    /**This function gets the list of players playing the game
     */
    public List<Player> getPlayersList() {
        return new ArrayList<>(this.players);
    }

    /**This function creates the list of players by adding new players and randomizing turn order
     * @param numPlayers This provides the number of players wishing to play the game
     * @param defaultMoney This provides the amount of money each person start the game with
     */
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

    /**This function finds a player in the game given an id.
     * @param id This provides the id of the player the function will look for
     */
    public Optional<Player> getPlayerByID(int id) {
        for (Player player : this.players) {
            if (player.hasID(id)) {
                return Optional.of(player);
            }
        }

        return Optional.empty();
    }

    /**This function gets and returns the player who is currently getting their turn
     */
    public Player getCurrentPlayer() {
        return this.players.get(currentPlayer);
    }

    /**This function determines if the player has rolled since the player must roll before the next player can
     */
    public boolean canEndCurrentTurn() {
        return this.currentPlayerHasRolled;
    }

    /**This function goes to the next turn allowing the next player in the order to roll
     */
    public void nextTurn() {
        this.currentPlayer = (this.currentPlayer + 1) % players.size();
        this.currentPlayerHasRolled = false;
        this.currentPlayerHasActed = false;
    }

    /**This function removes a player from the game by removing them from the player list thereby
     * removing them from the turn order
     *
     * @param player This provides the player to remove from the player list and game
     */
    public void removePlayer(Player player) {
        this.players.remove(player);

        this.currentPlayer = this.currentPlayer % this.players.size();
    }

    public void handleCurrentPlayerActed() {
        this.currentPlayerHasActed = true;
    }

    public boolean hasCurrentPlayerActed() {
        return this.currentPlayerHasActed;
    }

    public void handleCurrentPlayerFinishedRolling() {
        this.currentPlayerHasRolled = true;
    }

    public boolean hasCurrentPlayerFinishedRolling() {
        return this.currentPlayerHasRolled;
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
