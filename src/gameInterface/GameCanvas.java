package gameInterface;

import gameLogic.GameBoard;
import gameLogic.Players;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class GameCanvas extends JPanel {

    private final GameBoard gameBoard;
    private final Players players;

    public GameCanvas(GameBoard gameBoard, Players players) {
        super();

        this.gameBoard = gameBoard;
        this.players = players;
    }

    @Override
    public Dimension getPreferredSize() {
        Container parent = this.getParent();
        return new Dimension(parent.getWidth(), parent.getHeight() * 2 / 3);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        GameGraphics gameGraphics = new GameGraphics(g, this.getWidth(), this.getHeight());

        gameGraphics.drawRect(new Point(0, 0), new Dimension(GameGraphics.CANVAS_WIDTH - 1, GameGraphics.CANVAS_HEIGHT - 1));

        List<GameDrawable> drawables = new ArrayList<>();
        drawables.add(new GameGridDrawable(this.gameBoard));
        drawables.add(players.getPlayerDrawables());

        PriorityQueue<GameDrawable> gameDrawables = new PriorityQueue<>(Comparator.comparingInt(GameDrawable::drawLayer));
        gameDrawables.addAll(drawables);

        while (!gameDrawables.isEmpty()) {
            gameDrawables.poll().draw(gameGraphics);
        }
    }
}
