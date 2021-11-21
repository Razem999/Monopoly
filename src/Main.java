import gameInterface.*;
import gameLogic.AIStrategy;
import gameLogic.GameActions;
import gameLogic.GameBoard;
import gameLogic.Players;

import javax.swing.*;
import java.awt.*;

public class Main {
    private static final Dimension windowSize = Toolkit.getDefaultToolkit().getScreenSize();

    public static void main(String[] args) {
        JFrame mainFrame = new JFrame("Monopoly: The Devil Wants His Money!");

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(windowSize);
        Container contentPane = mainFrame.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        CompoundGameInterface gameInterface = new CompoundGameInterface();

        GameBoard gameBoard = new GameBoard(gameInterface);

        AIStrategy.Factory aiFactory = new AIStrategy.Factory(gameBoard);
        Players players = new Players(aiFactory);

        GameActions gameActions = new GameActions(gameBoard, players, gameInterface);
        players.setAIActionHandler(gameActions);

        GameCanvas gameCanvas = new GameCanvas(gameBoard, players);
        contentPane.add(gameCanvas);

        contentPane.setFocusable(true);
        contentPane.addKeyListener(gameCanvas.getGameCameraController());

        AuctionBetExecutor.Factory auctionBetExecutorFactory = new AuctionBetExecutor.Factory();
        auctionBetExecutorFactory.setAiFactory(aiFactory);
        GameInfoPanel gameInfoPanel = new GameInfoPanel(gameActions, auctionBetExecutorFactory);
        gameInterface.connectGameInterface(gameInfoPanel.getGameInterface());
        contentPane.add(gameInfoPanel);

        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);

        PlayerSelection playerSelection = gameInterface.askHowManyPlayers();
        players.createPlayers(playerSelection);

        gameCanvas.requestRedraw();
    }

}
