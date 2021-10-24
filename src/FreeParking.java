public class FreeParking implements GameTileI{
    public static int totalDeposited;

    private GameInterfaceI gameInterface;

    FreeParking(GameInterfaceI gameInterface) {
        this.gameInterface = gameInterface;
    }

    public void addTax(int amount) {
        totalDeposited += amount;
    }

    @Override
    public void onLand(Player player, GameBoard gameBoard, Players players) {
        player.changeBalance(totalDeposited);
        gameInterface.notifyFreeParkingDeposit(player, totalDeposited);
        totalDeposited = 0;
    }

    @Override
    public String tileDescription() {
        return "Name: GO Tile\nDescription: Gain $200 when passing through.";
    }

    @Override
    public boolean tryBuy(Player player) {
        gameInterface.notifyCannotBuyTileKind(player, this);
        return false;
    }

    @Override
    public String getName() {
        return "Free Parking";
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
