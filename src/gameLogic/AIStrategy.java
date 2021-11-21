package gameLogic;

public interface AIStrategy {
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
                    return new AggresiveAIStrategy(this.gameBoard);
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

    void doPlayerTurn(Player player, Players players, GameActions gameActions);
}
