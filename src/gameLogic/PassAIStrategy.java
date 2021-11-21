package gameLogic;

public class PassAIStrategy implements AIStrategy {
    @Override
    public void doPlayerTurn(Player player, Players players, GameActions gameActions) {
        while (!players.hasCurrentPlayerFinishedRolling()) {
            gameActions.currentPlayerRoll();
        }

        gameActions.currentPlayerPass();
    }
}
