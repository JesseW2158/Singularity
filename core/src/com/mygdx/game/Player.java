package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;

import net.mgsx.gltf.scene3d.scene.SceneManager;

public class Player extends Spaceship implements InputProcessor {
    @Override
    public void create(SceneManager sceneManager) {
        super.create(sceneManager);
        
        targPos.z += 5000;

		playerTransform.translate(targPos);
		scene.modelInstance.transform.set(playerTransform);
		scene.modelInstance.transform.getTranslation(currPos);
		targPos.set(0, 0, 0);
    }

    public void handleInput(float deltaTime) {
        playerTransform = scene.modelInstance.transform;

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
			scene.modelInstance.transform.rotate(Vector3.X, -2f);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.A) && !warping) {
			scene.modelInstance.transform.rotate(Vector3.Y, 1f);
		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.S) && !warping) {
			scene.modelInstance.transform.rotate(Vector3.X, 2f);
		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.D) && !warping) {
			scene.modelInstance.transform.rotate(Vector3.Y, -1f);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.Q) && !warping) {
			scene.modelInstance.transform.rotate(Vector3.Z, -3f);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.E) && !warping) {
			scene.modelInstance.transform.rotate(Vector3.Z, 3f);
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

        targPos.z += speed * deltaTime;

		playerTransform.translate(targPos);
		scene.modelInstance.transform.set(playerTransform);
		scene.modelInstance.transform.getTranslation(currPos);
		targPos.set(0, 0, 0);

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
