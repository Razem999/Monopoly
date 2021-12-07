import gameInterface.*;
import gameLogic.AIStrategy;
import gameLogic.GameActions;
import gameLogic.GameBoard;
import gameLogic.Players;
import save.GameBoardSave;
import save.GameStateSave;
import save.SaveCreator;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.util.Optional;

public class Main {
    private static final Dimension windowSize = Toolkit.getDefaultToolkit().getScreenSize();

    private static void startGameDefault(JFrame mainFrame, SaveCreator saveCreator) {
        Container contentPane = mainFrame.getContentPane();

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

        AuctionBidExecutor.Factory auctionBetExecutorFactory = new AuctionBidExecutor.Factory();
        auctionBetExecutorFactory.setAiFactory(aiFactory);
        GameInfoPanel gameInfoPanel = new GameInfoPanel(gameActions, auctionBetExecutorFactory, players, gameBoard, saveCreator);
        gameInterface.connectGameInterface(gameInfoPanel.getGameInterface());
        contentPane.add(gameInfoPanel);

        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);

        PlayerSelection playerSelection = gameInterface.askHowManyPlayers();
        players.createPlayers(playerSelection);

        saveCreator.setGameBoard(gameBoard);
        saveCreator.setPlayers(players);

        gameCanvas.requestRedraw();
    }

    private enum GameStartOption {
        Load,
        New,
    }

    private static GameStartOption getGameStartChoice() {
        Object[] options = { "Load Game", "New Game" };
        int choice = JOptionPane.showOptionDialog(null, "Would you like to load a save or start a new game?", "Main Menul", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);

        return switch (choice) {
            case 0 -> GameStartOption.Load;
            case 1 -> GameStartOption.New;
            default -> GameStartOption.New;
        };
    }

    private static Optional<GameStateSave> loadGameState(File file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            GameStateSave gameStateSave = (GameStateSave) objectInputStream.readObject();

            objectInputStream.close();
            fileInputStream.close();

            return Optional.of(gameStateSave);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    private static void loadGame(JFrame mainFrame, SaveCreator saveCreator, GameStateSave gameStateSave) {
        Container contentPane = mainFrame.getContentPane();

        CompoundGameInterface gameInterface = new CompoundGameInterface();

        AIStrategy.Factory aiFactory = new AIStrategy.Factory();
        Players players = gameStateSave.getPlayersSave().getPlayers(aiFactory);

        GameBoardSave gameBoardSave = gameStateSave.getGameBoardSave();

        GameBoard gameBoard = new GameBoard(gameInterface, gameStateSave.getPropertyTileSavess(), gameBoardSave.getPlaceableHouses(), gameBoardSave.getPlaceableHotels(), players);

        gameBoard.applyJailSave(gameStateSave.getJailSave(), players);
        gameBoard.applyElectricCompanySave(gameStateSave.getElectricCompanyOwner(), players);
        gameBoard.applyWaterworksSave(gameStateSave.getWaterworksOwner(), players);
        gameBoard.applyRailroadSaves(gameStateSave.getRailroadSaves(), players);
        gameBoard.setFreeParkingDeposits(gameStateSave.getFreeParkingDeposits());

        aiFactory.setGameBoard(gameBoard);

        GameActions gameActions = new GameActions(gameBoard, players, gameInterface);
        players.setAIActionHandler(gameActions);

        GameCanvas gameCanvas = new GameCanvas(gameBoard, players);
        contentPane.add(gameCanvas);

        contentPane.setFocusable(true);
        contentPane.addKeyListener(gameCanvas.getGameCameraController());

        AuctionBidExecutor.Factory auctionBetExecutorFactory = new AuctionBidExecutor.Factory();
        auctionBetExecutorFactory.setAiFactory(aiFactory);
        GameInfoPanel gameInfoPanel = new GameInfoPanel(gameActions, auctionBetExecutorFactory, players, gameBoard, saveCreator);
        gameInterface.connectGameInterface(gameInfoPanel.getGameInterface());
        contentPane.add(gameInfoPanel);

        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);

        saveCreator.setGameBoard(gameBoard);
        saveCreator.setPlayers(players);

        gameCanvas.requestRedraw();

    }

    private static Optional<File> getSaveFileSelection() {
        JFileChooser chooser = new JFileChooser();

        int result = chooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            return Optional.of(chooser.getSelectedFile());
        } else {
           return Optional.empty();
        }

    }

    public static void main(String[] args) {
        JFrame mainFrame = new JFrame("Monopoly: The Devil Wants His Money!");
        SaveCreator saveCreator = new SaveCreator();

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(windowSize);
        Container contentPane = mainFrame.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        while (true) {
            GameStartOption gameStartOption = Main.getGameStartChoice();

            if (gameStartOption == GameStartOption.New) {
                Main.startGameDefault(mainFrame, saveCreator);
            } else {
                Optional<File> saveFile = Main.getSaveFileSelection();

                if (saveFile.isPresent()) {
                    Optional<GameStateSave> gameStateSave = loadGameState(saveFile.get());

                    if (gameStateSave.isEmpty()) {
                        continue;
                    }
                    Main.loadGame(mainFrame, saveCreator, gameStateSave.get());
                } else {
                    continue;
                }
            }
            break;
        }
    }

}
