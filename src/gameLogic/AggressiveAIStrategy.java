package gameLogic;

import tiles.GameTile;

import java.util.Optional;

public class AggressiveAIStrategy implements AIStrategy {
    private final GameBoard gameBoard;

    public AggressiveAIStrategy(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    /**This method is used to determine if an AI player will buy or auction a property it has landed on
     * @param player the AI player who is currently on the tile
     * @param players the players in the game
     * @param gameActions the actions that can be preformed by the AI
     */
    private void doTileActions(Player player, Players players, GameActions gameActions) {
        Optional<GameTile> tileOpt = gameBoard.getTile(player.getTilePosition());
        if (tileOpt.isPresent()) {
            GameTile currentTile = tileOpt.get();

            if (currentTile.asBuyable().isPresent() && currentTile.asBuyable().get().getBuyCost() <= player.getBalance()) {
                gameActions.currentPlayerBuy();
            } else if (currentTile.asBuyable().isPresent() && Auction.AUCTION_START_PRICE <= player.getBalance()) {
                gameActions.currentPlayerStartAuction();
            }
        }
    }

    /**This method is used to get the AI player to roll and pass their turn
     * @param player the AI player who is currently on the tile
     * @param players the players in the game
     * @param gameActions the actions that can be preformed by the AI
     */
    @Override
    public void doPlayerTurn(Player player, Players players, GameActions gameActions) {
        while (!players.hasCurrentPlayerFinishedRolling()) {
            this.doTileActions(player, players, gameActions);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {}

            gameActions.currentPlayerRoll();

            this.doTileActions(player, players, gameActions);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {}
        }

        gameActions.currentPlayerPass();
    }

    /**This method is used to get the AI player to bid in the auction
     * @param auction the current auction being held
     * @param players the players in the game
     * @param tilePosition the position on the game board of the tile being auctioned
     */
    @Override
    public Auction.BidAdvanceToken doPlayerBid(Auction auction, Players players, int tilePosition) {
        if (auction.getPrice() <= auction.getCurrentBidderBalance()) {
            return auction.bid(auction.getPrice() + Auction.AUCTION_MINIMUM_INCREASE)
                    .orElse(auction.withdrawCurrentPlayerFromAuction());
        } else {
            return auction.withdrawCurrentPlayerFromAuction();
        }
    }
}
