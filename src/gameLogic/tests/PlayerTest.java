package gameLogic.tests;

import gameLogic.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    Player p1 = new Player(0, 1500);
    Player p2 = new Player(1, 750);
    Player p3 = new Player(2, 500);
    Player p4 = new Player(3);
    int p5 = 4;

    @Test
    void getTilePosition() {
        assertEquals(p1.getTilePosition(), 0);
        assertEquals(p2.getTilePosition(), 0);
        assertEquals(p3.getTilePosition(), 0);
        assertEquals(p4.getTilePosition(), 0);

    }

    @Test
    void setTilePosition() {
        p1.setTilePosition(5);
        assertEquals(p1.getTilePosition(), 5);

        p2.setTilePosition(10);
        assertEquals(p2.getTilePosition(), 10);

        p3.setTilePosition(20);
        assertEquals(p3.getTilePosition(), 20);

        p4.setTilePosition(2);
        assertEquals(p4.getTilePosition(), 2);
    }

    @Test
    void getBalance() {
        assertEquals(p1.getBalance(), 1500);
        assertEquals(p2.getBalance(), 750);
        assertEquals(p3.getBalance(), 500);
        assertEquals(p4.getBalance(), 0);
    }

    @Test
    void getPlayerID() {
        assertEquals(p1.getPlayerID(), 0);
        assertEquals(p2.getPlayerID(), 1);
        assertEquals(p3.getPlayerID(), 2);
        assertEquals(p4.getPlayerID(), 3);
    }

    @Test
    void hasID() {
        assertTrue(p1.hasID(0));
        assertTrue(p2.hasID(1));

        assertFalse(p3.hasID(3));

        assertTrue(p4.hasID(3));
    }

    @Test
    void testEquals() {
        assertTrue(p1.equals(p1));
        assertTrue(p2.equals(p2));
        assertTrue(p3.equals(p3));
        assertTrue(p4.equals(p4));

        assertFalse(p4.equals(p5));
    }

    @Test
    void changeBalance() {
        p1.changeBalance(200);
        p2.changeBalance(-200);
        p3.changeBalance(-500);

        assertEquals(p1.getBalance(), 1700);
        assertEquals(p2.getBalance(), 550);
        assertEquals(p3.getBalance(), 0);
    }
}