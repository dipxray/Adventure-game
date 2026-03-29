package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.keyhandler;

public class Player extends Entity {

    GamePanel gp;
    keyhandler keyH;

    boolean nearSea = false;
    boolean wasNearSea = false;
    boolean isMoving = false;

    public final int screenx;
    public final int screeny;
    public int hasKey = 0;
    public int hasHearts = 0;

    BufferedImage up0, up1, up2, down0, down1, down2, left0, left1, left2, right0, right1, right2;

    public Player(GamePanel gp, keyhandler keyH) {
        super(gp);
        this.gp = gp;
        this.keyH = keyH;

        screenx = gp.screenWidth / 2 - (gp.tileSize / 2);
        screeny = gp.screenHeight / 2 - (gp.tileSize / 2);

        solidArea = new Rectangle(0, 16, 32, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        setDefaultValues();
        getplayerimage();
    }

    public void setDefaultValues() {
        worldx = 1294;
        worldy = 1294;
        speed = 4;
        direction = "down";

        maxLife = 6;
        life = maxLife;

        hasKey = 0;
        hasHearts = 0;
    }

    public void getplayerimage() {
        try {
            up0 = ImageIO.read(getClass().getResourceAsStream("/res/back.png"));
            up1 = ImageIO.read(getClass().getResourceAsStream("/res/back1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/res/back2.png"));

            down0 = ImageIO.read(getClass().getResourceAsStream("/res/front.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/res/front1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/res/front2.png"));

            left0 = ImageIO.read(getClass().getResourceAsStream("/res/left.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/res/left1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/res/left2.png"));

            right0 = ImageIO.read(getClass().getResourceAsStream("/res/right.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/res/right1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/res/right2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {

        isMoving = false;

        speed = keyH.shiftPressed ? 8 : 4;

        if (keyH.upPressed) { direction = "up"; isMoving = true; }
        else if (keyH.downPressed) { direction = "down"; isMoving = true; }
        else if (keyH.leftPressed) { direction = "left"; isMoving = true; }
        else if (keyH.rightPressed) { direction = "right"; isMoving = true; }

        if (isMoving) {

            collisionOn = false;
            gp.cChecker.checkTile(this);

            int objIndex = gp.cChecker.checkObject(this, true);

            if (objIndex != 999) {
                pickUpObject(objIndex);
            }
            //npc collision
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);
            if (!collisionOn) {
                switch (direction) {
                    case "up": worldy -= speed; break;
                    case "down": worldy += speed; break;
                    case "left": worldx -= speed; break;
                    case "right": worldx += speed; break;
                }
            }

            spriteCounter++;
            int animationSpeed = keyH.shiftPressed ? 6 : 10;
            if (spriteCounter > animationSpeed) {
                spriteNum = (spriteNum == 1) ? 2 : 1;
                spriteCounter = 0;
            }

        } else {
            spriteCounter = 0;
        }

        // 🌊 SEA SOUND (UNCHANGED)
        nearSea = false;
        int playerCol = (worldx + gp.tileSize / 2) / gp.tileSize;
        int playerRow = (worldy + gp.tileSize / 2) / gp.tileSize;

        for (int col = playerCol - 1; col <= playerCol + 1; col++) {
            for (int row = playerRow - 1; row <= playerRow + 1; row++) {
                if (col >= 0 && row >= 0 && col < gp.maxworldcol && row < gp.maxworldrow) {
                    int tileNum = gp.tileM.mapTileNum[col][row];
                    if (tileNum == 12) nearSea = true;
                }
            }
        }

        if (nearSea && !wasNearSea) {
            if (gp.ambient != null) { gp.ambient.setFile(0); gp.ambient.loop(); }
        }
        if (!nearSea && wasNearSea) {
            if (gp.ambient != null) gp.ambient.stop();
        }
        wasNearSea = nearSea;
    }

    public void pickUpObject(int i){

        if(gp.obj[i] == null) return;

        String objectName = gp.obj[i].name;

        switch(objectName){

            case "Key":
                hasKey++;
                gp.obj[i] = null;
                gp.ui.showMessage("You got a key!", new Color(255, 215, 0));
                gp.se.setFile(2);
                gp.se.play();
                break;

            case "Door":
                if(hasKey > 0){
                    gp.obj[i] = null;
                    gp.ui.showMessage("You opened the door!", new Color(139, 69, 19));
                    gp.doorSE.setFile(3);
                    gp.doorSE.play();
                    hasKey--;
                } else {
                    gp.ui.showMessage("You don't have a key!", Color.gray);
                }
                break;

            case "Apple":
            case "Coconut":
            case "Cherry":
                hasHearts++;
                gp.obj[i] = null; // 🔥 remove permanently

                gp.ui.showMessage("You got a " + objectName.toLowerCase() + "! +1 Heart", Color.red);
                gp.se.setFile(2);
                gp.se.play();

                // 🔥 SAVE immediately so it never comes back
                gp.fileHandler.saveData();

                break;

           case "Sword":
    gp.obj[i] = null;

    gp.ui.showMessage("You got a sword!", Color.white);
    gp.se.setFile(4);
    gp.se.play();

    if(hasHearts >= 3){
        gp.gameState = gp.fightState;

        gp.music.stop();
        gp.music.setFile(7);
        gp.music.play();
        gp.music.loop();
    } else {
        gp.ui.showMessage("Need 3 hearts!", Color.red);
    }

    break;

            default:
                gp.obj[i] = null;
                gp.se.setFile(2);
                gp.se.play();
                break;
        }
    }

    public void interactNPC(int i) {
        if (i != 999) {
            System.out.println("COLLISION DETECTED with NPC " + i);
            gp.ui.showMessage("NPC Collision!", new Color(255, 0, 0));
        }
    }
    public void draw(Graphics2D g2) {

        BufferedImage image = null;

        switch (direction) {
            case "up": image = !isMoving ? up0 : (spriteNum == 1 ? up1 : up2); break;
            case "down": image = !isMoving ? down0 : (spriteNum == 1 ? down1 : down2); break;
            case "left": image = !isMoving ? left0 : (spriteNum == 1 ? left1 : left2); break;
            case "right": image = !isMoving ? right0 : (spriteNum == 1 ? right1 : right2); break;
        }

        int drawX = worldx - getCamX();
        int drawY = worldy - getCamY();

        g2.drawImage(image, drawX, drawY, gp.tileSize, gp.tileSize, null);
    }

    public int getCamX() {
        int camX = worldx - screenx;
        if (camX < 0) camX = 0;
        if (camX > gp.worldwidth - gp.screenWidth) camX = gp.worldwidth - gp.screenWidth;
        return camX;
    }

    public int getCamY() {
        int camY = worldy - screeny;
        if (camY < 0) camY = 0;
        if (camY > gp.worldheight - gp.screenHeight) camY = gp.worldheight - gp.screenHeight;
        return camY;
    }
}