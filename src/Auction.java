import java.util.List;

public class Auction {
    private int currentPlayerIndex;
    private List<Player> players;
    private int price;
    private Player highestBidder;

    /**
     * The Auction class represents the ability to auction properties from the original game,
     * where all unowned properties that are landed on can be auctioned off rather than bought for base price.
     *
     * @param players This is the list of players entering the auction
     * @param player This is the player that initiated the auction
     * @param price This is the minimum price the property must sell for in the auction
     **/
    public Auction(List<Player> players, Player player, int price) {
        currentPlayerIndex = -1;

        //finds where the player is in the players ArrayList.
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).equals(player)) {
                currentPlayerIndex = i % players.size();
                break;
            }
        }
        //If the player can't be found throws an exception
        if (currentPlayerIndex == -1) {
            throw new IllegalArgumentException("Player does not exist");
        }

        this.players = players;
        this.price = price;
    }

    //When called it changes the current player to the next player in the ArrayList
    private void advanceAuction() {
        //if all but one have withdrawn from the auction, case accounts for not being able to mod by 0
        if (players.size() == 1) {
            currentPlayerIndex = 0;
        } else {
            currentPlayerIndex = (currentPlayerIndex + 1) % (players.size() - 1);
        }
    }

    //Takes in a bet amount and sets the new price based on the bet and sets
    //the character currently winning the property
    public void bet(int betAmount) {
        price = betAmount;
        this.highestBidder = players.get(currentPlayerIndex);

        //this function is only called after the players bet has been checked so it automatically
        //switches to the next person betting
        advanceAuction();
    }

    //When a player types quit, this function is called to remover the player from the players list
    public void withdrawCurrentPlayerFromAuction() {
        players.remove(currentPlayerIndex);
        advanceAuction();
    }

    //Gets the current list of active players in the auction
    public List<Player> getPlayerList() {
        return players;
    }

    //gets the current price/highest bid in the auction
    public int getPrice() {
        return price;
    }

    //gets the player who has placed the highest bid
    public Player getHighestBidder() {
        return highestBidder;
    }

    //gets the index of the current player in the ArrayList of players currently in the auction
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }
}
