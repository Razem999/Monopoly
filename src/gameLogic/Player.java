package gameLogic;

import java.util.ArrayList;
import java.util.List;

/**The gameLogic.Player class is the gameLogic.Player that plays the game. Each gameLogic.Player has a
 * status, that is a gameLogic.Player ID, a balance, their positioning in the board
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

    private final List<PlayerBalanceChangeListener> playerBalanceChangeListeners;

    /**This is the constructor of gameLogic.Player with parameters
     * @param id This is the gameLogic.Player ID
     * @param startingBalance This is the gameLogic.Player's starting balance
     */
    Player(int id, int startingBalance) {
        this.playerID = id;
        this.balance = startingBalance;
        this.tilePosition = 0;

        this.playerBalanceChangeListeners = new ArrayList<>();
    }

    /**This is another constructor of gameLogic.Player, which is used if we decide
     * to play the game with fewer gameLogic.Players.
     * @param id This is the gameLogic.Player ID
     */
    Player(int id) {
        this(id, 0);
    }

    /**This method is used to get the tile position of the gameLogic.Player
     * @return int This returns the gameLogic.Player's tile position
     */
    public int getTilePosition() {
        return this.tilePosition;
    }

    /**This method is used to set the tile position of a gameLogic.Player, with parameter
     * @param tilePosition This is the gameLogic.Player's next tile Position
     */
    public void setTilePosition(int tilePosition) {
        this.tilePosition = tilePosition;
    }

    /**This method is used to get the gameLogic.Player's current balance
     * @return int This returns the gameLogic.Player's balance
     */
    public int getBalance() {
        return this.balance;
    }

    /**This method is used to get the gameLogic.Player's ID
     * @return int This returns the PLayer's ID
     */
    public int getPlayerID() {
        return this.playerID;
    }

    /**This method is used to check if the gameLogic.Player has an ID
     * @param id This is the gameLogic.Player's ID
     * @return boolean This returns true if the gameLogic.Player has an ID, otherwise false
     */
    public boolean hasID(int id) {
        return this.playerID == id;
    }

    /**Overrides function equals to check if the object Provided is a gameLogic.Player or not
     * @param p This is the object provided for identification
     * @return This returns true if the object is a gameLogic.Player, otherwise false
     */
    @Override
    public boolean equals(Object p) {
        if (p instanceof Player) {
            return ((Player) p).hasID(this.playerID);
        }

        return false;
    }

    /**This method is used to add Objects that will rely on the gameLogic.Player's balance change
     * and take actions
     * @param listener This is the object that will be notified of any changes in the gameLogic.Player's balance
     */
    public void registerPlayerBalanceChangeListener(PlayerBalanceChangeListener listener) {
        this.playerBalanceChangeListeners.add(listener);
    }

    /**This method is used to change the gameLogic.Player's balance once a transaction takes place
     * between the gameLogic.Player and the other party
     * @param diff This is the amount the gameLogic.Player either pays or receives
     */
    public void changeBalance(int diff) {
        int oldBalance = this.balance;
        this.balance += diff;

        for (PlayerBalanceChangeListener listener : playerBalanceChangeListeners) {
            listener.onBalanceChange(this, oldBalance, this.balance);
        }
    }
}
