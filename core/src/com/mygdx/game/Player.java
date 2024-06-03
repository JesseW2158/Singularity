package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;

import net.mgsx.gltf.scene3d.scene.SceneManager;

public class Player extends Spaceship implements InputProcessor {
    private ClosestEnemy targettingArrow;

    public void create(SceneManager sceneManager, ArrayList<Enemy> enemies) { //creates ship and targetting arrow
        super.create(sceneManager);

		scene.modelInstance.transform.getTranslation(currPos);
		targPos.set(0, 0, 0);

        targettingArrow = new ClosestEnemy(sceneManager, this, enemies);
        targettingArrow.create();
    }

    public void handleInput(float deltaTime) { //handles player input
        playerTransform = scene.modelInstance.transform;

        targettingArrow.gameDispose();

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) { //exit program
			Gdx.app.exit();
		}

		if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && !warping) { //shift for accelerate
			speed += 1f;

            if(speed > maxSpeed) {
                speed = maxSpeed;
            }
        }

		if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && !warping) { //control for deceleration
			speed += -2f;
            
            if(speed < 0) {
                speed = 0;
            }
		}
        
        if(Gdx.input.isKeyPressed(Input.Keys.W) && !warping) { //pitch upward
			scene.modelInstance.transform.rotate(Vector3.X, -5f);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.A) && !warping) { //yaw left
			scene.modelInstance.transform.rotate(Vector3.Y, 3f);
        }
		
		if(Gdx.input.isKeyPressed(Input.Keys.S) && !warping) { //pitch down
			scene.modelInstance.transform.rotate(Vector3.X, 5f);
		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.D) && !warping) { //yaw right
			scene.modelInstance.transform.rotate(Vector3.Y, -3f);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.Q) && !warping) { //roll left
			scene.modelInstance.transform.rotate(Vector3.Z, -5f);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.E) && !warping) { //roll right
			scene.modelInstance.transform.rotate(Vector3.Z, 5f);
		}

        if(warping) { //accelerates ship for warping
            if(speed < 1) {
                speed = 1;
            }

            speed *= 1.1;

            if(speed > 10000f) {
                speed = 10000f;
            }
        }

        if(shooting && !warping) { //creates laser on left click
            weaponSystem.createLasers(sceneManager, this);
        }

        if(currPos.dst(0, 0, 0) > 49000) { //map boundaries
            speed = 0;
            targPos.set(targPos.x - 50, targPos.y - 50, targPos.z - 50
            
            );
        }

        targPos.z += speed * deltaTime; //moves ship to new z position

		playerTransform.translate(targPos); //applies vector to matrix
		scene.modelInstance.transform.set(playerTransform); //sets current matrix to updated matrix
		scene.modelInstance.transform.getTranslation(currPos); //sets currPos to updated pos
		targPos.set(0, 0, 0); //zeros targPos
        
        targettingArrow.create(); //creates arrow
        targettingArrow.render(); //renders arrow

        destroyedShipRemove(); //removes ship if destroyed
    }
    
    @Override
    public boolean keyDown(int keycode) { //if player holds down space and isn't in combat then warp activates
        if(keycode == Input.Keys.SPACE && !weaponSystem.isInCombat()) {
            warping = true;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) { //stops warp upon space release
        if(keycode == Input.Keys.SPACE) {
            warping = false;
            speed = maxSpeed;
        }

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) { //if player holds left click then projectiles fire
        if(button == Input.Buttons.LEFT) {
            shooting = true;
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) { //if player lets go of left click then projectiles stop firing
        if(button == Input.Buttons.LEFT) {
            shooting = false;
        }

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
}
