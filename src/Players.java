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
    private int rollStreak;
    private boolean currentPlayerHasRolled;
    private boolean currentPlayerHasActed;

    private final GameInterfaceI gameInterface;

    /**This is the constructor of Players with parameters
     * @param gameInterface This provides text for each action the player takes
     */
    Players(GameInterfaceI gameInterface) {
        this.gameInterface = gameInterface;
        this.players = createPlayerList(4, 1500);

        this.currentPlayer = 0;
        this.currentPlayerHasRolled = false;
        this.rollStreak = 0;
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

    /**This function gets the current player and lets them buy the property they are currently on.
     * @param gameBoard This provides the gameboard with all the tiles
     */
    public void currentPlayerBuy(GameBoard gameBoard) {
        Player currentPlayer = this.getCurrentPlayer();

        Optional<GameTileI> tileOpt = gameBoard.getTile(currentPlayer.getTilePosition());
        if (tileOpt.isPresent()) {
            GameTileI tile = tileOpt.get();

            Optional<BuyableI> buyableTile = tile.asBuyable();
            if (buyableTile.isPresent()) {
                buyableTile.get().buy(currentPlayer);
                this.currentPlayerHasActed = true;
            } else {
                gameInterface.notifyCannotBuyTileKind(currentPlayer, tile);
            }
        }
    }

    public void currentPlayerStartAuction(GameBoard gameBoard) {
        GameTileI tile = gameBoard.getTile(this.getCurrentPlayer().getTilePosition()).orElseThrow();

        Optional<BuyableI> buyableTile = tile.asBuyable();
        if (buyableTile.isPresent()){
            gameInterface.startAuction(10, buyableTile.get(), this);
            this.currentPlayerHasActed = true;
        } else {
            gameInterface.notifyAuctionCannotStart(tile);
        }
    }

    /**This function handles the rolls of players currently inside jail
     * @param gameBoard This provides the gameboard with all the tiles
     * @param currentPlayer The player rolling
     */
    private void handleJailedPlayerRoll(GameBoard gameBoard, Player currentPlayer) {
        int firstDie = ThreadLocalRandom.current().nextInt(1, 6 + 1);
        int secondDie = ThreadLocalRandom.current().nextInt(1, 6 + 1);
        gameInterface.notifyRoll(currentPlayer, firstDie, secondDie);
        if (firstDie == secondDie) {
            gameBoard.handleSuccessfulJailedPlayerRoll(currentPlayer);
            gameBoard.advancePlayer(currentPlayer, firstDie + secondDie, this);
        } else {
            gameBoard.handleFailedJailedPlayerRoll(currentPlayer);

            if (!gameBoard.isPlayerInJail(currentPlayer)) {
                gameBoard.advancePlayer(currentPlayer, firstDie + secondDie, this);
            }
        }

        this.currentPlayerHasRolled = true;
    }

    /**This function determine the value of the current players roll and determine if its doubles.
     * @param gameBoard This provides the gameboard with all the tiles
     */
    public void currentPlayerRoll(GameBoard gameBoard) {
        Player currentPlayer = this.getCurrentPlayer();
        if (currentPlayerHasRolled) {
            gameInterface.notifyCannotRoll(this.getCurrentPlayer());
            return;
        }

        if (gameBoard.isPlayerInJail(currentPlayer)) {
            handleJailedPlayerRoll(gameBoard, currentPlayer);
        } else {
            int firstDie = ThreadLocalRandom.current().nextInt(1, 6 + 1);
            int secondDie = ThreadLocalRandom.current().nextInt(1, 6 + 1);

            if (firstDie == secondDie) {
                rollStreak++;

                if (rollStreak == 3) {
                    gameBoard.jailPlayer(currentPlayer);
                    this.currentPlayerHasRolled = true;
                    rollStreak = 0;

                    return;
                }
            } else {
                this.currentPlayerHasRolled = true;
                rollStreak = 0;
            }

            gameInterface.notifyRoll(currentPlayer, firstDie, secondDie);
            gameBoard.advancePlayer(currentPlayer, firstDie + secondDie, this);
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
