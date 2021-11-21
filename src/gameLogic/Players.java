package gameLogic;

import gameInterface.GameInterfaceI;
import gameInterface.PlayerSelection;
import gameInterface.PlayersDrawable;
import tiles.GameTileI;

import java.util.*;
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
    private Player.PlayerChangeListener playerChangeListener;
    private Optional<GameActions> aiActionHandler;
    private final AIStrategy.Factory aiFactory;

    public Players(AIStrategy.Factory aiFactory) {
        this.players = new ArrayList<>();
        this.aiActionHandler = Optional.empty();

        this.currentPlayer = 0;
        this.currentPlayerHasRolled = false;
        this.currentPlayerHasActed = false;

        this.aiFactory = aiFactory;
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
    private static List<Player> createPlayerList(int numPlayers, int defaultMoney) {
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

    private void doAIActions(Player player) {
        AIStrategy aiStrategy = this.aiFactory.getAIStrategy(player.getAIStrategy().get());
        this.aiActionHandler.ifPresent(aiActionHandler -> {
            aiStrategy.doPlayerTurn(player, this, aiActionHandler);
        });
    }

    /**This function goes to the next turn allowing the next player in the order to roll
     */
    public void nextTurn() {
        this.currentPlayer = (this.currentPlayer + 1) % players.size();
        this.currentPlayerHasRolled = false;
        this.currentPlayerHasActed = false;


        Player currentPlayer = this.getCurrentPlayer();
        if (currentPlayer.getAIStrategy().isPresent()) {
            this.doAIActions(currentPlayer);
        }
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

    public PlayersDrawable getPlayerDrawables() {
        return new PlayersDrawable(this.players);
    }

    public void setAIActionHandler(GameActions aiActionHandler) {
        this.aiActionHandler = Optional.of(aiActionHandler);
    }

    public void addPlayerChangeListener(Player.PlayerChangeListener playerChangeListener) {
        this.playerChangeListener = playerChangeListener;
        for (Player player : this.players) {
            player.addPlayerChangeListener(playerChangeListener);
        }
    }

    public void createPlayers(PlayerSelection playerSelection) {
        List<AIStrategy.StrategyType> aiStrategies = new ArrayList<>();
        aiStrategies.add(AIStrategy.StrategyType.AGGRESSIVE);
        aiStrategies.add(AIStrategy.StrategyType.DEFAULT);

        this.players.forEach(Player::removeChangeListener);
        this.players.clear();
        this.players.addAll(createPlayerList(playerSelection.getNumPlayers(), 1500));
        for (int i = 0; i < playerSelection.getNumAIPlayers(); i++) {
            AIStrategy.StrategyType randomStrategy = aiStrategies.get(ThreadLocalRandom.current().nextInt(aiStrategies.size()));
            this.players.get(this.players.size() - i - 1).setAIStrategy(randomStrategy);
        }

        addPlayerChangeListener(this.playerChangeListener);

        if (this.getCurrentPlayer().getAIStrategy().isPresent()) {
            this.doAIActions(this.getCurrentPlayer());
        }
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
