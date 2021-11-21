package gameLogic;

public class PassAIStrategy implements AIStrategy {
    @Override
    public void doPlayerTurn(Player player, Players players, GameActions gameActions) {
        while (!players.hasCurrentPlayerFinishedRolling()) {
            gameActions.currentPlayerRoll();
        }

        gameActions.currentPlayerPass();
    }

    @Override
    public Auction.BidAdvanceToken doPlayerBid(Auction auction, Players players, int tilePosition) {
        return auction.withdrawCurrentPlayerFromAuction();
    }
}
