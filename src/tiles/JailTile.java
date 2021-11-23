package tiles;

import gameLogic.Player;
import gameLogic.Players;
import gameLogic.GameBoard;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The tiles.JailedPlayerInfo class keeps track of the players in jail and their roll states.
 *
 * @version 1.0
 * @since 2021-10-25
 */
class JailedPlayerInfo {
    private final Player player;
    private int rollsInJail;

    /**
     * This is the constructor for tiles.JailedPlayerInfo with parameters
     * @param player This is the player who could be in Jail
     */
    JailedPlayerInfo(Player player) {
        this.player = player;
        this.rollsInJail = 0;
    }

    /**
     * This method is used to get the number of attempts of rolls to escape jail
     * @return This returns the number of attempts
     */
    public int getRollsInJail() {
        return this.rollsInJail;
    }

    /**
     * This method is used to get the maximum number of rolls a player is allowed
     * @return This returns the maximum rolls possible
     */
    public int getMaxRollsInJail() {
        return 3;
    }

    /**
     * This method is used to check if a player reached the maximum number of rolls in jail
     * @return This returns true or false depending on whether the player has maximum number of times or not
     */
    public boolean hasPlayerMaxedRolls() {
        return this.rollsInJail == this.getMaxRollsInJail();
    }

    /**
     * This method is used to get the player in jail
     * @return This returns the player in jail
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * This method is used to increment the number of attempts in escaping jail
     */
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
public class JailTile implements GameTile {

    public static int JAIL_FINE = 50;

    private final List<JailedPlayerInfo> jailedPlayers;

    /**This is the constructor of Jail, initially, there are no players in jail
     */
    public JailTile() {
        this.jailedPlayers = new ArrayList<>();
    }

    /**
     * This method does nothing, meaning any player that rolls and lands on this tile, then they are considered
     * to be just visiting Jail. They are not in Jail.
     * @param player This is the player that landed on the tile
     * @param gameBoard This is the board where the tile is situated
     * @param players This is the list of players playing the game
     */
    @Override
    public void onLand(Player player, GameBoard gameBoard, Players players) {
        //do nothing
    }

    /**
     * This method prints the tile's description
     * @return This returns the description as a string
     */
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

    /**
     * This method is used to get the name of the tile
     * @return This returns the name as a string
     */
    @Override
    public String getName() {
        return "Jail";
    }

    /**
     * This method is used to get the set color this property represents
     * @return This returns the PropertySet
     */
    @Override
    public PropertySet getPropertySet() {
        return PropertySet.White;
    }

    /**
     * This method is used to get the property in this tile
     * @return This returns null since this is not buyable
     */
    @Override
    public PropertyTile getPropertyTile() {
        return null;
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

    /**
     * This method returns an empty Optional since this is an empty tile
     * @return an empty Optional
     */
    @Override
    public Optional<BuyableTile> asBuyable() {
        return Optional.empty();
    }

    /**
     * This method returns an empty Optional since this is an empty tile
     * @return an empty Optional
     */
    @Override
    public Optional<HousingTile> asHousingTile() {
        return Optional.empty();
    }
}
