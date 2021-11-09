package tiles;

import java.awt.*;

public enum PropertySet {
    Red,
    Yellow,
    Green,
    Blue,
    LightBlue,
    Pink,
    Orange,
    White,
    Purple;

    public Color getColor() {
        switch (this) {
            case Red -> {
                return Color.RED;
            }
            case Yellow -> {
                return Color.YELLOW;
            }
            case Green -> {
                return Color.GREEN;
            }
            case Blue -> {
                return Color.BLUE;
            }
            case LightBlue -> {
                return Color.CYAN;
            }
            case Pink -> {
                return Color.PINK;
            }
            case Orange -> {
                return Color.ORANGE;
            }
            case Purple -> {
                return Color.MAGENTA;
            }
            default -> {
                return Color.WHITE;
            }
        }
    }
}
