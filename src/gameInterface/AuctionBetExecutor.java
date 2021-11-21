package gameInterface;

import gameLogic.AIStrategy;
import gameLogic.Auction;
import gameLogic.Player;
import gameLogic.Players;

import java.util.Optional;

public interface AuctionBetExecutor {
    class Factory {
        private Optional<GameInterface> gameInterface;
        private Optional<AIStrategy.Factory> aiFactory;

        public Factory() {
            this.gameInterface = Optional.empty();
            this.aiFactory = Optional.empty();
        }

        public void setGameInterface(GameInterface gameInterface) {
            this.gameInterface = Optional.of(gameInterface);
        }

        public void setAiFactory(AIStrategy.Factory aiFactory) {
            this.aiFactory = Optional.of(aiFactory);
        }

        public Optional<AuctionBetExecutor> getAuctionBetExecutor(Player player) {
            if (player.getAIStrategy().isPresent()) {
                return this.aiFactory.map(f -> f.getAIStrategy(player.getAIStrategy().get()));
            } else {
                /* Java doesn't have the greatest support for variance on its subtypes so it does not recognize that
                   when we have an Optional<T> where T is a subtype of F, the Optional<T> is also a subtype of Optional<F>.
                   in other languages with stronger type systems this would be called a covariance relationship
                   on the type constructor Optional. Here, we do a identity mapping to kick java in gear
                 */
                return gameInterface.map(g -> g);
            }
        }
    }

    Auction.BidAdvanceToken doPlayerBid(Auction auction, Players players, int tilePosition);
}
