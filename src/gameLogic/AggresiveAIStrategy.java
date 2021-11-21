package gameLogic;

import tiles.GameTileI;

import java.util.Optional;

public class AggresiveAIStrategy implements AIStrategy {
    private final GameBoard gameBoard;

    public AggresiveAIStrategy(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    @Override
    public void doPlayerTurn(Player player, Players players, GameActions gameActions) {
        while (!players.hasCurrentPlayerFinishedRolling()) {
            Optional<GameTileI> tileOpt = gameBoard.getTile(player.getTilePosition());
            if (tileOpt.isPresent()) {
                GameTileI currentTile = tileOpt.get();

                if (currentTile.asBuyable().isPresent() && currentTile.asBuyable().get().getBuyCost() <= player.getBalance()) {
                    gameActions.currentPlayerBuy();
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {}
            }

            gameActions.currentPlayerRoll();
        }

        gameActions.currentPlayerPass();
    }
}
