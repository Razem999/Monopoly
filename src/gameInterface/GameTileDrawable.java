package gameInterface;

import tiles.GameTile;

import java.awt.*;

public class GameTileDrawable implements GameDrawable {
    private final GameTile gameTile;
    private final int tilePosition;
    public static final int TILE_WIDTH = 75;
    public static final int TILE_HEIGHT = 75;
    private static final int SCREEN_PADDING = 100;

    public GameTileDrawable(GameTile gameTile, int tilePosition) {
        this.gameTile = gameTile;
        this.tilePosition = tilePosition;
    }

    public static Point getTileDrawOrigin(int tilePosition) {
        int bottom = GameGraphics.CANVAS_HEIGHT - TILE_HEIGHT - SCREEN_PADDING;
        int right = GameGraphics.CANVAS_WIDTH - TILE_WIDTH - SCREEN_PADDING;
        int left = right - (10 * TILE_WIDTH);
        int top = bottom - (10 * TILE_HEIGHT);

        // Bottom row
        if (tilePosition < 10) {
            return new Point(right - tilePosition * TILE_WIDTH, bottom);
        } else if (tilePosition < 20) { // Left column
            return new Point(left, bottom - (tilePosition - 10) * TILE_HEIGHT);
        } else if (tilePosition < 30) { // Top row
            return new Point(left + (tilePosition - 20) * TILE_WIDTH, top);
        } else { // Right column
            return new Point(right, top + (tilePosition - 30) * TILE_HEIGHT);
        }
    }

    private Point getDrawOrigin() {
        return GameTileDrawable.getTileDrawOrigin(this.tilePosition);
    }

    @Override
    public void draw(GameGraphics g) {
        Point drawOrigin = this.getDrawOrigin();

        g.drawRect(drawOrigin, new Dimension(TILE_WIDTH, TILE_HEIGHT), Color.BLACK);
        g.fillRect(drawOrigin, new Dimension(TILE_WIDTH, TILE_HEIGHT), this.gameTile.getPropertySet().getColor());
        g.drawText(this.gameTile.getName(), drawOrigin, (int) Math.round(TILE_WIDTH * 0.9), Color.BLACK);
    }

    @Override
    public int drawLayer() {
        return GameDrawable.TILE_DRAW_LAYER;
    }
}
