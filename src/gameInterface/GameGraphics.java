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
            this.scaleFactor = new Point2D.Double(realHeight / (double) CANVAS_HEIGHT, realHeight / (double) CANVAS_HEIGHT);

            int widthDifference = realWidth - CANVAS_WIDTH;

            this.originTranslation = new Point(0, widthDifference / 2);
        } else { // Width is constrained by height
            this.scaleFactor = new Point2D.Double(realWidth / (double) CANVAS_WIDTH, realWidth / (double) CANVAS_WIDTH);

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

    public void fillRect(Point origin, Dimension rect, Color color) {
        if (!(this.graphics instanceof Graphics2D graphics2D)) {
            return;
        }

        Point scaledOrigin = scalePoint(origin);
        Dimension scaledDimension = scaleDimension(rect);

        if (color == null) {
            color = Color.WHITE;
        }
        graphics2D.setColor(color);
        graphics2D.setStroke(new BasicStroke(1));
        graphics2D.fillRect(scaledOrigin.x, scaledOrigin.y, scaledDimension.width, scaledDimension.height);
    }

    public void drawRect(Point origin, Dimension rect) {
        if (!(this.graphics instanceof Graphics2D graphics2D)) {
            return;
        }

        Point scaledOrigin = scalePoint(origin);
        Dimension scaledDimension = scaleDimension(rect);

        graphics2D.setColor(Color.BLACK);
        graphics2D.setStroke(new BasicStroke(4));
        graphics2D.drawRect(scaledOrigin.x, scaledOrigin.y, scaledDimension.width, scaledDimension.height);
    }

    public void drawText(String s, Point origin, int fontWidth) {
        int rawStringWidth = graphics.getFontMetrics().stringWidth(s);

        if (!(this.graphics instanceof Graphics2D graphics2D)) {
            return;
        }

        graphics2D.setColor(Color.BLACK);
        graphics2D.setStroke(new BasicStroke(2));

        // Dummy height value
        int unscaledFontWidth = unscaleDimension(new Dimension(rawStringWidth, 1)).width;

        float fontScaling = fontWidth / (float) unscaledFontWidth;

        Font currentFont = graphics.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() * fontScaling);
        graphics2D.setFont(newFont);
        Point scaledOrigin = scalePoint(origin);
        graphics2D.drawString(s, scaledOrigin.x, scaledOrigin.y);
    }

    public void drawOvalFill(Point origin, Dimension widthAndHeight) {
        Point scaledOrigin = scalePoint(origin);
        Dimension scaledWidthAndHeight = scaleDimension(widthAndHeight);

        this.graphics.fillOval(scaledOrigin.x, scaledOrigin.y, scaledWidthAndHeight.width, scaledWidthAndHeight.height);
    }
}
