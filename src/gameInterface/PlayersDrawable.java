package gameInterface;

import gameLogic.Player;
import gameLogic.Players;

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

    private void drawPlayersOnTile(int tilePosition, List<Player> players, GameGraphics g) {
        Point tileOrigin = GameTileDrawable.getTileDrawOrigin(tilePosition);

        final int MAX_PLAYERS_IN_ROW_ON_TILE = 3;
        int playersInRow = Math.min(players.size(), MAX_PLAYERS_IN_ROW_ON_TILE);

        for (int i = 0; i < players.size(); i++) {
            int row = (i / playersInRow);
            int rowPosition = (i % playersInRow);

            int xOffset = (int) Math.round(rowPosition * (GameTileDrawable.TILE_WIDTH / (double) MAX_PLAYERS_IN_ROW_ON_TILE));
            int yOffset = (int) Math.round(row * (GameTileDrawable.TILE_HEIGHT / (double) MAX_PLAYERS_IN_ROW_ON_TILE));

            Point drawOrigin = new Point(xOffset + tileOrigin.x, yOffset + tileOrigin.y);
            g.drawOvalFill(drawOrigin, new Dimension(PLAYER_WIDTH / playersInRow, PLAYER_HEIGHT / playersInRow));
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
