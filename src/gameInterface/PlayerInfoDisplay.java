package gameInterface;

import gameLogic.GameBoard;
import gameLogic.Player;
import tiles.GameTile;

import java.awt.*;

public class PlayerInfoDisplay implements GameDrawable {
    private final Player player;
    private final GameBoard gameBoard;

    private final static Point margin = new Point(50, 50);

    public PlayerInfoDisplay(Player player, GameBoard gameBoard) {
        this.player = player;
        this.gameBoard = gameBoard;
    }

    /**This method is used to draw the player information onto the screen
     * @param g the graphics component
     */
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

        int playerDotSize = 50;
        PlayersDrawable.drawPlayerBubble(g, margin, new Dimension(playerDotSize, playerDotSize), PlayersDrawable.getPlayerColor(player));

        Point textOrigin = new Point(margin.x, margin.y + playerDotSize);
        g.drawTextMultiline(infoText.toString(), textOrigin, 25, Color.BLACK);
    }

    /**This method is used to draw on the UI components layer
     */
    @Override
    public int drawLayer() {
        return GameDrawable.UI_DRAW_LAYER;
    }
}
