package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;

public class Spaceship implements InputProcessor {
    private SceneManager sceneManager;
    private SceneAsset shipAsset;
    private Scene scene;
    private WeaponSystem weaponSystem;

    private boolean warping = false;

    private float speed = 0f;
    private Matrix4 playerTransform = new Matrix4();
    private Vector3 targPos = new Vector3();
    private Vector3 currPos = new Vector3();

    public void create(SceneManager sceneManager) {
		//model obtained from: https://free3d.com/3d-model/intergalactic-spaceships-version-2-blender-292-eevee-359585.html
        this.sceneManager = sceneManager;
		shipAsset = new GLTFLoader().load(Gdx.files.internal("models\\Player Spaceship.gltf"));
		scene = new Scene(shipAsset.scene);
        weaponSystem = new WeaponSystem();
    }

    public void dispose() {
        shipAsset.dispose();
    }

    public void handleInput(float deltaTime) {
		playerTransform = scene.modelInstance.transform;

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}

		if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
			speed += 1f;

            if(speed > 1000) {
                speed = 1000;
            }
        }

		if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
			speed += -1f;
            
            if(speed < 0) {
                speed = 0;
            }
		}
        
        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
			scene.modelInstance.transform.rotate(Vector3.X, -2f);
			scene.modelInstance.transform.set(playerTransform);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.A)) {
			scene.modelInstance.transform.rotate(Vector3.Y, 1f);
			scene.modelInstance.transform.set(playerTransform);
		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.S)) {
			scene.modelInstance.transform.rotate(Vector3.X, 2f);
			scene.modelInstance.transform.set(playerTransform);
		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.D)) {
			scene.modelInstance.transform.rotate(Vector3.Y, -1f);
			scene.modelInstance.transform.set(playerTransform);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.Q)) {
			scene.modelInstance.transform.rotate(Vector3.Z, -3f);
			scene.modelInstance.transform.set(playerTransform);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.E)) {
			scene.modelInstance.transform.rotate(Vector3.Z, 3f);
			scene.modelInstance.transform.set(playerTransform);
		}

        if(warping) {
            speed += 100f;

            if(speed > 10000f) {
                speed = 10000f;
            }
        }

        targPos.z += speed * deltaTime;

		playerTransform.translate(targPos);
		scene.modelInstance.transform.set(playerTransform);
		scene.modelInstance.transform.getTranslation(currPos);
		targPos.set(0, 0, 0);
    }
    
    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.SPACE && !weaponSystem.isInCombat()) {
            warping = true;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.SPACE) {
            warping = false;
            speed = 0;
        }

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(button == Input.Buttons.LEFT) {
            weaponSystem.createLasers(sceneManager, this);
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    //GETTERS AND SETTERS

    public WeaponSystem getWeaponSystem() {
        return weaponSystem;
    }

    public void setWeaponSystem(WeaponSystem weaponSystem) {
        this.weaponSystem = weaponSystem;
    }

    public SceneAsset getShipAsset() {
        return shipAsset;
    }

    public void setShipAsset(SceneAsset shipAsset) {
        this.shipAsset = shipAsset;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public Matrix4 getPlayerTransform() {
        return playerTransform;
    }

    public void setPlayerTransform(Matrix4 playerTransform) {
        this.playerTransform = playerTransform;
    }

    public Vector3 getTargPos() {
        return targPos;
    }

    public void setTargPos(Vector3 targPos) {
        this.targPos = targPos;
    }

    public Vector3 getCurrPos() {
        return currPos;
    }

    public void setCurrPos(Vector3 currPos) {
        this.currPos = currPos;
    }

    public boolean isWarping() {
        return warping;
    }

    public void setWarping(boolean warping) {
        this.warping = warping;
    }
}
