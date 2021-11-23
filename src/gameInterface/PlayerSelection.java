package gameInterface;

public class PlayerSelection {
    private int numPlayers;
    private int numAIPlayers;

    public PlayerSelection(int numPlayers, int numAIPlayers) {
        this.numPlayers = numPlayers;
        this.numAIPlayers = numAIPlayers;
    }

    /**This method is used to get the number of players including the AI
     */
    public int getNumPlayers() {
        return this.numPlayers;
    }

    /**This method is used get the number of AI players
     */
    public int getNumAIPlayers() {
        return this.numAIPlayers;
    }
}
