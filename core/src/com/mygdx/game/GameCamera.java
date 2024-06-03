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

    public GameCamera() { //creates camera
        perspectiveCamera = new PerspectiveCamera(60f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //sets near and far clipping
		perspectiveCamera.near = 10f;
		perspectiveCamera.far = 10000000;
		perspectiveCamera.position.set(0, 0, 4f);

		cameraController = new FirstPersonCameraController(perspectiveCamera);
		Gdx.input.setInputProcessor(cameraController); //inputs get routed to cameraController
    }

    public void calculateCameraPosition(Vector3 currPos, float horizontalDistance, float verticalDistance) { //calculates new camera position based on new positioning given from previous calculations
    	float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(angleAroundPlayer)));
        float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(angleAroundPlayer)));

		perspectiveCamera.position.x = currPos.x - offsetX;
		perspectiveCamera.position.z = currPos.z - offsetZ;
		perspectiveCamera.position.y = currPos.y + verticalDistance;
	}

    public void calculateAngleAroundPlayer() { //gets input change on the x axis and multiplies it by a factor before changing angleAroundPlayer
        float angleChange = Gdx.input.getDeltaX() * CAMERA_ANGLE_AROUND_PLAYER_FACTOR;  
        angleAroundPlayer -= angleChange;
	}

    public void calculatePitch() { //gets input change on the y axis and multiplies it by a factor before changing camPitch
		float pitchChange = Gdx.input.getDeltaY() * CAMERA_PITCH_FACTOR;
		camPitch -= pitchChange;
	}

    public float calculateVerticalDistance() { //gets new vertical distance
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(camPitch)));
	}

	public float calculateHorizontalDistance() { //gets new horizontal distance
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(camPitch)));
	}

    public void updateCamera(Player player) {
		calculatePitch();
		calculateAngleAroundPlayer();
        calculateCameraPosition(player.getCurrPos(), calculateHorizontalDistance(), calculateVerticalDistance());

		perspectiveCamera.up.set(Vector3.Y); //tells camera where the up axis is
		perspectiveCamera.lookAt(player.getCurrPos()); //locks camera onto player
		perspectiveCamera.update(); //updates camera for rendering
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
