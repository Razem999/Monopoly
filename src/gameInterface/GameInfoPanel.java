package gameInterface;

import gameLogic.GameActions;
import gameLogic.GameBoard;
import gameLogic.Players;

import javax.swing.*;

public class GameInfoPanel extends JComponent {

    private GameTextBox gameTextBox;

    public GameInfoPanel(GameActions gameActions, AuctionBidExecutor.Factory auctionBetExecutorFactory, Players players, GameBoard gameBoard) {
        super();

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.gameTextBox = new GameTextBox(auctionBetExecutorFactory);
        this.add(this.gameTextBox);

        this.add(new GameButtonPanel(gameActions, gameBoard, players));
    }

    public GameInterface getGameInterface() {
        return this.gameTextBox;
    }

}
