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

    /**This method is used to get the average balance of all players in the game
     */
    static int getAverageBalance(Players players) {
        return players.getPlayersList().stream().mapToInt(Player::getBalance).sum() / players.getPlayersList().size();
    }

    /**This method is used to get the average net worth of all the players in the game
     */
    static int getAverageNetWorth(Players players, GameBoard gameBoard) {
        return players.getPlayersList().stream().mapToInt(gameBoard::getPlayerNetWorth).sum() / players.getPlayersList().size();
    }

    /**This method is used to roll and pass an AI players turn
     * @param player the AI player who is currently on the tile
     * @param players the players in the game
     * @param gameActions the actions the AI can preform
     */
    void doPlayerTurn(Player player, Players players, GameActions gameActions);
}
