package gameLogic;

import gameInterface.CompoundGameInterface;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuctionTest {

    CompoundGameInterface gameInterface = new CompoundGameInterface();
    GameBoard gb = new GameBoard(gameInterface);

    Players players = new Players(gameInterface);

    Auction auction = new Auction()

    @Test
    void bet() {
    }

    @Test
    void withdrawCurrentPlayerFromAuction() {

    }

    @Test
    void getPrice() {
    }

    @Test
    void getHighestBidder() {
    }

    @Test
    void getCurrentBidder() {
    }

    @Test
    void getCurrentBidderBalance() {
    }

    @Test
    void shouldEnd() {
    }
}