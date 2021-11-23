package gameInterface;

import tiles.GameTile;
import tiles.HousingTile;

import java.awt.*;

public class GameTileDrawable implements GameDrawable {
    private final GameTile gameTile;
    private final int tilePosition;
    public static final int TILE_WIDTH = 75;
    public static final int TILE_HEIGHT = 75;
    public static final int HOUSE_WIDTH = 30;
    public static final int HOUSE_HEIGHT = 30;
    private static final int SCREEN_PADDING = 100;

    public GameTileDrawable(GameTile gameTile, int tilePosition) {
        this.gameTile = gameTile;
        this.tilePosition = tilePosition;
    }

    /**This method is used to get the location of a tile
     * @param tilePosition the drawn tile
     */
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

    /**This method is used to get the location of a tile
     */
    private Point getDrawOrigin() {
        return GameTileDrawable.getTileDrawOrigin(this.tilePosition);
    }

    private void drawHotel(GameGraphics g, HousingTile housingTile) {
        Point tileOrigin = GameTileDrawable.getTileDrawOrigin(tilePosition);
        Point drawOrigin = new Point(tileOrigin.x, TILE_HEIGHT + tileOrigin.y - HOUSE_HEIGHT);
        Dimension houseDimension = new Dimension(TILE_WIDTH, HOUSE_HEIGHT);
        g.fillRect(drawOrigin, houseDimension, Color.ORANGE);
        g.drawRect(drawOrigin, houseDimension, Color.BLACK);
    }

    /**This method is used to draw houses and hotels
     * @param g the graphics component
     */
    private void drawUgrades(GameGraphics g) {
        if (this.gameTile.asHousingTile().isEmpty()) {
            return;
        }

        HousingTile housingTile = this.gameTile.asHousingTile().get();
        if (housingTile.hasHotel()) {
            this.drawHotel(g, housingTile);
        } else if (housingTile.numberOfHouses() > 0) {
            Point tileOrigin = GameTileDrawable.getTileDrawOrigin(tilePosition);

            int numHouses = housingTile.numberOfHouses();

            for (int i = 0; i < numHouses; i++) {
                int xOffset = (int) Math.round(i * (GameTileDrawable.HOUSE_WIDTH / (double) numHouses));

                Point drawOrigin = new Point(xOffset + tileOrigin.x, tileOrigin.y + TILE_HEIGHT - HOUSE_HEIGHT / numHouses);
                Dimension houseDimension = new Dimension(HOUSE_WIDTH / numHouses, HOUSE_HEIGHT / numHouses);
                g.fillRect(drawOrigin, houseDimension, Color.GREEN);
                g.drawRect(drawOrigin, houseDimension, Color.BLACK);
            }
        }
    }

    /**This method is used to draw tiles
     * @param g the graphics component
     */
    @Override
    public void draw(GameGraphics g) {
        Point drawOrigin = this.getDrawOrigin();

        g.drawRect(drawOrigin, new Dimension(TILE_WIDTH, TILE_HEIGHT), Color.BLACK);
        g.fillRect(drawOrigin, new Dimension(TILE_WIDTH, TILE_HEIGHT), this.gameTile.getPropertySet().getColor());
        g.drawText(this.gameTile.getName(), drawOrigin, (int) Math.round(TILE_WIDTH * 0.9), Color.BLACK);

        this.drawUgrades(g);
    }

    /**This method is used to draw on the tile layer
     */
    @Override
    public int drawLayer() {
        return GameDrawable.TILE_DRAW_LAYER;
    }
}
