package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import net.mgsx.gltf.scene3d.attributes.PBRColorAttribute;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneManager;

public class Projectile extends Spaceship {
    private Scene scene;
    private Matrix4 projectilePos;
    private Vector3 currPos;

    private int x, y, z;
    private int rotatedX, rotatedY, rotatedZ;
    private static int projectileNum = 0;

    public Projectile() {
        projectilePos = new Matrix4();
        currPos = new Vector3();

        projectileNum++;
    }

    public void create(SceneManager sceneManager) {
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();

        Material material = new Material();
        material.set(PBRColorAttribute.createBaseColorFactor(Color.RED));
        MeshPartBuilder builder = modelBuilder.part("Projectile: " + projectileNum, GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, material);
        BoxShapeBuilder.build(builder, this.getCurrPos().x, this.getCurrPos().y - 1f, this.getCurrPos().z, 1f,1f,5f);

        scene = new Scene(new ModelInstance(modelBuilder.end()));
        sceneManager.addScene(scene);

        scene.modelInstance.transform.rotate(Vector3.Z, 50f + this.getCurrPos().x);
    }

    public void handleInput(float deltaTime) {
        super.handleInput(deltaTime);

        //handle input
    }

    public void render(Spaceship ship) {
        projectilePos = scene.modelInstance.transform;

        Vector3 targPos = new Vector3(x, y, z);

        // scene.modelInstance.transform.rotate(ship.getCurrPos(), 10f);
        // scene.modelInstance.transform.set(projectilePos);
        
        projectilePos.translate(targPos);
		scene.modelInstance.transform.getTranslation(currPos);
		targPos.set(0, 0, 0);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
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

    public Matrix4 getProjectilePos() {
        return projectilePos;
    }

    public void setProjectilePos(Matrix4 projectilePos) {
        this.projectilePos = projectilePos;
    }

    public Vector3 getCurrPos() {
        return currPos;
    }

    public void setCurrPos(Vector3 currPos) {
        this.currPos = currPos;
    }

    public int getRotatedX() {
        return rotatedX;
    }

    public void setRotatedX(int rotatedX) {
        this.rotatedX = rotatedX;
    }

    public int getRotatedY() {
        return rotatedY;
    }

    public void setRotatedY(int rotatedY) {
        this.rotatedY = rotatedY;
    }

    public int getRotatedZ() {
        return rotatedZ;
    }

    public void setRotatedZ(int rotatedZ) {
        this.rotatedZ = rotatedZ;
    }
}
