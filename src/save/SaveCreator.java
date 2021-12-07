package save;

import gameLogic.GameBoard;
import gameLogic.Players;

import javax.swing.*;
import java.io.*;
import java.util.Optional;

public class SaveCreator {
    private Optional<GameBoard> gameBoard;
    private Optional<Players> players;

    public SaveCreator() {
        this.gameBoard = Optional.empty();
        this.players = Optional.empty();
    }

    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = Optional.of(gameBoard);
    }

    public void setPlayers(Players players) {
        this.players = Optional.of(players);
    }

    private void saveToFile(File file, GameBoard gameBoard, Players players) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            GameStateSave gameStateSave = new GameStateSave(gameBoard, players);

            objectOutputStream.writeObject(gameStateSave);

            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createSave() {
        if (gameBoard.isEmpty() || players.isEmpty()) {
            JOptionPane.showMessageDialog(null, "ERROR: Game not yet initialized.");
            return;
        }

        JFileChooser chooser = new JFileChooser();

        int result = chooser.showSaveDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File chosenFile = chooser.getSelectedFile();

            saveToFile(chosenFile, gameBoard.get(), players.get());
        }

    }
}
