import java.util.Locale;
import java.util.Scanner;

public class TextGameInterface implements GameInterfaceI {
    Scanner scanner;

    TextGameInterface() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void startAuction(int tileNum, int startingBid) {
        System.out.println("An auction is starting for " + tileNum + " for $" + startingBid);
    }

    @Override
    public boolean processSale(String tileName, int amount, Player buyer) {
        System.out.println("The property " + tileName +
                " sells for $" + amount + " are you sure you want to buy it Player " + buyer.getPlayerID() + "?\n" +
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
    public void notifyPlayerDeclinedPurchase(Player player, String tileName) {
        System.out.println("Player " + player.getPlayerID() + " declined to purchase " + tileName);
    }

    @Override
    public void notifyPlayerPurchaseConfirm(Player player, String tileName, int amount) {
        System.out.println("Player " + player.getPlayerID() + " bought " + tileName +
                "for $" + amount);
    }

    @Override
    public void notifyRentPayment(Player owner, Player payer, int amount) {
        System.out.println("Player " + payer.getPlayerID() + " pays $" + amount + " in rent to Player " + owner.getPlayerID());
    }

    @Override
    public void notifyBankruptcy(Player player) {
        System.out.println("Player " + player.getPlayerID() + " has gone bankrupt!");
    }

    @Override
    public void notifyRoll(Player player, int firstRoll, int secondRoll) {
        int rollTotal = firstRoll + secondRoll;
        if (firstRoll == secondRoll) {
            System.out.println("Player " + player.getPlayerID() + " got a double roll of " +
                    rollTotal + "! (" + firstRoll + ", " + secondRoll + ")");
        } else {
            System.out.println("Player " + player.getPlayerID() + " rolled " +
                    rollTotal + " (" + firstRoll + ", " + secondRoll + ")");
        }
    }

    @Override
    public void notifyPassGo(Player player) {
        System.out.println("Player " + player.getPlayerID() + " has passed go and gained $200");
    }

    @Override
    public void notifyPlayerMovement(Player player, int tilesMoved, int newPosition, String destinationDescription) {
        System.out.println("Player " + player.getPlayerID() + " has moved " +
                tilesMoved + " tiles onto tile number " + newPosition + ":\n" + destinationDescription);
    }

    @Override
    public void notifyCannotRoll(Player player) {
        System.out.println("You already rolled!");
    }

    @Override
    public void notifyCannotBuyTileKind(Player player, GameTileI tile) {
        System.out.println("Player " + player.getPlayerID() +
                        " cannot buy this tile because it is not a buyable tile.\n" +
                        tile.tileDescription()
                );
    }

    @Override
    public void notifyCannotBuyAlreadyOwned(Player player, Player owner, GameTileI tile) {
        System.out.println("Player " + player.getPlayerID() +
                " cannot buy this tile because it is already owned by Player " + owner.getPlayerID() + "\n" +
                tile.tileDescription());
    }

    @Override
    public void notifyCannotBuyTileBalanceReasons(Player player, GameTileI tile) {
        System.out.println("Player " + player.getPlayerID() +
                " cannot buy this tile because they have insufficient funds.\n" +
                "Player Balance: $" + player.getBalance() + "\n" +
                tile.tileDescription()
        );
    }

    @Override
    public void notifyPlayerOwnsThis(Player owner) {
        System.out.println("You Own This Property!");
    }

    @Override
    public void notifyPlayerSentToJail(Player player) {
        System.out.println("Player " + player.getPlayerID() + " is sent to Jail!");
    }

    @Override
    public void notifyPlayerLeftJail(Player player) {
        System.out.println("Player " + player.getPlayerID() + " left Jail!");
    }

    @Override
    public void notifyPlayerStayJail(Player player) {
        System.out.println("Player " + player.getPlayerID() + " stays in Jail.");
    }

    @Override
    public void notifyFreeParkingDeposit(Player player, int amount) {
        System.out.println("$" + amount + " collected through taxes have been deposited into the account of Player " + player.getPlayerID() + ".");
    }

}
