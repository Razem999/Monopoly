package gameInterface;

public class PlayerSelection {
    private int numPlayers;
    private int numAIPlayers;

    public PlayerSelection(int numPlayers, int numAIPlayers) {
        this.numPlayers = numPlayers;
        this.numAIPlayers = numAIPlayers;
    }

    public int getNumPlayers() {
        return this.numPlayers;
    }

    public int getNumAIPlayers() {
        return this.numAIPlayers;
    }
}
