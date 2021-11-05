package gameInterface;

import gameLogic.Player;

public class PlayerDrawable implements GameDrawable {
    private final Player player;

    public PlayerDrawable(Player player) {
        this.player = player;
    }

    @Override
    public void draw(GameGraphics g) {

    }

    @Override
    public int drawLayer() {
        return GameDrawable.PLAYER_DRAW_LAYER;
    }
}
