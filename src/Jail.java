import java.util.ArrayList;
import java.util.List;

/**
 * The JailedPlayerInfo class keeps track of the players in jail and their roll states.
 *
 * @version 1.0
 * @since 2021-10-25
 */
class JailedPlayerInfo {
    private final Player player;
    private int rollsInJail;

    JailedPlayerInfo(Player player) {
        this.player = player;
        this.rollsInJail = 0;
    }

    public int getRollsInJail() {
        return this.rollsInJail;
    }

    public int getMaxRollsInJail() {
        return 3;
    }

    public boolean hasPlayerMaxedRolls() {
        return this.rollsInJail == this.getMaxRollsInJail();
    }

    public Player getPlayer() {
        return player;
    }

    public void incrementTimesRolled() {
        this.rollsInJail++;
    }
}

/**
 * The Jail class represents the Jail/Just Visiting tile from the original
 * game. When a Player lands on Go To Jail tile, or rolls doubles three times,
 * Then the Player will move their position to this tile and movements will be
 * restricted once they are inside jail (not Just Visiting).
 *
 * @version 1.0
 * @since 2021-10-25
 */
public class Jail implements GameTileI {

    public static int jailFine = 50;

    private final List<JailedPlayerInfo> jailedPlayers;

    /**This is the constructor of Jail, initially, there are no players in jail
     */
    public Jail() {
        this.jailedPlayers = new ArrayList<>();
    }

    @Override
    public void onLand(Player player, GameBoard gameBoard, Players players) {
        //do nothing
    }

    @Override
    public String tileDescription() {
        StringBuilder jailStringBuilder = new StringBuilder();
        jailStringBuilder.append("Name: Jail\nDescription: In Jail:\n");

        for (JailedPlayerInfo player : this.jailedPlayers) {
            jailStringBuilder.append("Player: ");
            jailStringBuilder.append(player.getPlayer().getPlayerID());
            jailStringBuilder.append(" (");
            jailStringBuilder.append(player.getRollsInJail());
            jailStringBuilder.append("/");
            jailStringBuilder.append(player.getMaxRollsInJail());
            jailStringBuilder.append(")");
            jailStringBuilder.append("\n");
        }

        return jailStringBuilder.toString();
    }

    @Override
    public boolean tryBuy(Player player) {
        return false;
    }

    @Override
    public String getName() {
        return "Jail";
    }

    @Override
    public boolean isOwnedBy(Player player) {
        return false;
    }

    @Override
    public boolean tryTransferOwnership(Player newOwner) {
        return false;
    }

    @Override
    public boolean tryCloseAuctionFor(int price, Player player) {
        return false;
    }

    @Override
    public boolean isAuctionable() {
        return false;
    }

    /** This function jails a player
     *
     * @param player The player being jailed
     */
    public void jailPlayer(Player player) {
        this.jailedPlayers.add(new JailedPlayerInfo(player));
    }

    /** This function increments the roll count of a player in jail
     *
     * @param player The player in jail who is rolling
     */
    public void incrementPlayerRolls(Player player) {
        int index = this.findPlayer(player);
        if (index != -1) {
            JailedPlayerInfo playerInfo = this.jailedPlayers.get(index);
            playerInfo.incrementTimesRolled();
        }
    }

    /** This function finds a player within the jail info list
     *
     * @param player The player
     */
    private int findPlayer(Player player) {
        for (int i = 0; i < this.jailedPlayers.size(); i++) {
            if (this.jailedPlayers.get(i).getPlayer().equals(player)) {
                return i;
            }
        }

        return -1;
    }

    /** This function determines whether a player has made enough roll attempts to automatically get out of jail
     *
     * @param player The player in jail who is rolling
     */
    public boolean hasPlayerRolledOutOfJail(Player player) {
        int index = this.findPlayer(player);
        if (index != -1) {
            JailedPlayerInfo playerInfo = this.jailedPlayers.get(index);
            return playerInfo.hasPlayerMaxedRolls();
        }

        return false;
    }

    /** This function unjails a player currently in jail
     *
     * @param player The player in jail
     */
    public void unjailPlayer(Player player) {
        int index = this.findPlayer(player);
        if (index != -1) {
            this.jailedPlayers.remove(index);
        }
    }

    /** This function returns whether a player is currently in jail or not
     *
     * @param player The player in question
     */
    public boolean isPlayerJailed(Player player) {
        return this.findPlayer(player) != -1;
    }
}
