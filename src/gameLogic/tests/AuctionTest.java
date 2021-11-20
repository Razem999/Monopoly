import gameInterface.CompoundGameInterface;
import gameLogic.Auction;
import gameLogic.Player;
import gameLogic.Players;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuctionTest {

    CompoundGameInterface gameInterface = new CompoundGameInterface();

    Players players = new Players(gameInterface);
    Player currentPlayer = players.getPlayerByID(1).get();

    @Test
    void bet() {
        Auction auction = new Auction(players.getPlayersList(), currentPlayer, 100);
        auction.bet(110);
        auction.bet(120);
        auction.withdrawCurrentPlayerFromAuction();
        auction.withdrawCurrentPlayerFromAuction();
        auction.withdrawCurrentPlayerFromAuction();
        assertEquals(auction.getPrice(), 120);
    }

    @Test
    void withdrawCurrentPlayerFromAuction() {
        Auction auction = new Auction(players.getPlayersList(), currentPlayer, 100);
        auction.bet(120);
        auction.withdrawCurrentPlayerFromAuction();
        auction.withdrawCurrentPlayerFromAuction();
        auction.withdrawCurrentPlayerFromAuction();
        assertTrue(auction.shouldEnd());
    }

    @Test
    void getPrice() {
        Auction auction = new Auction(players.getPlayersList(), currentPlayer, 100);
        auction.bet(120);
        assertEquals(auction.getPrice(), 120);
        auction.bet(150);
        assertEquals(auction.getPrice(), 150);
        auction.bet(10);
        assertEquals(auction.getPrice(), 150);
        auction.bet(-10);
        assertEquals(auction.getPrice(), 150);
    }

    @Test
    void getHighestBidder() {
        Auction auction = new Auction(players.getPlayersList(), currentPlayer, 100);
        Player highestBidder = auction.getCurrentBidder();
        auction.bet(120);
        assertEquals(auction.getHighestBidder(), highestBidder);
        auction.bet(100);
        assertEquals(auction.getHighestBidder(), highestBidder);
        auction.bet(130);
        assertNotEquals(auction.getHighestBidder(), highestBidder);
        highestBidder = auction.getCurrentBidder();
        auction.bet(140);
        assertEquals(auction.getHighestBidder(), highestBidder);
    }

    @Test
    void getCurrentBidder() {
        Auction auction = new Auction(players.getPlayersList(), currentPlayer, 100);
        assertEquals(auction.getCurrentBidder(), currentPlayer);
        auction.bet(120);
        assertNotEquals(auction.getCurrentBidder(), currentPlayer);

    }

    /**
     * All Players start off with $1500.
     * The tests below are performed with this amount in mind.
     */
    @Test
    void getCurrentBidderBalance() {
        Auction auction = new Auction(players.getPlayersList(), currentPlayer, 100);
        assertEquals(auction.getCurrentBidderBalance(), 1500);
        auction.bet(500);
        assertEquals(auction.getCurrentBidderBalance(), 1500);
    }

    @Test
    void shouldEnd() {
        Auction auction = new Auction(players.getPlayersList(), currentPlayer, 100);
        auction.bet(130);
        assertFalse(auction.shouldEnd());
        auction.withdrawCurrentPlayerFromAuction();
        assertFalse(auction.shouldEnd());
        auction.withdrawCurrentPlayerFromAuction();
        assertFalse(auction.shouldEnd());
        auction.withdrawCurrentPlayerFromAuction();
        assertTrue(auction.shouldEnd());
    }
}