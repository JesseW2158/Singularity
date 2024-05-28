package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.enums.CameraMode;

public class GameCamera {
    public static final float CAMERA_START_PITCH = 20f;
    public static final float CAMERA_START_HEIGHT = 10f;
    public static final float CAMERA_ZOOM_FACTOR = 0.5f;
    public static final float CAMERA_PITCH_FACTOR = 0.3f;
    public static final float CAMERA_ANGLE_AROUND_PLAYER_FACTOR = 0.2f;
    public static final float CAMERA_MIN_DISTANCE = 4;

    private CameraMode cameraMode = CameraMode.FREE_LOOK;
	private float camPitch = CAMERA_START_PITCH;
	private float distanceFromPlayer = 35f;
	private float angleAroundPlayer = 0f;
	private float angleBehindPlayer = 0f;

    private PerspectiveCamera perspectiveCamera;
	private FirstPersonCameraController cameraController;

    public GameCamera() {
        perspectiveCamera = new PerspectiveCamera(60f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		perspectiveCamera.near = 5f;
		perspectiveCamera.far = 100000;
		perspectiveCamera.position.set(0, 0, 4f);

		cameraController = new FirstPersonCameraController(perspectiveCamera);
		Gdx.input.setInputProcessor(cameraController);
    }

    public void calculateCameraPosition(Vector3 currPos, float horizontalDistance, float verticalDistance) {
    	float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(angleAroundPlayer)));
        float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(angleAroundPlayer)));

        //Must find a way to calculate the rotation of the camera based off of ship's rotation

		perspectiveCamera.position.x = currPos.x - offsetX;
		perspectiveCamera.position.z = currPos.z - offsetZ;
		perspectiveCamera.position.y = currPos.y + verticalDistance;
	}

    public void calculateAngleAroundPlayer() {
		if(cameraMode == CameraMode.FREE_LOOK) {
			float angleChange = Gdx.input.getDeltaX() * CAMERA_ANGLE_AROUND_PLAYER_FACTOR;
			angleAroundPlayer -= angleChange;
		}
	}

    public void calculatePitch() {
		float pitchChange = Gdx.input.getDeltaY() * CAMERA_PITCH_FACTOR;
		camPitch -= pitchChange;
	}

    public float calculateVerticalDistance() {
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(camPitch)));
	}

	public float calculateHorizontalDistance() {
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(camPitch)));
	}
    
    public void updateCamera(Spaceship playerShip) {
		calculatePitch();
		calculateAngleAroundPlayer();
		calculateCameraPosition(playerShip.getCurrPos(), calculateHorizontalDistance(), calculateVerticalDistance());

		perspectiveCamera.up.set(Vector3.Y);
		perspectiveCamera.lookAt(playerShip.getCurrPos());
		perspectiveCamera.update();
	}

    //GETTERS AND SETTERS

    public CameraMode getCameraMode() {
        return cameraMode;
    }

    public void setCameraMode(CameraMode cameraMode) {
        this.cameraMode = cameraMode;
    }

    public float getCamPitch() {
        return camPitch;
    }

    public void setCamPitch(float camPitch) {
        this.camPitch = camPitch;
    }

    public float getDistanceFromPlayer() {
        return distanceFromPlayer;
    }

    public void setDistanceFromPlayer(float distanceFromPlayer) {
        this.distanceFromPlayer = distanceFromPlayer;
    }

    public float getAngleAroundPlayer() {
        return angleAroundPlayer;
    }

    public void setAngleAroundPlayer(float angleAroundPlayer) {
        this.angleAroundPlayer = angleAroundPlayer;
    }

    public float getAngleBehindPlayer() {
        return angleBehindPlayer;
    }

    public void setAngleBehindPlayer(float angleBehindPlayer) {
        this.angleBehindPlayer = angleBehindPlayer;
    }

    public PerspectiveCamera getPerspectiveCamera() {
        return perspectiveCamera;
    }

    public void setPerspectiveCamera(PerspectiveCamera perspectiveCamera) {
        this.perspectiveCamera = perspectiveCamera;
    }

    public FirstPersonCameraController getCameraController() {
        return cameraController;
    }

    public void setCameraController(FirstPersonCameraController cameraController) {
        this.cameraController = cameraController;
    }
}
