package gameLogic;

import gameInterface.CompoundGameInterface;
import gameInterface.GameInterfaceI;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import tiles.*;

import static org.junit.jupiter.api.Assertions.*;

class GameBoardTest {

    CompoundGameInterface gameInterface = new CompoundGameInterface();
    GameBoard gb = new GameBoard(gameInterface);

    GameTileI gameTileI;
    BuyableI buyableTile;

    Players players = new Players(gameInterface);


    PropertyTile brownProperty = new PropertyTile("Mediterranean Avenue", PropertySet.Purple, gameInterface, 60, 50, 2, 10, 30, 90, 160, 250);
    GoTile goTile = new GoTile(gameInterface);

//    PropertyTileBuilder propertyTileBuilder = new PropertyTileBuilder("Mediterranean Avenue", PropertySet.Purple, gameInterface).setCost(60).setPricePerHouse(50).setRent(2, 10, 30, 90, 160, 250).getPropertyTile());


//    @Before
//    public void SetUp() {
//        p1.setTilePosition(1);
//        brownProperty.buy(p1);
//    }

//    @Test
//    void getTilesOwnedByPlayer() {
//        assertTrue(brownProperty.isOwnedBy(p1));
//    }

    @Test
    void advancePlayer() {
        gb.advancePlayer(players.getPlayerByID(0).get(), 1, players);
        assertEquals(players.getPlayerByID(0).get().getTilePosition(), 1);

        gb.advancePlayer(players.getPlayerByID(1).get(), 9, players);
        assertEquals(players.getPlayerByID(1).get().getTilePosition(), 9);

        gb.advancePlayer(players.getPlayerByID(0).get(), 10, players);
        assertEquals(players.getPlayerByID(0).get().getTilePosition(), 11);
    }

    @Test
    void getTile() {
        assertEquals(gb.getTile(0).get().getName(), "GO Tile");
        assertEquals(gb.getTile(1).get().getName(), "Mediterranean Avenue");
        assertEquals(gb.getTile(2).get().getName(), "Empty Tile");
        assertEquals(gb.getTile(4).get().getName(), "Income Tax");
        assertEquals(gb.getTile(5).get().getName(), "Reading Railroad");
        assertEquals(gb.getTile(10).get().getName(), "Jail");
        assertEquals(gb.getTile(12).get().getName(), "Electric Company");
        assertEquals(gb.getTile(12).get().getName(), "Go To Jail");
    }

    @Test
    void getTileDescriptionByIndex() {
        String temp = String.valueOf(gb.getTileDescriptionByIndex(0));
        assertEquals(temp, "Optional[Tile 0" + "\n" + goTile.tileDescription() + "]");
    }

    @Test
    void transferPlayerProperties() {
        gb.transferPlayerProperties(players.getPlayerByID(0).get(), players.getPlayerByID(1).get());
        gb.getTilesOwnedByPlayer(players.getPlayerByID(1).get());
    }

//    @Test
//    void getPropertiesFilter() {
//    }

//    @Test
//    void payJailFine() {
//    }

    @Test
    void jailPlayer() {
        gb.jailPlayer(players.getPlayerByID(0).get());
        assertEquals(players.getPlayerByID(0).get().getTilePosition(), 10);
    }

    @Test
    void isPlayerInJail() {
        gb.jailPlayer(players.getPlayerByID(0).get());
        assertTrue(gb.isPlayerInJail(players.getPlayerByID(0).get()));
        assertFalse(gb.isPlayerInJail(players.getPlayerByID(1).get()));
    }

//    @Test
//    void handleFailedJailedPlayerRoll() {
//    }
//
//    @Test
//    void handleSuccessfulJailedPlayerRoll() {
//    }
//
//    @Test
//    void getTileDrawables() {
//    }
}