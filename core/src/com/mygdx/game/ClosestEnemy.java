package com.mygdx.game;

import java.util.ArrayList;

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

public class ClosestEnemy {
    private SceneManager sceneManager;
    private ModelInstance arrow;
    private Player player;

    private Matrix4 worldTransform = new Matrix4();
    
    private ArrayList<Enemy> enemies;

    public ClosestEnemy(SceneManager sceneManager, Player player, ArrayList<Enemy> enemies) {
        this.sceneManager = sceneManager;
        this.player = player;
        this.enemies = enemies;

        worldTransform = new Matrix4(player.getScene().modelInstance.transform);
    }

    public void create() {
        ModelBuilder modelBuilder = new ModelBuilder();
		modelBuilder.begin();

        Material material = new Material();
        material.set(PBRColorAttribute.createBaseColorFactor(Color.GREEN));
        MeshPartBuilder builder = modelBuilder.part("Target Path", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, material);
        BoxShapeBuilder.build(builder, 0, 0, 0, 1f, 1f, 5f);

        arrow = new ModelInstance(modelBuilder.end());
		sceneManager.addScene(new Scene(arrow));

        worldTransform = new Matrix4(player.getScene().modelInstance.transform);
    }

    public void render() {
        int closestEnemy = 0;
        float closestEnemyDist = Float.MAX_VALUE;

        worldTransform.idt().translate(player.getCurrPos());

        for(int i = 0; i < enemies.size(); i++) {
            float temp = enemies.get(i).getCurrPos().dst(player.currPos);
            if(temp < closestEnemyDist) {
                closestEnemyDist = temp;
                closestEnemy = i;
            }
        }

        worldTransform.rotateTowardTarget(enemies.get(closestEnemy).getCurrPos(), Vector3.Y);
        worldTransform.rotate(Vector3.X, 180).translate(new Vector3(0, 0, 5f));
        arrow.transform.set(worldTransform);
    }

    public void gameDispose() {
        Matrix4 temp = new Matrix4();
        Vector3 trash = new Vector3(1_000_000_000, 1_000_000_000, 1_000_000_000);

        temp.translate(trash);
        arrow.transform.set(temp);
    }
}
