package gameLogic.tests;

import gameInterface.CompoundGameInterface;
import gameInterface.PlayerSelection;
import gameLogic.AIStrategy;
import gameLogic.GameBoard;

import gameLogic.Player;
import gameLogic.Players;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayersTest {

    private Players players;

    @BeforeEach
    void setup() {
        GameBoard gameBoard = new GameBoard(new CompoundGameInterface());
        players = new Players(new AIStrategy.Factory(gameBoard));
        players.createPlayers(new PlayerSelection(4, 0));
    }

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
        assertFalse(players.canEndCurrentTurn());
        players.handleCurrentPlayerFinishedRolling();
        assertTrue(players.canEndCurrentTurn());

    }

    @Test
    void nextTurn() {
        Player prev = players.getCurrentPlayer();
        players.nextTurn();
        assertNotEquals(players.getCurrentPlayer(), prev);
    }

    @Test
    void removePlayer() {
        assertEquals(players.getPlayersList().size(), 4);
        players.removePlayer(players.getCurrentPlayer());
        assertEquals(players.getPlayersList().size(), 3);
    }

    @Test
    void handleCurrentPlayerActed() {
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