public class IncomeTaxTile implements GameTileI{
    public static int baseCost = -200;

    private FreeParking freeParking;
    private GameInterfaceI gameInterface;

    IncomeTaxTile(GameInterfaceI gameInterface, FreeParking freeParking) {
        this.freeParking = freeParking;
        this.gameInterface = gameInterface;
    }

    @Override
    public void onLand(Player player, GameBoard gameBoard, Players players) {
        player.changeBalance(baseCost);
        freeParking.addTax(baseCost);
    }

    @Override
    public String tileDescription() {
        return "Name: Income Tax\nDescription: Pay $200";
    }

    @Override
    public boolean tryBuy(Player player) {
        gameInterface.notifyCannotBuyTileKind(player, this);
        return false;
    }

    @Override
    public String getName() {
        return "Income Tax";
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
