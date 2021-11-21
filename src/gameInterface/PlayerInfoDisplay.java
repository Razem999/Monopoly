package gameInterface;

import gameLogic.GameBoard;
import gameLogic.Player;
import tiles.GameTile;

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
        StringBuilder infoText = new StringBuilder();
        infoText.append("Player ");
        infoText.append(this.player.getPlayerID());
        infoText.append(":\nBalance: $");
        infoText.append(this.player.getBalance());
        infoText.append("\nTile: ");

        infoText.append(this.gameBoard.getTileDescriptionByIndex(this.player.getTilePosition()).orElse(""));

        infoText.append("\nOwns:\n");
        for (GameTile tile : this.gameBoard.getTilesOwnedByPlayer(this.player)) {
            infoText.append(tile.getName());
            infoText.append("\n");
        }

        g.drawTextMultiline(infoText.toString(), margin, 25, Color.BLACK);
    }

    @Override
    public int drawLayer() {
        return GameDrawable.UI_DRAW_LAYER;
    }
}
