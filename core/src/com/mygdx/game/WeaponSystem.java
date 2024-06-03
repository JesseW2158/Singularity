package com.mygdx.game;

import java.util.ArrayList;

import net.mgsx.gltf.scene3d.scene.SceneManager;

public class WeaponSystem extends Spaceship {
    private boolean inRange = false;
    private boolean inCombat = false;
    private boolean dead = false;

    ArrayList<Projectile> projectiles = new ArrayList<>();

    public void createLasers(SceneManager sceneManager, Spaceship ship) { //creates a projectile
        Projectile temp = new Projectile();
        temp.create(ship, sceneManager);
        
        projectiles.add(temp);
    }
    
    public void render(ArrayList<? extends Spaceship> ships, SceneManager sceneManager, boolean enemyFire) { //renders all the projectiles currently
        ArrayList<Projectile> temp = new ArrayList<>();

        for(Projectile projectile : projectiles) { //for each projectile fired by the ship
            if(!projectile.isDisposed() && projectile.getTime() > 120f) { //checks if they've existed longer then 120 frames
                temp.add(projectile); //adds it to the arraylist to be trashed
            } else if(!dead) { //while the ship isn't destroyed
                projectile.render(); //renders projectile
                
                for(Spaceship ship : ships) { //checks all the ships if a projectile has collided with it
                    // if(ship instanceof Player) {
                    //     System.out.println(ship.hp);
                    // }
                    if(projectile.hasCollided(ship)) { //if a collision with a projectile and a ship has happened
                        ship.setHp(ship.getHp() - 1); //ship hp decreases by 1
                    }
                }
            }
        }
        
        for(Projectile projectile : temp) { //removes all trashed projectiles
            projectiles.remove(projectile);
            projectile.gameDispose();
        }

        if(dead) { //if ship dies then it disappears with its projectiles
            for(Projectile projectile : projectiles) {
                projectile.gameDispose();
            }
            projectiles.clear(); //clears this array so they aren't rendered
        }
    }

    //GETTERS AND SETTERS

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
