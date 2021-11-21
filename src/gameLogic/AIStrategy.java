package gameLogic;

import gameInterface.AuctionBidExecutor;

public interface AIStrategy extends AuctionBidExecutor {
    enum StrategyType {
        AGGRESSIVE,
        DEFAULT,
    }

    class Factory {
        private final GameBoard gameBoard;

        public Factory(GameBoard gameBoard) {
            this.gameBoard = gameBoard;
        }

        public AIStrategy getAIStrategy(StrategyType strategyType) {
            switch (strategyType) {
                case AGGRESSIVE -> {
                    return new AggressiveAIStrategy(this.gameBoard);
                }
                case DEFAULT -> {
                    return new DefaultAIStrategy(this.gameBoard);
                }
                default -> {
                    return new PassAIStrategy();
                }
            }
        }
    }

    static int getAverageBalance(Players players) {
        return players.getPlayersList().stream().mapToInt(Player::getBalance).sum() / players.getPlayersList().size();
    }

    static int getAverageNetWorth(Players players, GameBoard gameBoard) {
        return players.getPlayersList().stream().mapToInt(gameBoard::getPlayerNetWorth).sum() / players.getPlayersList().size();
    }

    void doPlayerTurn(Player player, Players players, GameActions gameActions);
}
