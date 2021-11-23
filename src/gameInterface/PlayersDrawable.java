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

    public static void drawPlayerBubble(GameGraphics g, Point drawOrigin, Dimension dimension, Color playerColor) {
        g.drawOvalFill(drawOrigin, dimension, playerColor);
        g.drawOval(drawOrigin, dimension, Color.BLACK);
    }

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

    @Override
    public void draw(GameGraphics g) {
        Set<Integer> playerPositions = this.players.stream().map(Player::getTilePosition).collect(Collectors.toSet());

        for (Integer tilePosition : playerPositions) {
            List<Player> playersOnTile = this.players.stream().filter(p -> tilePosition == p.getTilePosition()).collect(Collectors.toList());
            this.drawPlayersOnTile(tilePosition, playersOnTile, g);
        }
    }

    @Override
    public int drawLayer() {
        return GameDrawable.PLAYER_DRAW_LAYER;
    }
}
