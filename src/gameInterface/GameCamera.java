package gameInterface;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameCamera {
    private Point offset;
    private float scale;

    private final List<CameraChangeListener> cameraChangeListeners;

    public interface CameraChangeListener {
        void handleCameraChange(GameCamera newCamera);
    }

    public GameCamera() {
        this.cameraChangeListeners = new ArrayList<>();
        this.offset = new Point(0, 0);
        this.scale = 1.f;
    }

    /**This method is used to tell places to rerender when
     * @param listener listens for a change in the camera
     */
    public void addCameraChangeListener(CameraChangeListener listener) {
        this.cameraChangeListeners.add(listener);
    }

    /**This method tell the game that the camera has changed
     */
    private void updateListeners() {
        for (CameraChangeListener listener : this.cameraChangeListeners) {
            listener.handleCameraChange(this);
        }
    }

    /**This method is used to move the cameras position
     * @param xDelta the x-coordinate of the camera new position
     * @param yDelta the y-coordinate of the camera new position
     */
    public void moveCamera(int xDelta, int yDelta) {
        this.offset = new Point(offset.x + xDelta, offset.y + yDelta);
        this.updateListeners();
    }

    /**This method is used to change the scale of the camera
     * @param change the amount of which the scale has changed
     */
    public void changeScale(float change) {
        this.scale += change;
        this.updateListeners();
    }

    /**This method returns where the camera is
     */
    public Point getCameraOffset() {
        return this.offset;
    }

    /**This method returns the scale of the camera
     */
    public float getScale() {
        return this.scale;
    }
}
