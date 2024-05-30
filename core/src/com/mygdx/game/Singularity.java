package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import net.mgsx.gltf.scene3d.attributes.PBRCubemapAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import net.mgsx.gltf.scene3d.scene.SceneSkybox;
import net.mgsx.gltf.scene3d.utils.IBLBuilder;

public class Singularity extends Game {
	public SceneManager sceneManager;
	private GameCamera camera;
	private Spaceship playerShip;
	private Spaceship referenceship;
	private Cubemap diffuseCubemap;
	private Cubemap environmentCubemap;
	private Cubemap specularCubemap;
	private Texture brdfLUT;
	private SceneSkybox skybox;
	private DirectionalLightEx light;
	

	//quick start from https://github.com/mgsx-dev/gdx-gltf and following https://www.youtube.com/watch?v=e-3OMXY9bDU&t=624s&ab_channel=JamesTKhan's multi-video guide
	@Override
	public void create() {
		// create scene
		sceneManager = new SceneManager();
		playerShip = new Spaceship();
		referenceship = new Spaceship();

		SceneAsset mars = new GLTFLoader().load(Gdx.files.internal("models\\Mars.gltf"));
		Scene marsScene = new Scene(mars.scene);

		playerShip.create(sceneManager);
		referenceship.create(sceneManager);

		sceneManager.addScene(playerShip.getScene());
		sceneManager.addScene(referenceship.getScene());
		sceneManager.addScene(marsScene);

		camera = new GameCamera();

		sceneManager.setCamera(camera.getPerspectiveCamera());
		camera.getPerspectiveCamera().position.set(0, 0, 4f);

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

		sceneManager.setAmbientLight(0f);
		sceneManager.environment.set(new PBRTextureAttribute(PBRTextureAttribute.BRDFLUTTexture, brdfLUT));
		sceneManager.environment.set(PBRCubemapAttribute.createSpecularEnv(specularCubemap));
		sceneManager.environment.set(PBRCubemapAttribute.createDiffuseEnv(diffuseCubemap));

		// setup skybox
		skybox = new SceneSkybox(environmentCubemap);
		sceneManager.setSkyBox(skybox);
	}

	@Override
	public void resize(int width, int height) {
		sceneManager.updateViewport(width, height);
	}

	@Override
	public void render() {
		float deltaTime = Gdx.graphics.getDeltaTime();

		Gdx.input.setInputProcessor(playerShip);
		processInput(deltaTime);

		// render
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		sceneManager.update(deltaTime);
		sceneManager.render();
	}

	private void processInput(float deltaTime) {
		playerShip.handleInput(deltaTime);
		playerShip.getWeaponSystem().render(playerShip);
		camera.updateCamera(playerShip);
	}

	@Override
	public void dispose() {
		sceneManager.dispose();
		playerShip.dispose();
		environmentCubemap.dispose();
		diffuseCubemap.dispose();
		specularCubemap.dispose();
		brdfLUT.dispose();
		skybox.dispose();
	}

	//GETTERS AND SETTERS

	public SceneManager getSceneManager() {
		return sceneManager;
	}

	public void setSceneManager(SceneManager sceneManager) {
		this.sceneManager = sceneManager;
	}

	public GameCamera getCamera() {
		return camera;
	}

	public void setCamera(GameCamera camera) {
		this.camera = camera;
	}

	public Spaceship getPlayerShip() {
		return playerShip;
	}

	public void setPlayerShip(Spaceship playerShip) {
		this.playerShip = playerShip;
	}

	public Spaceship getReferenceship() {
		return referenceship;
	}

	public void setReferenceship(Spaceship referenceship) {
		this.referenceship = referenceship;
	}

	public Cubemap getDiffuseCubemap() {
		return diffuseCubemap;
	}

	public void setDiffuseCubemap(Cubemap diffuseCubemap) {
		this.diffuseCubemap = diffuseCubemap;
	}

	public Cubemap getEnvironmentCubemap() {
		return environmentCubemap;
	}

	public void setEnvironmentCubemap(Cubemap environmentCubemap) {
		this.environmentCubemap = environmentCubemap;
	}

	public Cubemap getSpecularCubemap() {
		return specularCubemap;
	}

	public void setSpecularCubemap(Cubemap specularCubemap) {
		this.specularCubemap = specularCubemap;
	}

	public Texture getBrdfLUT() {
		return brdfLUT;
	}

	public void setBrdfLUT(Texture brdfLUT) {
		this.brdfLUT = brdfLUT;
	}

	public SceneSkybox getSkybox() {
		return skybox;
	}

	public void setSkybox(SceneSkybox skybox) {
		this.skybox = skybox;
	}

	public DirectionalLightEx getLight() {
		return light;
	}

	public void setLight(DirectionalLightEx light) {
		this.light = light;
	}
}