package save;

import gameLogic.AIStrategy;
import gameLogic.Players;

import java.io.Serializable;
import java.util.List;

public class PlayersSave implements Serializable {
    private final List<PlayerSave> players;
    private final int currentPlayer;
    private final boolean currentPlayerHasRolled;
    private final boolean currentPlayerHasActed;
    private final List<AIStrategy.StrategyType> aiPlayers;

    public PlayersSave(List<PlayerSave> players, int currentPlayer, boolean currentPlayerHasRolled, boolean currentPlayerHasActed, List<AIStrategy.StrategyType> aiPlayers) {
        this.players = players;
        this.currentPlayer = currentPlayer;
        this.currentPlayerHasRolled = currentPlayerHasRolled;
        this.currentPlayerHasActed = currentPlayerHasActed;
        this.aiPlayers = aiPlayers;
    }

    public Players getPlayers(AIStrategy.Factory aiFactory) {
        return new Players(players, currentPlayer, currentPlayerHasRolled, currentPlayerHasActed, aiFactory);
    }

    public List<AIStrategy.StrategyType> getAiPlayers() {
        return aiPlayers;
    }
}

