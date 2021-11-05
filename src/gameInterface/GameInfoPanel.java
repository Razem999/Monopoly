package gameInterface;

import gameLogic.GameActions;

import javax.swing.*;

public class GameInfoPanel extends JComponent {

    private GameTextBox gameTextBox;

    public GameInfoPanel(GameActions gameActions) {
        super();

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.gameTextBox = new GameTextBox();
        this.add(this.gameTextBox);

        this.add(new GameButtonPanel(gameActions));
    }

    public GameInterfaceI getGameInterface() {
        return this.gameTextBox;
    }

}
