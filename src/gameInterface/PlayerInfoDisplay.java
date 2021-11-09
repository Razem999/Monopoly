package gameInterface;

import gameLogic.GameBoard;
import gameLogic.Player;

import java.awt.*;

public class PlayerInfoDisplay implements GameDrawable {
    private Player player;
    private GameBoard gameBoard;

    private final static Point margin = new Point(50, 50);

    public PlayerInfoDisplay(Player player, GameBoard gameBoard) {
        this.player = player;
        this.gameBoard = gameBoard;
    }

    @Override
    public void draw(GameGraphics g) {
        g.drawTextWithFontSize("Player " + this.player.getPlayerID() + "\n", margin, 25, Color.BLACK);
    }

    @Override
    public int drawLayer() {
        return GameDrawable.UI_DRAW_LAYER;
    }
}
