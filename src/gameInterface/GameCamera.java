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

    public void addCameraChangeListener(CameraChangeListener listener) {
        this.cameraChangeListeners.add(listener);
    }

    private void updateListeners() {
        for (CameraChangeListener listener : this.cameraChangeListeners) {
            listener.handleCameraChange(this);
        }
    }

    public void moveCamera(int xDelta, int yDelta) {
        this.offset = new Point(offset.x + xDelta, offset.y + yDelta);
        this.updateListeners();
    }

    public void changeScale(float change) {
        this.scale += change;
        this.updateListeners();
    }

    public Point getCameraOffset() {
        return this.offset;
    }

    public float getScale() {
        return this.scale;
    }
}
