package gameInterface;

import gameLogic.GameBoard;
import gameLogic.Player;
import gameLogic.Players;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * The gameInterface.GameCanvas is where the drawable elements such as tiles and players are controlled
 */
public class GameCanvas extends JPanel implements GameCamera.CameraChangeListener, Player.PlayerChangeListener {
    private final GameCamera camera;
    private final GameCameraController gameCameraController;
    private final GameBoard gameBoard;
    private final Players players;

    /**This is the constructor of GameCanvas with parameters
     * @param gameBoard Contains all tiles and their order. (Everything that makes up the game board)
     * @param players Contains a list of all the players currently playing the game
     */
    public GameCanvas(GameBoard gameBoard, Players players) {
        super();

        this.camera = new GameCamera();
        this.gameCameraController = new GameCameraController(this.camera);
        this.camera.addCameraChangeListener(this);

        this.gameBoard = gameBoard;
        this.players = players;

        this.players.addPlayerChangeListener(this);
    }

    public GameCameraController getGameCameraController() {
        return this.gameCameraController;
    }

    @Override
    public Dimension getPreferredSize() {
        Container parent = this.getParent();
        return new Dimension(parent.getWidth(), parent.getHeight() * 2 / 3);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Dimension preferredSize = this.getPreferredSize();
        GameGraphics gameGraphics = new GameGraphics(g, preferredSize.width, preferredSize.height, this.camera);

        gameGraphics.drawRect(new Point(0, 0), new Dimension(GameGraphics.CANVAS_WIDTH - 1, GameGraphics.CANVAS_HEIGHT - 1), Color.BLACK);

        List<GameDrawable> drawables = new ArrayList<>();
        drawables.add(new GameGridDrawable(this.gameBoard));
        drawables.add(players.getPlayerDrawables());

        if (players.getPlayersList().size() > 0) {
            drawables.add(new PlayerInfoDisplay(players.getCurrentPlayer(), gameBoard));
        }

        PriorityQueue<GameDrawable> gameDrawables = new PriorityQueue<>(Comparator.comparingInt(GameDrawable::drawLayer));
        gameDrawables.addAll(drawables);

        while (!gameDrawables.isEmpty()) {
            gameDrawables.poll().draw(gameGraphics);
        }
    }

    public void requestRedraw() {
        SwingUtilities.invokeLater(this::repaint);
    }

    @Override
    public void handleCameraChange(GameCamera newCamera) {
        this.requestRedraw();
    }

    @Override
    public void handlePlayerChange(Player player) {
        this.requestRedraw();
    }
}
