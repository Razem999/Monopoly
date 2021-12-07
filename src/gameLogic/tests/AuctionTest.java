package gameLogic.tests;

import gameInterface.CompoundGameInterface;
import gameInterface.PlayerSelection;
import gameLogic.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuctionTest {
    private Players players;
    private Player currentPlayer;

    @BeforeEach
    void setup() {
        GameBoard gameBoard = new GameBoard(new CompoundGameInterface());
        players = new Players(new AIStrategy.Factory(gameBoard));
        currentPlayer = players.getPlayerByID(1).get();
    }

    @Test
    void bet() {
        players.createPlayers(new PlayerSelection(4, 0));
        Auction auction = new Auction(players.getPlayersList(), currentPlayer);
        auction.bid(110);
        auction.bid(120);
        auction.withdrawCurrentPlayerFromAuction();
        auction.withdrawCurrentPlayerFromAuction();
        auction.withdrawCurrentPlayerFromAuction();
        assertEquals(auction.getPrice(), 120);
    }

    @Test
    void withdrawCurrentPlayerFromAuction() {
        players.createPlayers(new PlayerSelection(4, 0));
        Auction auction = new Auction(players.getPlayersList(), currentPlayer);
        auction.bid(120);
        auction.withdrawCurrentPlayerFromAuction();
        auction.withdrawCurrentPlayerFromAuction();
        auction.withdrawCurrentPlayerFromAuction();
        assertTrue(auction.shouldEnd());
    }

    @Test
    void getPrice() {
        players.createPlayers(new PlayerSelection(4, 0));
        Auction auction = new Auction(players.getPlayersList(), currentPlayer);
        auction.bid(120);
        assertEquals(auction.getPrice(), 120);
        auction.bid(150);
        assertEquals(auction.getPrice(), 150);
        auction.bid(10);
        assertEquals(auction.getPrice(), 150);
        auction.bid(-10);
        assertEquals(auction.getPrice(), 150);
    }

    @Test
    void getHighestBidder() {
        players.createPlayers(new PlayerSelection(4, 0));
        Auction auction = new Auction(players.getPlayersList(), currentPlayer);
        Player highestBidder = auction.getCurrentBidder();
        auction.bid(120);
        assertEquals(auction.getHighestBidder(), highestBidder);
        auction.bid(100);
        assertEquals(auction.getHighestBidder(), highestBidder);
        auction.bid(130);
        assertNotEquals(auction.getHighestBidder(), highestBidder);
        highestBidder = auction.getCurrentBidder();
        auction.bid(140);
        assertEquals(auction.getHighestBidder(), highestBidder);
    }

    @Test
    void getCurrentBidder() {
        players.createPlayers(new PlayerSelection(4, 0));
        Auction auction = new Auction(players.getPlayersList(), currentPlayer);
        assertEquals(auction.getCurrentBidder(), currentPlayer);
        auction.bid(120);
        assertNotEquals(auction.getCurrentBidder(), currentPlayer);

    }

    /**
     * All Players start off with $1500.
     * The tests below are performed with this amount in mind.
     */
    @Test
    void getCurrentBidderBalance() {
        players.createPlayers(new PlayerSelection(4, 0));
        Auction auction = new Auction(players.getPlayersList(), currentPlayer);
        assertEquals(auction.getCurrentBidderBalance(), 1500);
        auction.bid(500);
        assertEquals(auction.getCurrentBidderBalance(), 1500);
    }

    @Test
    void shouldEnd() {
        players.createPlayers(new PlayerSelection(4, 0));
        Auction auction = new Auction(players.getPlayersList(), currentPlayer);
        auction.bid(130);
        assertFalse(auction.shouldEnd());
        auction.withdrawCurrentPlayerFromAuction();
        assertFalse(auction.shouldEnd());
        auction.withdrawCurrentPlayerFromAuction();
        assertFalse(auction.shouldEnd());
        auction.withdrawCurrentPlayerFromAuction();
        assertTrue(auction.shouldEnd());
    }
}