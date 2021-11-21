package gameLogic;

public class DefaultAIStrategy implements AIStrategy {
    private final GameBoard gameBoard;

    public DefaultAIStrategy(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }
    @Override
    public void doPlayerTurn(Player player, Players players, GameActions gameActions) {

    }
}
