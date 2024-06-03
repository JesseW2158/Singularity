package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;

import net.mgsx.gltf.scene3d.scene.SceneManager;

public class Player extends Spaceship implements InputProcessor {
    private ClosestEnemy targettingArrow;

    public void create(SceneManager sceneManager, ArrayList<Enemy> enemies) {
        super.create(sceneManager);
        
        targPos.z += 5000;

		playerTransform.translate(targPos);
		scene.modelInstance.transform.set(playerTransform);
		scene.modelInstance.transform.getTranslation(currPos);
		targPos.set(0, 0, 0);

        targettingArrow = new ClosestEnemy(sceneManager, this, enemies);
        targettingArrow.create();
    }

    public void handleInput(float deltaTime) {
        playerTransform = scene.modelInstance.transform;

        targettingArrow.gameDispose();

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}

		if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && !warping) {
			speed += 1f;

            if(speed > maxSpeed) {
                speed = maxSpeed;
            }
        }

		if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && !warping) {
			speed += -2f;
            
            if(speed < 0) {
                speed = 0;
            }
		}
        
        if(Gdx.input.isKeyPressed(Input.Keys.W) && !warping) {
			scene.modelInstance.transform.rotate(Vector3.X, -5f);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.A) && !warping) {
			scene.modelInstance.transform.rotate(Vector3.Y, 3f);
        }
		
		if(Gdx.input.isKeyPressed(Input.Keys.S) && !warping) {
			scene.modelInstance.transform.rotate(Vector3.X, 5f);
		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.D) && !warping) {
			scene.modelInstance.transform.rotate(Vector3.Y, -3f);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.Q) && !warping) {
			scene.modelInstance.transform.rotate(Vector3.Z, -5f);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.E) && !warping) {
			scene.modelInstance.transform.rotate(Vector3.Z, 5f);
		}

        if(warping) {
            if(speed < 1) {
                speed = 1;
            }

            speed *= 1.1;

            if(speed > 10000f) {
                speed = 10000f;
            }
        }

        if(shooting && !warping) {
            weaponSystem.createLasers(sceneManager, this);
        }

        if(currPos.dst(0, 0, 0) > 45000) {
            speed = 0;
            targPos.set(targPos.x - 50, targPos.y - 50, targPos.z - 50);
        }

        targPos.z += speed * deltaTime;

		playerTransform.translate(targPos);
		scene.modelInstance.transform.set(playerTransform);
		scene.modelInstance.transform.getTranslation(currPos);
		targPos.set(0, 0, 0);
        
        targettingArrow.create();
        targettingArrow.render();

        destroyedShipRemove();
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
            speed = maxSpeed;
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
            shooting = true;
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
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
