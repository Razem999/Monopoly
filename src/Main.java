import gameInterface.*;
import gameLogic.GameActions;
import gameLogic.GameBoard;
import gameLogic.Players;

import javax.swing.*;
import java.awt.*;

public class Main {
    private static final Dimension windowSize = new Dimension(1500, 1000);

    public static void main(String[] args) {
        JFrame mainFrame = new JFrame("Monopoly: The Devil Wants His Money!");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(windowSize);
        Container contentPane = mainFrame.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        CompoundGameInterface gameInterface = new CompoundGameInterface();

        GameBoard gameBoard = new GameBoard(gameInterface);
        Players players = new Players(gameInterface);

        GameActions gameActions = new GameActions(gameBoard, players, gameInterface);

        contentPane.add(new GameCanvas(gameBoard));

        GameInfoPanel gameInfoPanel = new GameInfoPanel(gameActions);
        gameInterface.connectGameInterface(gameInfoPanel.getGameInterface());
        contentPane.add(gameInfoPanel);

        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

}
