package gameInterface;

import gameLogic.GameBoard;

public class GameGridDrawable implements GameDrawable {

    private final GameBoard gameBoard;

    public GameGridDrawable(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    @Override
    public void draw(GameGraphics g) {
        this.gameBoard.getTileDrawables().forEach(tileDrawable -> tileDrawable.draw(g));
    }

    @Override
    public int drawLayer() {
        return GameDrawable.TILE_DRAW_LAYER;
    }
}
