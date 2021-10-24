import java.util.List;

public class Auction {
    private int currentPlayerIndex;
    private List<Player> players;
    private int price;
    private Player highestBidder;

    public Auction(List<Player> players, Player player, int price) {
        currentPlayerIndex = -1;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).equals(player)) {
                currentPlayerIndex = (i + 1) % players.size();
                break;
            }
        }
        if (currentPlayerIndex == -1) {
            throw new IllegalArgumentException("Player does not exist");
        }

        this.players = players;
        this.price = price;
    }

    private void advanceAuction() {
        currentPlayerIndex = (currentPlayerIndex + 1) % (players.size() - 1);
    }

    public void bet(int betAmount) {
        price = betAmount;
        this.highestBidder = players.get(currentPlayerIndex);

        advanceAuction();
    }

    public void withdrawCurrentPlayerFromAuction() {
        players.remove(currentPlayerIndex);
        advanceAuction();
    }

    public List<Player> getPlayerList() {
        return players;
    }

    public int getPrice() {
        return price;
    }
}
