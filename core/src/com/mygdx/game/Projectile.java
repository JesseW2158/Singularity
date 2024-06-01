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

public class Projectile {
    private Scene scene;
    private Matrix4 projectileTransform = new Matrix4();
    private Vector3 currPos = new Vector3();
    private Vector3 targPos = new Vector3();

    private float time = 0;

    private int x, y, z;
    private static int projectileNum = 0;

    public Projectile() {
        projectileNum++;
    }

    public void create(Spaceship ship, SceneManager sceneManager) {
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();

        Material material = new Material();
        material.set(PBRColorAttribute.createBaseColorFactor(Color.RED));
        MeshPartBuilder builder = modelBuilder.part("Projectile: " + projectileNum, GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, material);
        BoxShapeBuilder.build(builder, 0, 0, 0, 1f,1f,5f);

        scene = new Scene(new ModelInstance(modelBuilder.end()));
        sceneManager.addScene(scene);

        projectileTransform = new Matrix4(ship.getScene().modelInstance.transform);
        targPos.y += -2f;
        targPos.z += -5f;

        projectileTransform.translate(targPos);
        scene.modelInstance.transform.set(projectileTransform);
        targPos.set(0, 0, 0);
    }

    public void render() {
        time += 1;

        targPos.z += 10f;

        projectileTransform.translate(targPos);
		scene.modelInstance.transform.set(projectileTransform);
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
}
