package gameInterface;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameCameraController implements KeyListener {

    private final GameCamera gameCamera;

    public GameCameraController(GameCamera gameCamera) {
        this.gameCamera = gameCamera;
    }

    /**This method is used when a key is typed
     * @param e the key event that has occurred
     */
    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**This method is used when a arrow key or page up/down key is pressed to move the camera
     * @param e the event of the key being pressed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int CAMERA_MOVEMENT_UNIT = 100;
        float SCALE_CHANGE_UNIT = 0.1f;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> {
                if (e.isShiftDown()) {
                    gameCamera.changeScale(SCALE_CHANGE_UNIT);
                } else {
                    gameCamera.moveCamera(0, CAMERA_MOVEMENT_UNIT);
                }
            }
            case KeyEvent.VK_DOWN -> {
                if (e.isShiftDown()) {
                    gameCamera.changeScale(-SCALE_CHANGE_UNIT);
                } else {
                    gameCamera.moveCamera(0, -CAMERA_MOVEMENT_UNIT);
                }
            }
            case KeyEvent.VK_LEFT -> gameCamera.moveCamera(CAMERA_MOVEMENT_UNIT, 0);
            case KeyEvent.VK_RIGHT -> gameCamera.moveCamera(-CAMERA_MOVEMENT_UNIT, 0);
        }
    }

    /**This method is used when the key is released
     * @param e the event of the key being released
     */
    @Override
    public void keyReleased(KeyEvent e) {

    }
}
