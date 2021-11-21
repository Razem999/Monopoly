package gameLogic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**The Player class is the Player that plays the game. Each Player has a
 * status, that is a Player ID, a balance, their positioning in the board
 * and whether they are in Jail or not, and will be performing actions in
 * the game which will lead to different events in the game.
 *
 * @version 1.0
 * @since 2021-10-25
 */
public class Player {
    private final int playerID;
    private int balance;
    private int tilePosition;
    private Optional<AIStrategy.StrategyType> aiStrategy;

    public interface PlayerChangeListener {
        void handlePlayerChange(Player player);
    }

    private final List<PlayerChangeListener> playerChangeListeners;

    /**This is the constructor of Player with parameters
     * @param id This is the Player ID
     * @param startingBalance This is the Player's starting balance
     */
    public Player(int id, int startingBalance) {
        this.playerID = id;
        this.balance = startingBalance;
        this.tilePosition = 0;
        this.aiStrategy = Optional.empty();

        this.playerChangeListeners = new ArrayList<>();
    }

    /**This is another constructor of  Player, which is used if we decide
     * to play the game with fewer Players.
     * @param id This is the Player ID
     */
    public Player(int id) {
        this(id, 0);
    }

    /**This method is used to get the tile position of the Player
     * @return int This returns the Player's tile position
     */
    public int getTilePosition() {
        return this.tilePosition;
    }

    /**This method is used to set the tile position of a Player, with parameter
     * @param tilePosition This is the Player's next tile Position
     */
    public void setTilePosition(int tilePosition) {
        this.tilePosition = tilePosition;
        this.updatePlayerChangeListeners();
    }

    /**This method is used to get the Player's current balance
     * @return int This returns the Player's balance
     */
    public int getBalance() {
        return this.balance;
    }

    /**This method is used to get the Player's ID
     * @return int This returns the PLayer's ID
     */
    public int getPlayerID() {
        return this.playerID;
    }

    /**This method is used to check if the Player has an ID
     * @param id This is the Player's ID
     * @return boolean This returns true if the Player has an ID, otherwise false
     */
    public boolean hasID(int id) {
        return this.playerID == id;
    }

    /**Overrides function equals to check if the object Provided is a Player or not
     * @param p This is the object provided for identification
     * @return This returns true if the object is a Player, otherwise false
     */
    @Override
    public boolean equals(Object p) {
        if (p instanceof Player) {
            return ((Player) p).hasID(this.playerID);
        }

        return false;
    }

    private void updatePlayerChangeListeners() {
        for (PlayerChangeListener listener : playerChangeListeners) {
            listener.handlePlayerChange(this);
        }
    }

    public void removeChangeListener() {
        this.playerChangeListeners.clear();
    }

    /**This method is used to change the Player's balance once a transaction takes place
     * between the Player and the other party
     * @param diff This is the amount the Player either pays or receives
     */
    public void changeBalance(int diff) {
        this.balance += diff;
        this.updatePlayerChangeListeners();
    }

    public void addPlayerChangeListener(PlayerChangeListener listener) {
        this.playerChangeListeners.add(listener);
    }

    public Optional<AIStrategy.StrategyType> getAIStrategy() {
        return this.aiStrategy;
    }

    public void setAIStrategy(AIStrategy.StrategyType aiStrategy) {
        this.aiStrategy = Optional.of(aiStrategy);
    }

}
