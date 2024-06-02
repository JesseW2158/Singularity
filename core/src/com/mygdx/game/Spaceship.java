package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;

public abstract class Spaceship {
    protected SceneManager sceneManager;
    protected SceneAsset shipAsset;
    protected Scene scene;

    protected WeaponSystem weaponSystem;

    protected boolean warping = false;
    protected boolean shooting = false;
    
    protected float speed = 0f;
    protected float maxSpeed = 100f;

    protected int hp = 100;

    protected Matrix4 playerTransform = new Matrix4();
    protected Vector3 targPos = new Vector3();
    protected Vector3 currPos = new Vector3();

    public void create(SceneManager sceneManager) {
		//model obtained from: https://free3d.com/3d-model/intergalactic-spaceships-version-2-blender-292-eevee-359585.html
        this.sceneManager = sceneManager;
		shipAsset = new GLTFLoader().load(Gdx.files.internal("models\\Player Spaceship.gltf"));
		scene = new Scene(shipAsset.scene);
        weaponSystem = new WeaponSystem();
    }

    public void gameDispose() {
        Matrix4 temp = new Matrix4();
        Vector3 trash = new Vector3(1_000_000_000, 1_000_000_000, 1_000_000_000);

        temp.translate(trash);
        scene.modelInstance.transform.set(temp);
    }

    public void destroyedShipRemove() {
        if(hp < 1) {
            gameDispose();
        }
    }

    public void dispose() {
        shipAsset.dispose();
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

    public SceneManager getSceneManager() {
        return sceneManager;
    }

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    public boolean isShooting() {
        return shooting;
    }

    public void setShooting(boolean shooting) {
        this.shooting = shooting;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
}
