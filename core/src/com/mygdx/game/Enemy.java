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

    public void create(SceneManager sceneManager, Player ship) { //creates the ship in a random part of a specified location (used for clustering enemies)
        super.create(sceneManager);
        hp = 10;

        targPos = new Vector3((float)(Math.random() * 300), (float)(Math.random() * 300), (float)(Math.random() * 300));

		worldPos.translate(targPos);

        scene.modelInstance.transform.set(worldPos);
        playerTransform.set(scene.modelInstance.transform);
		scene.modelInstance.transform.getTranslation(currPos);

		targPos.set(0, 0, 0);
    }

    public void handleInput(Player ship, float deltaTime) { //determines what the ship should do
        if(inRange(ship)) { //if player is in range
            ship.getWeaponSystem().setInCombat(true); //makes player unable to warp away
            
            targPos.z += speed * deltaTime; //accelerates ship towards player

            scene.modelInstance.transform.set(playerTransform);

            scene.modelInstance.transform.rotateTowardTarget(ship.getCurrPos(), Vector3.Y).rotate(Vector3.Y, 180); //rotates towards player
            scene.modelInstance.transform.translate(targPos);

            targPos.set(0, 0, 0); 

            weaponSystem.createLasers(sceneManager, this);

            speed += 1f;

            if(speed > maxSpeed/2) { //enemies are only half as fast as player
                speed = maxSpeed/2;
            }
            
            scene.modelInstance.transform.getTranslation(currPos);
            playerTransform.idt().translate(currPos); //zeros out matrix for next rotation
        } else { //if player is out of combat range
            ship.getWeaponSystem().setInCombat(false); //allows player to warp
            
            speed += -1f;

            if(speed < 0) {
                speed = 0;
            }
        }

        weaponSystem.render(new ArrayList<>(Arrays.asList(ship)), sceneManager, true); //renders enemy fire
        destroyedShipRemove(); //removes ship if destroyed
    }

    private boolean inRange(Spaceship ship) { //calculates if player is in range
        float distance = Math.abs(ship.getCurrPos().dst(this.getCurrPos()));

        return distance < 1500f;
    }
}
