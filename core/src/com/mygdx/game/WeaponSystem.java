package com.mygdx.game;

import java.util.ArrayList;

import net.mgsx.gltf.scene3d.scene.SceneManager;

public class WeaponSystem extends Spaceship {
    private boolean inRange = false;
    private boolean inCombat = false;
    private boolean dead = false;

    ArrayList<Projectile> projectiles = new ArrayList<>();

    public void createLasers(SceneManager sceneManager, Spaceship ship) {
        Projectile temp = new Projectile();
        temp.create(ship, sceneManager);
        
        projectiles.add(temp);
    }
    
    public void render(ArrayList<? extends Spaceship> ships, SceneManager sceneManager, boolean enemyFire) {
        ArrayList<Projectile> temp = new ArrayList<>();

        for(Projectile projectile : projectiles) {
            if(!projectile.isDisposed() && projectile.getTime() > 120f) {
                temp.add(projectile);
            } else if(!dead) {
                projectile.render();
                
                for(Spaceship ship : ships) {
                    // if(ship instanceof Player) {
                    //     System.out.println(ship.hp);
                    // }
                    if(projectile.hasCollided(ship)) {
                        ship.setHp(ship.getHp() - 1);
                    }
                }
            }
        }
        
        for(Projectile projectile : temp) {
            projectiles.remove(projectile);
            projectile.gameDispose();
        }

        if(dead) {
            for(Projectile projectile : projectiles) {
                projectile.gameDispose();
            }
            projectiles.clear();
        }
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

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }
}
