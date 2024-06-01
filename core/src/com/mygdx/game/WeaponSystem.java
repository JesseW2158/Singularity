package com.mygdx.game;

import java.util.ArrayList;

import net.mgsx.gltf.scene3d.scene.SceneManager;

public class WeaponSystem extends Player {
    private boolean inRange = false;
    private boolean inCombat = false;

    ArrayList<Projectile> projectiles = new ArrayList<>();

    public void createLasers(SceneManager sceneManager, Spaceship ship) {
        Projectile temp = new Projectile();
        temp.create(ship, sceneManager);
        
        projectiles.add(temp);
    }
    
    @SuppressWarnings("unlikely-arg-type")
    public void render(Spaceship ship) {
        ArrayList<Projectile> temp = new ArrayList<>();

        for(Projectile projectile : projectiles) {
            if(projectile.getTime() > 120) {
                temp.add(projectile);
            } else {
                projectile.render();
            }
        }

        projectiles.remove(temp);
    }

    public void calculateIfInRange(Spaceship target) {
        double distance = Math.pow(target.getCurrPos().x - this.getCurrPos().x, 2) + Math.pow(target.getCurrPos().y - this.getCurrPos().y, 2) + Math.pow(target.getCurrPos().z - this.getCurrPos().z, 2);

        inRange = distance < 1500;
    }

    public boolean isInRange() {
        return inRange;
    }

    public void setInRange(boolean inRange) {
        this.inRange = inRange;
    }

    public boolean isInCombat() {
        return inCombat;
    }

    public void setInCombat(boolean inCombat) {
        this.inCombat = inCombat;
    }

    public ArrayList<Projectile> getProjectiles() {
        return projectiles;
    }

    public void setProjectiles(ArrayList<Projectile> projectiles) {
        this.projectiles = projectiles;
    }
}
