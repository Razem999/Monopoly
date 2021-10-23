public class Jail implements GameTileI{



    @Override
    public void onLand(Player player, GameBoard gameBoard, Players players) {
        //do nothing
    }

    @Override
    public String tileDescription() {
        return "Name: Jail\n" +
                "Description: Just Visiting Jail.";
    }

    @Override
    public boolean tryBuy(Player player) {
        return false;
    }

    @Override
    public String getName() {
        return "Just Visiting Jail";
    }

    @Override
    public boolean isOwnedBy(Player player) {
        return false;
    }

    @Override
    public boolean tryTransferOwnership(Player newOwner) {
        return false;
    }
}
