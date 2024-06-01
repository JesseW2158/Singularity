package com.mygdx.game;

import com.badlogic.gdx.math.Vector3;

import net.mgsx.gltf.scene3d.scene.SceneManager;

public class Enemy extends Spaceship {
    public void create(SceneManager sceneManager, Player ship) {
        super.create(sceneManager);

        targPos = new Vector3((float)(Math.random() * 1000), (float)(Math.random() * 1000), 5000f + (float)(Math.random() * 100));

		playerTransform.translate(targPos);

        scene.modelInstance.transform.set(playerTransform);
		scene.modelInstance.transform.getTranslation(currPos);

		targPos.set(0, 0, 0);
    }

    public void handleInput(Player ship, float deltaTime) {
        if(inRange(ship)) {
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
            speed += -1f;

            if(speed < 0) {
                speed = 0;
            }
        }

        for(Projectile projectile : weaponSystem.getProjectiles()) {
            projectile.render();
        }
    }

    private boolean inRange(Spaceship ship) {
        float distance = Math.abs(ship.getCurrPos().dst(this.getCurrPos()));

        return distance < 1500f;
    }
}
