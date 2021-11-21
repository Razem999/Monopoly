package gameLogic;

import gameInterface.GameInterface;

import java.util.List;
import java.util.Optional;

public class Auction {

    /* This bid advance token allows us to statically guarantee (99% of the time) that bid executors eventually
       either make a bid or withdraw so that bugs cannot put the program into a state where the interface is stuck
       looping waiting for a executor that does no actions. More details are in the design document.
     */
    public interface BidAdvanceToken { }

    private static class BidAdvanceTokenImpl implements BidAdvanceToken { }

    private int currentPlayerIndex;
    private final List<Player> players;
    private int price;
    private Player highestBidder;

    public static int AUCTION_START_PRICE = 100;
    public static int AUCTION_MINIMUM_INCREASE = 5;

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

    public void showBetErrorIfBetInvalid(int betInput, GameInterface gameInterface) {
        Player currentBidder = this.getCurrentBidder();

        int minimumBet = this.getPrice() + Auction.AUCTION_MINIMUM_INCREASE;
        if (betInput > currentBidder.getBalance()) {
            gameInterface.notifyBetError("That bet exceeds your balance!");
        } else if (betInput < minimumBet) {
            gameInterface.notifyBetError("The that bet amount is too low! (Must be more than " + minimumBet + ")");
        }
    }

    //Takes in a bet amount and sets the new price based on the bet and sets
    //the character currently winning the property
    public Optional<BidAdvanceToken> bid(int betAmount) {
        if(betAmount >= price + Auction.AUCTION_MINIMUM_INCREASE) {
            price = betAmount;
            this.highestBidder = players.get(currentPlayerIndex);

            //this function is only called after the players bet has been checked so it automatically
            //switches to the next person betting
            advanceAuction();

            return Optional.of(new BidAdvanceTokenImpl());
        }

        return Optional.empty();
    }

    //When a player types quit, this function is called to remover the player from the players list
    public BidAdvanceToken withdrawCurrentPlayerFromAuction() {
        players.remove(currentPlayerIndex);

        if (this.currentPlayerIndex >= this.players.size()) {
            this.currentPlayerIndex = 0;
        }

        return new BidAdvanceTokenImpl();
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
