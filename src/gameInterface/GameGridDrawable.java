package gameInterface;

import gameLogic.GameBoard;

public class GameGridDrawable implements GameDrawable {

    private final GameBoard gameBoard;

    /**This method is used to assign the gameBoard
     * @param gameBoard Has all tile information
     */
    public GameGridDrawable(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    /**This method is used draw the tiles on the gameBoard
     * @param g the graphics component
     */
    @Override
    public void draw(GameGraphics g) {
        this.gameBoard.getTileDrawables().forEach(tileDrawable -> tileDrawable.draw(g));
    }

    /**This method is used to draw the tiles on the tile level of the graphics
     */
    @Override
    public int drawLayer() {
        return GameDrawable.TILE_DRAW_LAYER;
    }
}
