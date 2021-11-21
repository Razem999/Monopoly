package gameLogic;

import gameInterface.GameInterfaceI;

import java.util.List;

public class Auction {
    private int currentPlayerIndex;
    private final List<Player> players;
    private int price;
    private Player highestBidder;

    public static int AUCTION_START_PRICE = 10;

    /**
     * The Auction class represents the ability to auction properties from the original game,
     * where all unowned properties that are landed on can be auctioned off rather than bought for base price.
     *
     * @param players This is the list of players entering the auction
     * @param player This is the player that initiated the auction
     **/
    public Auction(List<Player> players, Player player) {
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
        this.price = AUCTION_START_PRICE;
    }

    //When called it changes the current player to the next player in the ArrayList
    private void advanceAuction() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    //Takes in a bet amount and sets the new price based on the bet and sets
    //the character currently winning the property
    public void bet(int betAmount) {
        if(betAmount > price) {
            price = betAmount;
            this.highestBidder = players.get(currentPlayerIndex);

            //this function is only called after the players bet has been checked so it automatically
            //switches to the next person betting
            advanceAuction();
        } else {
            System.out.println("You must bet higher than $" + price + ".");
        }
    }

    //When a player types quit, this function is called to remover the player from the players list
    public void withdrawCurrentPlayerFromAuction() {
        players.remove(currentPlayerIndex);

        if (this.currentPlayerIndex >= this.players.size()) {
            this.currentPlayerIndex = 0;
        }
    }

    //gets the current price/highest bid in the auction
    public int getPrice() {
        return price;
    }

    //gets the player who has placed the highest bid
    public Player getHighestBidder() {
        return highestBidder;
    }

    public Player getCurrentBidder() {
        return this.players.get(currentPlayerIndex);
    }

    public int getCurrentBidderBalance() {
        return this.getCurrentBidder().getBalance();
    }

    public boolean shouldEnd() {
        return this.players.size() <= 1;
    }
}
