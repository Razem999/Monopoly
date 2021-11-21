package gameLogic;

import tiles.GameTileI;

import java.util.List;
import java.util.Optional;

public class DefaultAIStrategy implements AIStrategy {
    private final GameBoard gameBoard;

    public DefaultAIStrategy(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    private int determineTileScore(int tilePosition, Player player) {
        int score = 40;
        List<GameTileI> neighbourhood = gameBoard.getTileNeighbourhood(tilePosition, 3);

        int ownedByPlayer = neighbourhood.stream().mapToInt(tile -> {
            if (tile.asBuyable().isPresent()) {
                if (tile.asBuyable().get().isOwnedBy(player)) {
                    return 1;
                }
            }

            return 0;
        }).sum();

        int ownedByOthers = neighbourhood.stream().mapToInt(tile -> {
            if (tile.asBuyable().isPresent()) {
                if (tile.asBuyable().get().hasOwner() && !tile.asBuyable().get().isOwnedBy(player)) {
                    return 1;
                }
            }

            return 0;
        }).sum();

        // Incentivize filling out areas where player owns many properties
        if (ownedByPlayer > ownedByOthers) {
            score += 20;
        }

        // Incentivize filling out areas where player owns no properties at all
        if (ownedByPlayer == 0) {
            score += 10;
        }

        return score;
    }

    private int getTileScoreBuyThreshold(Players players, Player player, GameTileI currentTile) {
        int score = 50;

        int avgNetWorth = AIStrategy.getAverageNetWorth(players, this.gameBoard);

        // Incentivize buying at the start of the game
        if (avgNetWorth <= 300) {
            score -= 30;
        }

        // Incentivize buying when player owns less than average
        if (gameBoard.getPlayerNetWorth(player) <= avgNetWorth) {
            score -= 10;
        }

        // Incentivize buying when player has a lot of money
        if (player.getBalance() > 1500) {
            score -= 10;
        }

        // Disincentivize buying when the player is low on money
        if (player.getBalance() < 500) {
            score += 20;
        }

        return score;
    }

    private int getTileScoreAuctionThreshold(Players players, Player player, GameTileI currentTile) {
        int score = 40;

        // Incentivize auctioning when player owns less than average
        int avgNetWorth = AIStrategy.getAverageNetWorth(players, this.gameBoard);
        if (gameBoard.getPlayerNetWorth(player) <= avgNetWorth) {
            score -= 10;
        }

        // Disincentivize aucitioning when player balance lower than average
        if (player.getBalance() <= AIStrategy.getAverageBalance(players)) {
            score += 20;
        }

        return score;
    }

    private void doTileActions(Player player, Players players, GameActions gameActions) {
        Optional<GameTileI> tileOpt = gameBoard.getTile(player.getTilePosition());
        if (tileOpt.isPresent()) {
            GameTileI currentTile = tileOpt.get();

            if (currentTile.asBuyable().isPresent() && currentTile.asBuyable().get().getBuyCost() <= player.getBalance()) {
                if (this.determineTileScore(player.getTilePosition(), player) >= this.getTileScoreBuyThreshold(players, player, currentTile)) {
                    gameActions.currentPlayerBuy();
                }
            } else if (currentTile.asBuyable().isPresent() && Auction.AUCTION_START_PRICE <= player.getBalance()) {
                gameActions.currentPlayerStartAuction();
                if (this.determineTileScore(player.getTilePosition(), player) >= this.getTileScoreAuctionThreshold(players, player, currentTile)) {
                    gameActions.currentPlayerBuy();
                }
            }
        }
    }

    @Override
    public void doPlayerTurn(Player player, Players players, GameActions gameActions) {
        while (!players.hasCurrentPlayerFinishedRolling()) {
            doTileActions(player, players, gameActions);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {}

            gameActions.currentPlayerRoll();

            doTileActions(player, players, gameActions);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {}

        }

        gameActions.currentPlayerPass();
    }
}
