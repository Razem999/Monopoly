package gameInterface;

import tiles.GameTileI;

import java.awt.*;

public class GameTileDrawable implements GameDrawable {
    private final GameTileI gameTile;
    private final int tilePosition;
    private static final int TILE_WIDTH = 75;
    private static final int TILE_HEIGHT = 75;
    private static final int SCREEN_PADDING = 100;

    public GameTileDrawable(GameTileI gameTile, int tilePosition) {
        this.gameTile = gameTile;
        this.tilePosition = tilePosition;
    }

    private Point getDrawOrigin() {
        int bottom = GameGraphics.CANVAS_HEIGHT - TILE_HEIGHT - SCREEN_PADDING;
        int right = GameGraphics.CANVAS_WIDTH - TILE_WIDTH - SCREEN_PADDING;
        int left = right - (10 * TILE_WIDTH);
        int top = bottom - (10 * TILE_HEIGHT);

        // Bottom row
        if (this.tilePosition < 10) {
            return new Point(right - this.tilePosition * TILE_WIDTH, bottom);
        } else if (this.tilePosition < 20) { // Left column
            return new Point(left, bottom - (this.tilePosition - 10) * TILE_HEIGHT);
        } else if (this.tilePosition < 30) { // Top row
            return new Point(left + (this.tilePosition - 20) * TILE_WIDTH, top);
        } else { // Right column
            return new Point(right, top + (this.tilePosition - 30) * TILE_HEIGHT);
        }
    }

    @Override
    public void draw(GameGraphics g) {
        Point drawOrigin = this.getDrawOrigin();

        g.drawRect(drawOrigin, new Dimension(TILE_WIDTH, TILE_HEIGHT));
        g.drawText(this.gameTile.getName(), drawOrigin, TILE_WIDTH);
    }

    @Override
    public int drawLayer() {
        return GameDrawable.TILE_DRAW_LAYER;
    }

}
