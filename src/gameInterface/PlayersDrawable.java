package gameInterface;

import gameLogic.Player;

import java.awt.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PlayersDrawable implements GameDrawable {
    private final List<Player> players;
    private final int PLAYER_WIDTH = (int) Math.round(GameTileDrawable.TILE_WIDTH * 0.75);
    private final int PLAYER_HEIGHT = (int) Math.round(GameTileDrawable.TILE_HEIGHT * 0.75);

    public PlayersDrawable(List<Player> players) {
        this.players = players;
    }

    /**This method is used to get the players colors for their circles
     * @param player the player whose color is needed
     */
    public static Color getPlayerColor(Player player) {
        int playerID = player.getPlayerID();

        return switch (playerID) {
            case 1 -> Color.RED;
            case 2 -> Color.BLUE;
            case 3 -> Color.GREEN;
            case 4 -> Color.YELLOW;
            default -> Color.WHITE;
        };
    }

    /**This method is used to draw the players circles
     * @param g the graphics component
     * @param drawOrigin the location to draw
     * @param dimension the size of the circle
     * @param playerColor the color of the players circle
     */
    public static void drawPlayerBubble(GameGraphics g, Point drawOrigin, Dimension dimension, Color playerColor) {
        g.drawOvalFill(drawOrigin, dimension, playerColor);
        g.drawOval(drawOrigin, dimension, Color.BLACK);
    }

    /**This method is used to draw players onto the board
     * @param tilePosition the tile position on the gameBoard
     * @param players the players in the game
     * @param g the graphics component
     */
    private void drawPlayersOnTile(int tilePosition, List<Player> players, GameGraphics g) {
        Point tileOrigin = GameTileDrawable.getTileDrawOrigin(tilePosition);

        final int MAX_PLAYERS_IN_ROW_ON_TILE = 3;
        int playersInRow = Math.min(players.size(), MAX_PLAYERS_IN_ROW_ON_TILE);

        for (int i = 0; i < players.size(); i++) {
            int row = (i / playersInRow);
            int rowPosition = (i % playersInRow);

            int xOffset = (int) Math.round(rowPosition * (GameTileDrawable.TILE_WIDTH / (double) MAX_PLAYERS_IN_ROW_ON_TILE));
            int yOffset = (int) Math.round(row * (GameTileDrawable.TILE_HEIGHT / (double) MAX_PLAYERS_IN_ROW_ON_TILE));

            Color playerColor = PlayersDrawable.getPlayerColor(players.get(i));

            Point drawOrigin = new Point(xOffset + tileOrigin.x, yOffset + tileOrigin.y);
            Dimension playerDimension = new Dimension(PLAYER_WIDTH / playersInRow, PLAYER_HEIGHT / playersInRow);
            PlayersDrawable.drawPlayerBubble(g, drawOrigin, playerDimension, playerColor);
        }
    }

    /**This method is used to draw the players onto the screen
     * @param g the graphics component
     */
    @Override
    public void draw(GameGraphics g) {
        Set<Integer> playerPositions = this.players.stream().map(Player::getTilePosition).collect(Collectors.toSet());

        for (Integer tilePosition : playerPositions) {
            List<Player> playersOnTile = this.players.stream().filter(p -> tilePosition == p.getTilePosition()).collect(Collectors.toList());
            this.drawPlayersOnTile(tilePosition, playersOnTile, g);
        }
    }

    /**This method is used to draw on the player level
     */
    @Override
    public int drawLayer() {
        return GameDrawable.PLAYER_DRAW_LAYER;
    }
}
