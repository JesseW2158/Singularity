package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;
public class Projectile {
    private SceneAsset sceneAsset;
    private Scene scene;
    private Matrix4 projectileTransform = new Matrix4();
    private Vector3 currPos = new Vector3();
    private Vector3 targPos = new Vector3();

    private boolean isDisposed = false;

    private float time = 0;

    private static int projectileNum = 0;

    public Projectile() {
        projectileNum++;
    }

    public void create(Spaceship ship, SceneManager sceneManager) {
        sceneAsset = new GLTFLoader().load(Gdx.files.internal("models\\laser.gltf"));
        scene = new Scene(sceneAsset.scene);
        sceneManager.addScene(scene);

        projectileTransform = new Matrix4(ship.getScene().modelInstance.transform);
        targPos.y += -2f;
        targPos.z += -5f;

        projectileTransform.translate(targPos);
        scene.modelInstance.transform.set(projectileTransform);
        scene.modelInstance.transform.getTranslation(currPos);
        targPos.set(0, 0, 0);
    }

    public void render() {
        time += 1;

        targPos.z += 15f;

        projectileTransform.translate(targPos);
		scene.modelInstance.transform.set(projectileTransform);
        scene.modelInstance.transform.getTranslation(currPos);
		targPos.set(0, 0, 0);
    }

    public void dispose() {
        Matrix4 temp = new Matrix4();
        Vector3 trash = new Vector3(1_000_000_000, 1_000_000_000, 1_000_000_000);

        temp.translate(trash);
        scene.modelInstance.transform.set(temp);

        isDisposed = true;
    }

    public boolean hasCollided(Spaceship ship) {
        if(Math.abs(ship.getCurrPos().dst(this.currPos)) < 3f) {
            return true;
        }
    
        return false;
    }

    public static int getProjectileNum() {
        return projectileNum;
    }

    public static void setProjectileNum(int projectileNum) {
        Projectile.projectileNum = projectileNum;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Matrix4 getProjectileTransform() {
        return projectileTransform;
    }

    public void setProjectileTransform(Matrix4 projectileTransform) {
        this.projectileTransform = projectileTransform;
    }

    public Vector3 getCurrPos() {
        return currPos;
    }

    public void setCurrPos(Vector3 currPos) {
        this.currPos = currPos;
    }

    public Vector3 getTargPos() {
        return targPos;
    }

    public void setTargPos(Vector3 targPos) {
        this.targPos = targPos;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public SceneAsset getSceneAsset() {
        return sceneAsset;
    }

    public void setSceneAsset(SceneAsset sceneAsset) {
        this.sceneAsset = sceneAsset;
    }

    public boolean isDisposed() {
        return isDisposed;
    }

    public void setDisposed(boolean isDisposed) {
        this.isDisposed = isDisposed;
    }
}
