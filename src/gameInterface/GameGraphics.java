package gameInterface;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * The gameInterface.GameGraphics is where the drawable elements such as tiles and scales are determined
 */
public class GameGraphics {
    /**
     * TextDrawLocation determines the location something will be drawn
     */
    public enum TextDrawLocation {
        TopLeft,
        TopRight,
        BottomLeft,
        BottomRight,
    }

    private final Graphics graphics;
    private final GameCamera gameCamera;

    private final Point2D scaleFactor;

    private final Point originTranslation;

    public static int CANVAS_HEIGHT = 1000;
    public static int CANVAS_WIDTH = 1500;

    /**This is the constructor of GameGraphics with parameters
     * @param graphics Provides the drawing features
     * @param realWidth the width of the game window
     * @param realHeight the height of the game window
     */
    public GameGraphics(Graphics graphics, int realWidth, int realHeight, GameCamera gameCamera) {
        this.graphics = graphics;

        this.gameCamera = gameCamera;

        double realRatio = realWidth / (double) realHeight;
        double canvasRatio = CANVAS_WIDTH / (double) CANVAS_HEIGHT;

        if (realRatio > canvasRatio) { // Height is constrained by width
            this.scaleFactor = new Point2D.Double(realHeight / (double) CANVAS_HEIGHT, realHeight / (double) CANVAS_HEIGHT);

            int widthDifference = realWidth - (int) Math.round(CANVAS_WIDTH * this.scaleFactor.getX());

            this.originTranslation = new Point(widthDifference / 2, 0);
        } else { // Width is constrained by height
            this.scaleFactor = new Point2D.Double(realWidth / (double) CANVAS_WIDTH, realWidth / (double) CANVAS_WIDTH);

            int heightDifference = realHeight - (int) Math.round(CANVAS_HEIGHT * this.scaleFactor.getY());

            this.originTranslation = new Point(0, heightDifference / 2);
        }
    }

    /**This function assists with making the game scalable and reactive
     * @param p the point in the game canvas points
     */
    private Point scalePoint(Point p) {
        int newX = (int) Math.round(p.x * this.scaleFactor.getX() * this.gameCamera.getScale() + this.originTranslation.x + this.gameCamera.getCameraOffset().x);
        int newY = (int) Math.round(p.y * this.scaleFactor.getY() * this.gameCamera.getScale() + this.originTranslation.y + this.gameCamera.getCameraOffset().y);
        return new Point(newX, newY);
    }

    /**This function assists with rendering text fonts in the right size
     * @param scaledDimension the dimension to scale the text too
     */
    private Dimension unscaleDimension(Dimension scaledDimension) {
        return new Dimension((int) Math.round(scaledDimension.width / this.scaleFactor.getX() / this.gameCamera.getScale()),
                (int) Math.round(scaledDimension.height / this.scaleFactor.getY() / this.gameCamera.getScale()));
    }

    /**This function assists with rendering text fonts in the right size
     * @param d the dimension to scale the text too
     */
    private Dimension scaleDimension(Dimension d) {
        return new Dimension((int) Math.round(d.width * this.scaleFactor.getX() * this.gameCamera.getScale()),
                (int) Math.round(d.height * this.scaleFactor.getY() * this.gameCamera.getScale()));
    }

    /**This function draws a rectangle filled by a defined color
     * @param origin the coordinates for game canvas
     * @param rect the dimensions of the rectangle
     * @param color the color to fill the rectangle with
     */
    public void fillRect(Point origin, Dimension rect, Color color) {
        if (!(this.graphics instanceof Graphics2D graphics2D)) {
            return;
        }

        Point scaledOrigin = scalePoint(origin);
        Dimension scaledDimension = scaleDimension(rect);

        Color previousColor = graphics2D.getColor();

        graphics2D.setColor(color);
        graphics2D.setStroke(new BasicStroke(1));
        graphics2D.fillRect(scaledOrigin.x, scaledOrigin.y, scaledDimension.width, scaledDimension.height);
        graphics2D.setColor(previousColor);
    }

    /**This function draws the outline of a rectangle
     * @param origin the coordinates for game canvas
     * @param rect the dimensions of the rectangle
     * @param color the color to fill the rectangle with
     */
    public void drawRect(Point origin, Dimension rect, Color color) {
        if (!(this.graphics instanceof Graphics2D graphics2D)) {
            return;
        }

        Point scaledOrigin = scalePoint(origin);
        Dimension scaledDimension = scaleDimension(rect);

        Color previousColor = graphics2D.getColor();

        graphics2D.setColor(color);
        graphics2D.setStroke(new BasicStroke(4));
        graphics2D.drawRect(scaledOrigin.x, scaledOrigin.y, scaledDimension.width, scaledDimension.height);
        graphics2D.setColor(previousColor);
    }

    private static Point getTextDrawOffsetFromDrawLocation(TextDrawLocation drawLocation, int unscaledFontWidth, int unscaledFontHeight) {
        switch (drawLocation) {
            case TopRight -> {
                return new Point(0, 0);
            }
            case BottomRight -> {
                return new Point(0, unscaledFontHeight);
            }
            case TopLeft -> {
                return new Point(-unscaledFontWidth, 0);
            }
            case BottomLeft -> {
                return new Point(-unscaledFontWidth, unscaledFontHeight);
            }
            default -> {
                return new Point(0, 0);
            }
        }
    }

    /**This function draws text with a specific font size
     * @param origin the coordinates for game canvas
     * @param s the string the text will be showing
     * @param color the color of the text
     * @param fontSize the fontSize of the text
     */
    public void drawTextWithFontSize(String s, Point origin, int fontSize, Color color) {
        drawTextWithFontSize(s, origin, fontSize, TextDrawLocation.BottomRight, color);
    }

    /**This function draws text with a specific font size
     * @param origin the coordinates for game canvas
     * @param s the string the text will be showing
     * @param color the color of the text
     * @param fontSize the fontSize of the text
     */
    public void drawTextWithFontSize(String s, Point origin, int fontSize, TextDrawLocation drawLocation, Color color) {
        if (!(this.graphics instanceof Graphics2D graphics2D)) {
            return;
        }

        Font previousFont = graphics2D.getFont();

        Font currentFont = graphics.getFont();
        Font newFont = currentFont.deriveFont(fontSize);

        int rawStringWidth = graphics.getFontMetrics().stringWidth(s);

        // We use a dummy width because we need to scale the width before the height adjustments can be calculated
        int unscaledStringWidth = unscaleDimension(new Dimension(rawStringWidth, 1)).width;

        graphics2D.setFont(newFont);

        Point scaledOrigin = scalePoint(origin);
        int rawStringHeight = GameGraphics.getStringHeight(s, graphics2D, scaledOrigin.x, scaledOrigin.y);

        graphics2D.setColor(Color.BLACK);
        graphics2D.setStroke(new BasicStroke(2));

        // We already have the unscaled width, so we can use a dummy width here
        int unscaledStringHeight = unscaleDimension(new Dimension(1, rawStringHeight)).height;
        Point textDrawOffset = GameGraphics.getTextDrawOffsetFromDrawLocation(drawLocation, unscaledStringWidth, unscaledStringHeight);

        Color previousColor = graphics2D.getColor();

        graphics2D.setColor(color);
        graphics2D.drawString(s, scaledOrigin.x + textDrawOffset.x, scaledOrigin.y + textDrawOffset.y);
        graphics2D.setFont(previousFont);
        graphics2D.setColor(previousColor);
    }

    /**This function draws text that uses multiple lines
     * @param s the string to draw onto the screen
     * @param origin the coordinates for the string
     * @param fontSize the size of the text font
     * @param color the color of the text
     */
    public void drawTextMultiline(String s, Point origin, int fontSize, Color color) {
        if (!(graphics instanceof Graphics2D graphics2D)) {
            return;
        }

        String[] lines = s.split("\n");

        int accumulatedYOffset = 0;

        for (String line : lines) {
            this.drawTextWithFontSize(line, new Point(origin.x, origin.y + accumulatedYOffset), fontSize, TextDrawLocation.BottomRight, color);

            Point scaledOrigin = this.scalePoint(origin);

            int unscaledHeight = GameGraphics.getStringHeight(s, graphics2D, (float) scaledOrigin.getX(), (float) scaledOrigin.getY()) + 5;

            // Dummy width dimension
            accumulatedYOffset += unscaleDimension(new Dimension(1, unscaledHeight)).height;
        }
    }

    /**This function draws text
     * @param s the string to draw onto the screen
     * @param origin the coordinates for the string
     * @param fontWidth the width of the area to draw the text
     * @param drawLocation the location to draw the string
     * @param color the color of the text
     */
    public void drawText(String s, Point origin, int fontWidth, TextDrawLocation drawLocation, Color color) {
        if (!(this.graphics instanceof Graphics2D graphics2D)) {
            return;
        }

        Font previousFont = graphics2D.getFont();

        int rawStringWidth = graphics.getFontMetrics().stringWidth(s);

        // We use a dummy width because we need to scale the width before the height adjustments can be calculated
        int unscaledStringWidth = unscaleDimension(new Dimension(rawStringWidth, 1)).width;
        float fontScaling = fontWidth / (float) unscaledStringWidth;
        Font currentFont = graphics.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() * fontScaling);

        graphics2D.setFont(newFont);

        Point scaledOrigin = scalePoint(origin);
        int rawStringHeight = GameGraphics.getStringHeight(s, graphics2D, scaledOrigin.x, scaledOrigin.y);

        graphics2D.setColor(Color.BLACK);
        graphics2D.setStroke(new BasicStroke(2));

        // We already have the unscaled width, so we can use a dummy width here
        int unscaledStringHeight = unscaleDimension(new Dimension(1, rawStringHeight)).height;
        Point textDrawOffset = GameGraphics.getTextDrawOffsetFromDrawLocation(drawLocation, unscaledStringWidth, unscaledStringHeight);

        Color previousColor = graphics2D.getColor();

        graphics2D.setColor(color);
        graphics2D.drawString(s, scaledOrigin.x + textDrawOffset.x, scaledOrigin.y + textDrawOffset.y);
        graphics2D.setFont(previousFont);
        graphics2D.setColor(previousColor);
    }

    /**This function draws text
     * @param s the string to draw onto the screen
     * @param origin the coordinates for the string
     * @param fontWidth the width of the area to draw the text
     * @param color the color of the text
     */
    public void drawText(String s, Point origin, int fontWidth, Color color) {
        drawText(s, origin, fontWidth, TextDrawLocation.BottomRight, color);
    }

    /**This function draws an oval filled by a defined color
     * @param origin the coordinates for game canvas
     * @param widthAndHeight the dimensions of the oval
     * @param color the color to fill the rectangle with
     */
    public void drawOvalFill(Point origin, Dimension widthAndHeight, Color color) {
        Point scaledOrigin = scalePoint(origin);
        Dimension scaledWidthAndHeight = scaleDimension(widthAndHeight);

        Color previousColor = graphics.getColor();

        this.graphics.setColor(color);
        this.graphics.fillOval(scaledOrigin.x, scaledOrigin.y, scaledWidthAndHeight.width, scaledWidthAndHeight.height);

        this.graphics.setColor(previousColor);
    }

    /**This function draws the outline of an oval
     * @param origin the coordinates for game canvas
     * @param widthAndHeight the dimensions of the oval
     * @param color the color to fill the rectangle with
     */
    public void drawOval(Point origin, Dimension widthAndHeight, Color color) {
        Point scaledOrigin = scalePoint(origin);
        Dimension scaledWidthAndHeight = scaleDimension(widthAndHeight);

        Color previousColor = this.graphics.getColor();

        this.graphics.setColor(color);
        this.graphics.drawOval(scaledOrigin.x, scaledOrigin.y, scaledWidthAndHeight.width, scaledWidthAndHeight.height);
        this.graphics.setColor(previousColor);
    }

    /**This function gets the height of the string
     * @param s the string to draw onto the screen
     * @param g the 2D graphics component
     * @param x the x coordinate of the string
     * @param y the y coordinate of the string
     */
    private static int getStringHeight(String s, Graphics2D g, float x, float y) {
        return g.getFont().createGlyphVector(g.getFontRenderContext(), s).getPixelBounds(null, x, y).height;
    }
}
