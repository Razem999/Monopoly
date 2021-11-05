package gameInterface;

import java.awt.*;
import java.awt.geom.Point2D;

public class GameGraphics {
    private final Graphics graphics;

    private final Point2D scaleFactor;

    private final Point originTranslation;

    public static int CANVAS_HEIGHT = 1000;
    public static int CANVAS_WIDTH = 1500;

    public GameGraphics(Graphics graphics, int realWidth, int realHeight) {
        this.graphics = graphics;

        double realRatio = realWidth / (double) realHeight;
        double canvasRatio = CANVAS_WIDTH / (double) CANVAS_HEIGHT;

        if (realRatio > canvasRatio) { // Height is constrained by width
            this.scaleFactor = new Point2D.Double(1, realHeight / (double) CANVAS_HEIGHT);

            int widthDifference = realWidth - CANVAS_WIDTH;

            this.originTranslation = new Point(0, widthDifference / 2);
        } else { // Width is constrained by height
            this.scaleFactor = new Point2D.Double(realWidth/ (double) CANVAS_WIDTH, 1);

            int heightDifference = realHeight - CANVAS_HEIGHT;

            this.originTranslation = new Point(heightDifference / 2, 0);
        }
    }

    private Point scalePoint(Point p) {
        return new Point((int) Math.round(p.x * this.scaleFactor.getX() + this.originTranslation.x),
                (int) Math.round(p.y * this.scaleFactor.getY() + this.originTranslation.y));
    }

    private Dimension unscaleDimension(Dimension scaledDimension) {
        return new Dimension((int) Math.round(scaledDimension.width / this.scaleFactor.getX()),
                (int) Math.round(scaledDimension.height / this.scaleFactor.getY()));
    }

    private Dimension scaleDimension(Dimension d) {
        return new Dimension((int) Math.round(d.width * this.scaleFactor.getX()),
                (int) Math.round(d.height * this.scaleFactor.getY()));
    }

    public void drawRect(Point origin, Dimension rect) {
        Point scaledOrigin = scalePoint(origin);
        Dimension scaledDimension = scaleDimension(rect);

        this.graphics.drawRect(scaledOrigin.x, scaledOrigin.y, scaledDimension.width, scaledDimension.height);
    }

    public void drawText(String s, Point origin, int fontWidth) {
        int rawStringWidth = graphics.getFontMetrics().stringWidth(s);

        // Dummy height value
        int unscaledFontWidth = unscaleDimension(new Dimension(rawStringWidth, 1)).width;

        float fontScaling = fontWidth / (float) unscaledFontWidth;

        Font currentFont = graphics.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() * fontScaling);

        this.graphics.setFont(newFont);
        Point scaledOrigin = scalePoint(origin);
        this.graphics.drawString(s, scaledOrigin.x, scaledOrigin.y);
    }
}