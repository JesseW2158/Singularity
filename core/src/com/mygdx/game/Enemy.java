package com.mygdx.game;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import net.mgsx.gltf.scene3d.scene.SceneManager;

public class Enemy extends Spaceship {
    Matrix4 worldPos;

    public Enemy(Matrix4 worldPos) {
        this.worldPos = worldPos;
    }

    public void create(SceneManager sceneManager, Player ship) {
        super.create(sceneManager);
        hp = 1;

        targPos = new Vector3((float)(Math.random() * 300), (float)(Math.random() * 300), (float)(Math.random() * 300));

		worldPos.translate(targPos);

        scene.modelInstance.transform.set(worldPos);
        playerTransform.set(scene.modelInstance.transform);
		scene.modelInstance.transform.getTranslation(currPos);

		targPos.set(0, 0, 0);
    }

    public void handleInput(Player ship, float deltaTime) {
        if(inRange(ship)) {
            ship.getWeaponSystem().setInCombat(true);
            
            targPos.z += speed * deltaTime;

            scene.modelInstance.transform.set(playerTransform);

            scene.modelInstance.transform.rotateTowardTarget(ship.getCurrPos(), Vector3.Y).rotate(Vector3.Y, 180);
            scene.modelInstance.transform.translate(targPos);

            targPos.set(0, 0, 0);

            weaponSystem.createLasers(sceneManager, this);

            speed += 1f;

            if(speed > maxSpeed/2) {
                speed = maxSpeed/2;
            }
            
            scene.modelInstance.transform.getTranslation(currPos);
            playerTransform.idt().translate(currPos);
        } else {
            ship.getWeaponSystem().setInCombat(false);
            
            speed += -1f;

            if(speed < 0) {
                speed = 0;
            }
        }

        weaponSystem.render(new ArrayList<>(Arrays.asList(ship)), sceneManager, true);
        destroyedShipRemove();
    }

    private boolean inRange(Spaceship ship) {
        float distance = Math.abs(ship.getCurrPos().dst(this.getCurrPos()));

        return distance < 1500f;
    }
}
