import gameInterface.CompoundGameInterface;
import gameLogic.AIStrategy;
import gameLogic.GameBoard;
import gameLogic.Players;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import tiles.*;

import static org.junit.jupiter.api.Assertions.*;

class GameBoardTest {
    private GameBoard gb;
    private Players players;
    private GoTile goTile;


    @BeforeEach
    void setup() {
        CompoundGameInterface gameInterface = new CompoundGameInterface();
        gb = new GameBoard(gameInterface);
        players = new Players(new AIStrategy.Factory(gb));
        goTile = new GoTile(gameInterface);
    }

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

}