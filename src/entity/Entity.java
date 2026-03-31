package entity;

import java.awt.Rectangle;       
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import main.GamePanel;

public class Entity {

    GamePanel gp;
    public int worldx, worldy;
    public int speed;
    
    public BufferedImage up1, up2, up3, up4, up5, up6, up7, up8, up9, up10;
    public BufferedImage down1, down2, down3, down4, down5, down6, down7, down8, down9, down10;
    public BufferedImage left1, left2, left3, left4, left5, left6, left7, left8, left9, left10;
    public BufferedImage right1, right2, right3, right4, right5, right6, right7, right8, right9, right10;
    public BufferedImage image;

    public int actionLockCounter = 0;

    public String direction = "down";
    public int spriteCounter = 0;
    public int spriteNum = 1;

    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;
    public String dialogues[]=new String[20];
    public int dialogueIndex=0;

    // Character status
    public String name = "";
    public int maxLife;
    public int life;
    public boolean alive = true;
    public boolean invincible = false;
    public int invincibleCounter = 0;
    public int attack;
    public Projectile projectile;

    public Entity(GamePanel gp) {
        this.gp = gp;
    }
    public void speak(){
        gp.ui.currentDialogue=dialogues[dialogueIndex];
        dialogueIndex++;
        
        switch(gp.player.direction){
            case "left":
                direction="right";
                break;
            case "right":
                direction="left";
                break;
        }
    }
    public void loadDialogue(String... lines) {
    for(int i = 0; i < lines.length; i++) {
        dialogues[i] = lines[i];
    }
}
    
    public void update() {
        setAction();
        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this, false);
        gp.cChecker.checkPlayer(this);
        
        if (!collisionOn) {
            switch (direction) {
                case "up": worldy -= speed; break;
                case "down": worldy += speed; break;
                case "left": worldx -= speed; break;
                case "right": worldx += speed; break;
            }
        }

        spriteCounter++;
        int animationSpeed = 12;
        if (spriteCounter > animationSpeed) {
            spriteNum++;
            if (spriteNum > 3) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }

        if (invincible == true) {
            invincibleCounter++;
            if (invincibleCounter > 40) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }
    
    public void setAction(){}

    public void draw(Graphics2D g2){
        int screenX = worldx - gp.player.getCamX();
        int screenY = worldy - gp.player.getCamY();

        // Draw only if visible on screen (optimization)
        if (worldx + gp.tileSize > gp.player.getCamX() &&
            worldx - gp.tileSize < gp.player.getCamX() + gp.screenWidth &&
            worldy + gp.tileSize > gp.player.getCamY() &&
            worldy - gp.tileSize < gp.player.getCamY() + gp.screenHeight) {

            switch(direction){
                case "up":
                    if(spriteNum==1) image=up1;
                    if(spriteNum==2) image=up2;
                    if(spriteNum==3) image=up3;
                    break;
                case "down":
                    if(spriteNum==1) image=down1;
                    if(spriteNum==2) image=down2;
                    if(spriteNum==3) image=down3;
                    break;
                case "left":
                    if(spriteNum==1) image=left1;
                    if(spriteNum==2) image=left2;
                    if(spriteNum==3) image=left3;
                    break;
                case "right":
                    if(spriteNum==1) image=right1;
                    if(spriteNum==2) image=right2;
                    if(spriteNum==3) image=right3;
                    break;
            }

            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
    }
}