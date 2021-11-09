package gameLogic;

import gameInterface.GameInterfaceI;
import tiles.BuyableI;
import tiles.GameTileI;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class GameActions {
    private final GameBoard gameBoard;
    private final Players players;
    private final GameInterfaceI gameInterface;

    private int rollStreak;

    public GameActions(GameBoard gameBoard, Players players, GameInterfaceI gameInterface) {
        this.gameBoard = gameBoard;
        this.players = players;
        this.gameInterface = gameInterface;
    }

    /**This function gets the current player and lets them buy the property they are currently on.
     */
    public void currentPlayerBuy() {
        Player currentPlayer = this.players.getCurrentPlayer();

        Optional<GameTileI> tileOpt = this.gameBoard.getTile(currentPlayer.getTilePosition());
        if (tileOpt.isPresent()) {
            GameTileI tile = tileOpt.get();

            Optional<BuyableI> buyableTile = tile.asBuyable();
            if (buyableTile.isPresent()) {
                buyableTile.get().buy(currentPlayer);
                this.players.handleCurrentPlayerActed();
            } else {
                this.gameInterface.notifyCannotBuyTileKind(currentPlayer, tile);
            }
        }
    }

    public void currentPlayerPass() {
        if (this.players.hasCurrentPlayerFinishedRolling()) {
            this.gameInterface.notifyPlayerEndedTurn(this.players.getCurrentPlayer());
            this.players.nextTurn();

            this.gameInterface.notifyPlayerTurn(this.players.getCurrentPlayer());
        } else {
            this.gameInterface.notifyPlayerMustRoll(this.players.getCurrentPlayer());
        }
    }

    public void currentPlayerStartAuction() {
        GameTileI tile = this.gameBoard.getTile(this.players.getCurrentPlayer().getTilePosition()).orElseThrow();

        Optional<BuyableI> buyableTile = tile.asBuyable();
        if (buyableTile.isPresent()){
            this.gameInterface.startAuction(10, buyableTile.get(), this.players);
            this.players.handleCurrentPlayerActed();
        } else {
            this.gameInterface.notifyAuctionCannotStart(tile);
        }
    }

    /**This function handles the rolls of players currently inside jail
     * @param gameBoard This provides the gameboard with all the tiles
     * @param currentPlayer The player rolling
     */
    private void handleJailedPlayerRoll(GameBoard gameBoard, Player currentPlayer) {
        int firstDie = ThreadLocalRandom.current().nextInt(1, 6 + 1);
        int secondDie = ThreadLocalRandom.current().nextInt(1, 6 + 1);
        this.gameInterface.notifyRoll(currentPlayer, firstDie, secondDie);
        if (firstDie == secondDie) {
            gameBoard.handleSuccessfulJailedPlayerRoll(currentPlayer);
            gameBoard.advancePlayer(currentPlayer, firstDie + secondDie, this.players);
        } else {
            gameBoard.handleFailedJailedPlayerRoll(currentPlayer);

            if (!gameBoard.isPlayerInJail(currentPlayer)) {
                gameBoard.advancePlayer(currentPlayer, firstDie + secondDie, this.players);
            }
        }

        this.players.handleCurrentPlayerFinishedRolling();
    }

    /**This function determine the value of the current players roll and determine if its doubles.
     */
    public void currentPlayerRoll() {
        Player currentPlayer = this.players.getCurrentPlayer();
        if (this.players.hasCurrentPlayerFinishedRolling()) {
            this.gameInterface.notifyCannotRoll(currentPlayer);
            return;
        }

        if (this.gameBoard.isPlayerInJail(currentPlayer)) {
            handleJailedPlayerRoll(gameBoard, currentPlayer);
        } else {
            int firstDie = ThreadLocalRandom.current().nextInt(1, 6 + 1);
            int secondDie = ThreadLocalRandom.current().nextInt(1, 6 + 1);

            if (firstDie == secondDie) {
                rollStreak++;

                if (rollStreak == 3) {
                    this.gameBoard.jailPlayer(currentPlayer);
                    this.players.handleCurrentPlayerFinishedRolling();
                    rollStreak = 0;

                    return;
                }
            } else {
                this.players.handleCurrentPlayerFinishedRolling();
                rollStreak = 0;
            }

            this.gameInterface.notifyRoll(currentPlayer, firstDie, secondDie);
            this.gameBoard.advancePlayer(currentPlayer, firstDie + secondDie, this.players);
        }
    }

}
