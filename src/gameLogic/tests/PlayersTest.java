import gameInterface.CompoundGameInterface;
import gameLogic.Player;
import gameLogic.Players;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayersTest {

    CompoundGameInterface gameInterfaceI = new CompoundGameInterface();

    @Test
    void getPlayersList() {
        /**
         * When the players object is created, each player rolls the dice and are
         * ordered in an Optional list based on the number they rolled. So the players
         * list is arranged in random, which cannot be tested.
         */
    }

    @Test
    void getPlayerByID() {
        Players players = new Players(gameInterfaceI);
        assertEquals(players.getPlayerByID(0).get().getPlayerID(), 0);
        assertEquals(players.getPlayerByID(1).get().getPlayerID(), 1);
        assertEquals(players.getPlayerByID(2).get().getPlayerID(), 2);
        assertEquals(players.getPlayerByID(3).get().getPlayerID(), 3);
    }

    @Test
    void getCurrentPlayer() {
        /**
         * Same as before, any of the players could start the game, so the order is
         * fixed after the players object is created.
         */
    }

    @Test
    void canEndCurrentTurn() {
        Players players = new Players(gameInterfaceI);
        assertFalse(players.canEndCurrentTurn());
        players.handleCurrentPlayerFinishedRolling();
        assertTrue(players.canEndCurrentTurn());

    }

    @Test
    void nextTurn() {
        Players players = new Players(gameInterfaceI);
        Player prev = players.getCurrentPlayer();
        players.nextTurn();
        assertNotEquals(players.getCurrentPlayer(), prev);
    }

    @Test
    void removePlayer() {
        Players players = new Players(gameInterfaceI);
        assertEquals(players.getPlayersList().size(), 4);
        players.removePlayer(players.getCurrentPlayer());
        assertEquals(players.getPlayersList().size(), 3);
    }

    @Test
    void handleCurrentPlayerActed() {
        Players players = new Players(gameInterfaceI);
        assertFalse(players.hasCurrentPlayerActed());
        players.handleCurrentPlayerActed();
        assertTrue(players.hasCurrentPlayerActed());
    }

    @Test
    void hasCurrentPlayerActed() {
        /**
         * Tested in test case handleCurrentPlayerActed.
         */
    }

    @Test
    void handleCurrentPlayerFinishedRolling() {
        Players players = new Players(gameInterfaceI);
        assertFalse(players.hasCurrentPlayerFinishedRolling());
        players.handleCurrentPlayerFinishedRolling();
        assertTrue(players.hasCurrentPlayerFinishedRolling());
    }

    @Test
    void hasCurrentPlayerFinishedRolling() {
        /**
         * Tested in test case handleCurrentPlayerFinishedRolling.
         */
    }
}