package gameInterface;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameCameraController implements KeyListener {

    private final GameCamera gameCamera;

    public GameCameraController(GameCamera gameCamera) {
        this.gameCamera = gameCamera;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int CAMERA_MOVEMENT_UNIT = 100;
        float SCALE_CHANGE_UNIT = 0.1f;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> gameCamera.moveCamera(0, CAMERA_MOVEMENT_UNIT);
            case KeyEvent.VK_DOWN -> gameCamera.moveCamera(0, -CAMERA_MOVEMENT_UNIT);
            case KeyEvent.VK_LEFT -> gameCamera.moveCamera(CAMERA_MOVEMENT_UNIT, 0);
            case KeyEvent.VK_RIGHT -> gameCamera.moveCamera(-CAMERA_MOVEMENT_UNIT, 0);
            case KeyEvent.VK_PAGE_UP -> gameCamera.changeScale(SCALE_CHANGE_UNIT);
            case KeyEvent.VK_PAGE_DOWN -> gameCamera.changeScale(-SCALE_CHANGE_UNIT);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
