package entity;

import main.GamePanel;

public class Projectile extends Entity {

    Entity user;

    public Projectile(GamePanel gp) {
        super(gp);
    }

    public void set(int worldX, int worldY, String direction, boolean alive, Entity user) {
        this.worldx = worldX;
        this.worldy = worldY;
        this.direction = direction;
        this.alive = alive;
        this.user = user;
        this.life = this.maxLife; // used as flight duration/distance
    }

    public void update() {

        if (user == gp.player) {
            // Player's projectile (e.g. magic spell) hitting monster/boss
            // Not implemented yet
        } else {
            // Monster projectile hitting player
            boolean contactPlayer = gp.cChecker.checkPlayer(this);
            if (contactPlayer == true && gp.player.invincible == false) {
                gp.player.life -= attack;
                gp.player.invincible = true;
                alive = false; // Projectile disappears when it hits
            }
        }

        switch (direction) {
            case "up": worldy -= speed; break;
            case "down": worldy += speed; break;
            case "left": worldx -= speed; break;
            case "right": worldx += speed; break;
        }

        life--;
        if (life <= 0) {
            alive = false;
        }

        spriteCounter++;
        if (spriteCounter > 12) {
            spriteNum++;
            if (spriteNum > 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }
}
