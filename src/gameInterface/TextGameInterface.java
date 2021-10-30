package gameInterface;

import tiles.BuyableI;
import tiles.GameTileI;
import gameLogic.Players;
import gameLogic.Player;
import gameLogic.GameBoard;
import gameLogic.Auction;

import java.util.Locale;
import java.util.Scanner;


/**The gameInterface.TextGameInterface consists of text-based responses that the gameLogic.Player will receive,
 * once he has taken an action or an event passes. The interface provides the gameLogic.Player
 * with information, which helps the gameLogic.Player in deciding the next course of actions to
 * take.
 */
public class TextGameInterface implements GameInterfaceI {
    private final Scanner scanner;

    /**
     * This is the constructor of gameInterface.TextGameInterface
     */
    public TextGameInterface() {
        this.scanner = new Scanner(System.in);
    }

    /**Overrides function startAuction in gameInterface.GameInterfaceI and displays information regarding the status of the auction.
     * This method keeps on requesting an amount higher than the highest bid from each gameLogic.Player, till the gameLogic.Players
     * quit the auction and there is only one remaining bidder.
     * @param startingBid This is the starting amount of the tile the gameLogic.Players are trying to acquire
     * @param tile This is the tile being auctioned
     * @param players These are the players taking part in the auction
     */
    @Override
    public void startAuction(int startingBid, BuyableI tile, Players players) {
        System.out.println("An auction is starting for " + tile.getName() + " for $" + startingBid);
        Auction auction = new Auction(players.getPlayersList(), players.getCurrentPlayer(), 10);

        while (!auction.shouldEnd()) {
            System.out.println("The current highest bid is $" + auction.getPrice());
            doPlayerBid(auction);
        }

        if (auction.getHighestBidder() != null) {
            tile.closeAuctionFor(auction.getPrice(), auction.getHighestBidder());
        } else {
            System.out.println("No one has purchased this property");
        }
    }

    /**This method is used to take the amount the gameLogic.Player bid and display it to other gameLogic.Players. If the gameLogic.Player
     * enters quit as their bid, the gameLogic.Player withdraws from the auction.
     * @param auction This is the auction the gameLogic.Players are partaking in
     */
    private void doPlayerBid(Auction auction) {
        Player currentBidder = auction.getCurrentBidder();
        int currentBidderBalance = auction.getCurrentBidderBalance();
        System.out.println("gameLogic.Player " + currentBidder.getPlayerID() + " is placing a bet for (type 'quit' to withdraw): ");

        while (true) {
            String betInput = scanner.nextLine();
            if (betInput.equalsIgnoreCase("quit")) {
                auction.withdrawCurrentPlayerFromAuction();
                break;
            }
            try {
                if ((Integer.parseInt(betInput) <= currentBidderBalance) && (Integer.parseInt(betInput) > auction.getPrice())) {
                    auction.bet(Integer.parseInt(betInput));
                    break;
                } else if (Integer.parseInt(betInput) <= auction.getPrice()) {
                    System.out.println(">> The that bet amount is too low! (Must be more than " + auction.getPrice() + ")");
                } else {
                    System.out.println(">> That bet exceeds your balance!");
                }
            } catch (NumberFormatException ex) {
                System.out.println(">> That input is not a number!");
            }
        }
    }

    /**Override function processSale in gameInterface.GameInterfaceI and checks to see if the gameLogic.Player is going to purchase
     * the property
     * @param tileName This is the name of the property
     * @param amount This is the cost of the property
     * @param buyer This is the gameLogic.Player buying the property
     * @return This returns true if the gameLogic.Player buys the property, otherwise false
     */
    @Override
    public boolean processSale(String tileName, int amount, Player buyer) {
        System.out.println("The property " + tileName +
                " sells for $" + amount + " are you sure you want to buy it gameLogic.Player " + buyer.getPlayerID() + "?\n" +
                "[Y/N]");

        while (true) {
            String response = scanner.nextLine();

            if (response.toLowerCase(Locale.ROOT).equals("y")) {
                return true;
            } else if (response.toLowerCase(Locale.ROOT).equals("n")) {
                return false;
            } else {
                System.out.println("That's not an option!");
            }
        }
    }

    @Override
    public void displayPlayerProperties(Player player, GameBoard gameBoard) {
        System.out.println("gameLogic.Player " + player.getPlayerID() + " owns:");
        for (BuyableI buyableTile : gameBoard.getTilesOwnedByPlayer(player)) {
            System.out.println(buyableTile.getName());
        }
    }

    /**Overrides function notifyPlayerDeclinedPurchase in gameInterface.GameInterfaceI and notifies gameLogic.Players
     * that a gameLogic.Player declined to purchase a property
     * @param player This is the gameLogic.Player who declined purchase
     * @param tileName This is the name of the property the gameLogic.Player declined to purchase
     */
    @Override
    public void notifyPlayerDeclinedPurchase(Player player, String tileName) {
        System.out.println("gameLogic.Player " + player.getPlayerID() + " declined to purchase " + tileName);
    }

    /**Overrides function notifyPlayerPurchaseConfirm in gameInterface.GameInterfaceI and notifies gameLogic.Players
     * that a gameLogic.Player purchased a property
     * @param player This is the gameLogic.Player who purchased the property
     * @param tileName This is the name of the property the gameLogic.Player purchased
     * @param amount This is the cost of the property the gameLogic.Player purchased it for
     */
    @Override
    public void notifyPlayerPurchaseConfirm(Player player, String tileName, int amount) {
        System.out.println("gameLogic.Player " + player.getPlayerID() + " bought " + tileName +
                " for $" + amount);
    }

    /**Overrides function notifyRentPayment in gameInterface.GameInterfaceI and notifies gameLogic.Players
     * that a gameLogic.Player payed rent to another gameLogic.Player
     * @param owner This is the gameLogic.Player/owner who receives payment
     * @param payer This is the gameLogic.Player/payer who pays
     * @param amount This is the amount the payer is paying to the owner
     */
    @Override
    public void notifyRentPayment(Player owner, Player payer, int amount) {
        System.out.println("gameLogic.Player " + payer.getPlayerID() + " pays $" + amount + " in rent to gameLogic.Player " + owner.getPlayerID());
    }

    /**Overrides function notifyBankruptcy in gameInterface.GameInterfaceI and notifies gameLogic.Players
     * that a gameLogic.Player has been bankrupted
     * @param player This is the gameLogic.Player who has been bankrupted
     */
    @Override
    public void notifyBankruptcy(Player player) {
        System.out.println("gameLogic.Player " + player.getPlayerID() + " has gone bankrupt!");
    }

    /**Overrides function notifyRoll in gameInterface.GameInterfaceI and notifies gameLogic.Players
     * that a gameLogic.Player has rolled the dice and displays what they have rolled
     * @param player This is the gameLogic.Player rolling
     * @param firstRoll This is the roll of the first die
     * @param secondRoll This is the roll of the second die
     */
    @Override
    public void notifyRoll(Player player, int firstRoll, int secondRoll) {
        int rollTotal = firstRoll + secondRoll;
        if (firstRoll == secondRoll) {
            System.out.println("gameLogic.Player " + player.getPlayerID() + " got a double roll of " +
                    rollTotal + "! (" + firstRoll + ", " + secondRoll + ")");
        } else {
            System.out.println("gameLogic.Player " + player.getPlayerID() + " rolled " +
                    rollTotal + " (" + firstRoll + ", " + secondRoll + ")");
        }
    }

    /**Overrides function notifyPassGo in gameInterface.GameInterfaceI and notifies gameLogic.Players
     * that a gameLogic.Player has passed go
     * @param player This is the gameLogic.Player who has passed Go
     */
    @Override
    public void notifyPassGo(Player player) {
        System.out.println("gameLogic.Player " + player.getPlayerID() + " has passed go and gained $200");
    }

    /**Overrides function notifyPlayerMovement in gameInterface.GameInterfaceI and notifies gameLogic.Players
     * that a gameLogic.Player has moved to a new tile position in the board
     * @param player This is the gameLogic.Player moving
     * @param tilesMoved This is the number of tiles the gameLogic.Player has moved
     * @param newPosition This is the new position the gameLogic.Player is in after moving
     * @param destinationDescription This is the description of the new position the gameLogic.Player has landed on
     */
    @Override
    public void notifyPlayerMovement(Player player, int tilesMoved, int newPosition, String destinationDescription) {
        System.out.println("gameLogic.Player " + player.getPlayerID() + " has moved " +
                tilesMoved + " tiles onto tile number " + newPosition + ":\n" + destinationDescription);
    }

    /**Overrides function notifyCannotRoll in gameInterface.GameInterfaceI and notifies gameLogic.Players
     * that a gameLogic.Player cannot roll again
     * @param player This is the gameLogic.Player who cannot roll again
     */
    @Override
    public void notifyCannotRoll(Player player) {
        System.out.println("You already rolled!");
    }

    /**Overrides function notifyCannotBuyTileKind in gameInterface.GameInterfaceI and notifies gameLogic.Players
     * that a gameLogic.Player cannot buy a tile kind
     * @param player This is the gameLogic.Player who is attempting to buy a tile
     * @param tile This is the tile that cannot be bought
     */
    @Override
    public void notifyCannotBuyTileKind(Player player, GameTileI tile) {
        System.out.println("gameLogic.Player " + player.getPlayerID() +
                        " cannot buy this tile because it is not a buyable tile.\n" +
                        tile.tileDescription()
                );
    }

    /**Overrides function notifyCannotBuyAlreadyOwned in gameInterface.GameInterfaceI and notifies gameLogic.Players
     * that a gameLogic.Player cannot buy a property in that tile since it is owned by another gameLogic.Player
     * @param player This is the gameLogic.Player attempting to buy the property
     * @param owner This is the owner of the property
     * @param tile This is the tile where the property is situated
     */
    @Override
    public void notifyCannotBuyAlreadyOwned(Player player, Player owner, GameTileI tile) {
        System.out.println("gameLogic.Player " + player.getPlayerID() +
                " cannot buy this tile because it is already owned by gameLogic.Player " + owner.getPlayerID() + "\n" +
                tile.tileDescription());
    }

    /**Overrides function notifyCannotBuyTileBalanceReasons in gameInterface.GameInterfaceI and notifies gameLogic.Players
     * that a gameLogic.Player cannot buy a property in that tile since they have insufficient funds
     * @param player This is the gameLogic.Player attempting to buy a property
     * @param tile This is the tile the gameLogic.Player is attempting to buy
     */
    @Override
    public void notifyCannotBuyTileBalanceReasons(Player player, GameTileI tile) {
        System.out.println("gameLogic.Player " + player.getPlayerID() +
                " cannot buy this tile because they have insufficient funds.\n" +
                "gameLogic.Player Balance: $" + player.getBalance() + "\n" +
                tile.tileDescription()
        );
    }

    /**Overrides function notifyPlayerOwnsThis in gameInterface.GameInterfaceI and notifies gameLogic.Players
     * that a gameLogic.Player owns this property
     * @param owner This is the gameLogic.Player that owns the property
     */
    @Override
    public void notifyPlayerOwnsThis(Player owner) {
        System.out.println("You Own This Property!");
    }

    /**Overrides function notifyPlayerSentToJail in gameInterface.GameInterfaceI and notifies gameLogic.Players
     * that a gameLogic.Player has been sent to Jail
     * @param player This is the gameLogic.Player who has been sent to Jail
     */
    @Override
    public void notifyPlayerSentToJail(Player player) {
        System.out.println("gameLogic.Player " + player.getPlayerID() + " is sent to Jail!");
    }

    /**Overrides function notifyPlayerLeftJail in gameInterface.GameInterfaceI and notifies gameLogic.Players
     * that a gameLogic.Player has left Jail
     * @param player This is the gameLogic.Player who has left Jail
     */
    @Override
    public void notifyPlayerLeftJail(Player player) {
        System.out.println("gameLogic.Player " + player.getPlayerID() + " left Jail!");
    }

    /**Overrides function notifyPlayerStayJail in gameInterface.GameInterfaceI and notifies gameLogic.Players
     * that a gameLogic.Player stays in Jail
     * @param player This is the gameLogic.Player who stays in Jail
     */
    @Override
    public void notifyPlayerStayJail(Player player) {
        System.out.println("gameLogic.Player " + player.getPlayerID() + " stays in Jail.");
    }

    /**Overrides function notifyFreeParkingDeposit in gameInterface.GameInterfaceI and notifies gameLogic.Players
     * that a gameLogic.Player has received money from Free Parking
     * @param player This is the gameLogic.Player who receives money from Free Parking
     * @param amount This is the amount the gameLogic.Player receives
     */
    @Override
    public void notifyFreeParkingDeposit(Player player, int amount) {
        System.out.println("$" + amount + " collected through taxes have been deposited into the account of gameLogic.Player " + player.getPlayerID() + ".");
    }

    @Override
    public void notifyAuctionCannotStart(GameTileI tile) {
        System.out.println("The tile " + tile.getName() + " cannot be auctioned");
    }

    @Override
    public void notifyPlayerTaxPayment(Player player, int amount) {
        System.out.println("gameLogic.Player " + player.getPlayerID() + " must pay a tax of $" + amount + ".");
    }
}
