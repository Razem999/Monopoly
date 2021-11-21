package gameLogic;

import tiles.GameTileI;

import java.util.Optional;

public class AggressiveAIStrategy implements AIStrategy {
    private final GameBoard gameBoard;

    public AggressiveAIStrategy(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    private void doTileActions(Player player, Players players, GameActions gameActions) {
        Optional<GameTileI> tileOpt = gameBoard.getTile(player.getTilePosition());
        if (tileOpt.isPresent()) {
            GameTileI currentTile = tileOpt.get();

            if (currentTile.asBuyable().isPresent() && currentTile.asBuyable().get().getBuyCost() <= player.getBalance()) {
                gameActions.currentPlayerBuy();
            } else if (currentTile.asBuyable().isPresent() && Auction.AUCTION_START_PRICE <= player.getBalance()) {
                gameActions.currentPlayerStartAuction();
            }

        }
    }

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
}
