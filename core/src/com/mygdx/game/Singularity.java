package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import net.mgsx.gltf.scene3d.attributes.PBRCubemapAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import net.mgsx.gltf.scene3d.scene.SceneSkybox;
import net.mgsx.gltf.scene3d.utils.IBLBuilder;

public class Singularity extends Game {
	private SceneManager sceneManager;

	private GameCamera camera;
	private Player player;
	
	private Cubemap diffuseCubemap;
	private Cubemap environmentCubemap;
	private Cubemap specularCubemap;
	private Texture brdfLUT;
	private DirectionalLightEx light;
	private SceneSkybox skybox;

	private ArrayList<Enemy> enemies = new ArrayList<>();
	private ArrayList<Scene> planets = new ArrayList<>();

	//quick start from https://github.com/mgsx-dev/gdx-gltf and following https://www.youtube.com/watch?v=e-3OMXY9bDU&t=624s&ab_channel=JamesTKhan's multi-video guide
	@Override
	public void create() {
		// create scene
		sceneManager = new SceneManager();
		player = new Player();

		spawnPlanets();
		spawnEnemies();
		
		//creating and adding player to world
		player.create(sceneManager, enemies);
		sceneManager.addScene(player.getScene());

		camera = new GameCamera();

		//setting the main camera to perspective camera focused on player
		sceneManager.setCamera(camera.getPerspectiveCamera());
		//sets camera position to its correct orientation
		camera.getPerspectiveCamera().position.set(0, 0, 4f);

		//cursor can move around screen freely with no boundaries
		Gdx.input.setCursorCatched(true);

		// setup light
		light = new DirectionalLightEx();
		light.direction.set(1, -3, 1).nor();
		light.color.set(Color.WHITE);
		sceneManager.environment.add(light);

		// setup quick IBL (image based lighting)
		IBLBuilder iblBuilder = IBLBuilder.createOutdoor(light);
		environmentCubemap = iblBuilder.buildEnvMap(1024);
		diffuseCubemap = iblBuilder.buildIrradianceMap(256);
		specularCubemap = iblBuilder.buildRadianceMap(10);
		iblBuilder.dispose();

		brdfLUT = new Texture(Gdx.files.classpath("net/mgsx/gltf/shaders/brdfLUT.png"));

		//lighting system
		sceneManager.setAmbientLight(0.1f);
		sceneManager.environment.set(new PBRTextureAttribute(PBRTextureAttribute.BRDFLUTTexture, brdfLUT));
		sceneManager.environment.set(PBRCubemapAttribute.createSpecularEnv(specularCubemap));
		sceneManager.environment.set(PBRCubemapAttribute.createDiffuseEnv(diffuseCubemap));

		//background
		sceneManager.addScene(new Scene(new GLTFLoader().load(Gdx.files.internal("models\\skybox.gltf")).scene));

		//creating lit up skybox for light for background
		skybox = new SceneSkybox(environmentCubemap);
		sceneManager.setSkyBox(skybox);
	}

	@Override //resizes cameraview to keep constant FOV
	public void resize(int width, int height) {
		sceneManager.updateViewport(width, height);
	}

	@Override
	public void render() {
		float deltaTime = Gdx.graphics.getDeltaTime();

		if(enemies.size() < 1) {
			spawnEnemies();
		}

		planetCollision();

		//Basically like setting keylistener to player
		Gdx.input.setInputProcessor(player);
		processInput(deltaTime);

		removeDeadEnemies();

		// render
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		sceneManager.update(deltaTime);
		sceneManager.render();
	}

	private void removeDeadEnemies() {
		ArrayList<Enemy> temp = new ArrayList<>();

		for(Enemy enemy : enemies) {
			if(enemy.getWeaponSystem().isDead()) {
				temp.add(enemy);
			}
		}

		for(Enemy enemy : temp) {
			enemy.weaponSystem.setDead(true);
			enemy.getWeaponSystem().render(temp, sceneManager, false);
			enemies.remove(enemy);
		}
	}

	private void processInput(float deltaTime) {
		player.handleInput(deltaTime);
		player.getWeaponSystem().render(enemies, sceneManager, false);
		camera.updateCamera(player);

		for(Enemy enemy : enemies) {
			enemy.handleInput(player, deltaTime);
		}
	}

	@Override
	public void dispose() {
		sceneManager.dispose();
		player.dispose();
		environmentCubemap.dispose();
		diffuseCubemap.dispose();
		specularCubemap.dispose();
		brdfLUT.dispose();
		// skybox.dispose();
	}

	private void spawnPlanets() {
		for(int i = 0; i < (float)(Math.random()) * 15 + 1; i++) {
			planets.add(new Scene(new GLTFLoader().load(Gdx.files.internal("models\\Planet" + (int)(Math.random() * 3 + 1) + ".gltf")).scene));
		}

		for(Scene planet : planets) {
			sceneManager.addScene(planet);

			Matrix4 temp = new Matrix4();
			Vector3 pos = new Vector3((float)(Math.random() * 25_000), (float)(Math.random() * 25_000), (float)(Math.random() * 25_000));

			pos.x *= (Math.random() > 0.5) ? -1 : 1;
			pos.y *= (Math.random() > 0.5) ? -1 : 1;
			pos.z *= (Math.random() > 0.5) ? -1 : 1;

			temp.translate(pos);

			planet.modelInstance.transform.set(temp);
		}
	}

	private void planetCollision() {
		for(Scene planet : planets) {
			Vector3 temp = new Vector3();
			temp = planet.modelInstance.transform.getTranslation(temp);

			if(player.getCurrPos().dst(temp) < 1_000f) {
				player.hp = 0;
			}
		}

	}

	private void spawnEnemies() {		
		Matrix4 worldPos = new Matrix4();
		worldPos.translate(new Vector3((float)(Math.random() * 25_000), (float)(Math.random() * 25_000), (float)(Math.random() * 25_000)));

		for(int i = 0; i < (int)(Math.random() * 10) + 1; i++) {
			enemies.add(new Enemy(worldPos));
		}
		
		for(Enemy enemy : enemies) {
			enemy.create(sceneManager, player);
			sceneManager.addScene(enemy.getScene());
		}
	}
}