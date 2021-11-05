package gameInterface;

public interface GameDrawable {
    int PLAYER_DRAW_LAYER = 3;
    int TILE_DRAW_LAYER = 2;

    void draw(GameGraphics g);
    int drawLayer();
}
