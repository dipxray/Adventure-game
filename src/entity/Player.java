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
    boolean attacking = false;
    public Rectangle attackArea = new Rectangle(0, 0, 36, 36);

    public final int screenx;
    public final int screeny;
    public int hasKey = 0;
    public int hasHearts = 0;

    // Quest Flags
    public boolean hasApple = false;
    public boolean hasCherry = false;
    public boolean hasCat = false;
    public boolean hasSword = false;
    public boolean hasDiamond = false;
    public int questStage = 0; // 0: Game Start, 1: Talk to Wizard, etc.
    private boolean initialSceneTriggered = false;

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
        questStage = 0;
        initialSceneTriggered = false;
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
            if (invincible == true) {
                invincibleCounter++;
                if (invincibleCounter > 60) {
                    invincible = false;
                    invincibleCounter = 0;
                }
            }
        }
    }

    public void attacking() {
        spriteCounter++;
        if (spriteCounter <= 5) {
            spriteNum = 1;
        }
        if (spriteCounter > 5 && spriteCounter <= 25) {
            spriteNum = 2; // the 'attack' frame

            // Save current position and hitbox
            int currentWorldX = worldx;
            int currentWorldY = worldy;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;

            // Offset position based on direction so the attack hits in front
            switch (direction) {
                case "up":
                    worldy -= attackArea.height;
                    break;
                case "down":
                    worldy += solidArea.height;
                    break;
                case "left":
                    worldx -= attackArea.width;
                    break;
                case "right":
                    worldx += solidArea.width;
                    break;
            }
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;

            // Removed monster combat logic
            // int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            // damageNPC(npcIndex);

            // Restore position and hitbox
            worldx = currentWorldX;
            worldy = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;
        }
        if (spriteCounter > 25) {
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }
    }

    public void update() {

        if (!initialSceneTriggered && gp.gameState == gp.playState) {
            triggerInitialScene();
            initialSceneTriggered = true;
            return;
        }

        if (attacking == true) {
            attacking();
        } else {
            if (keyH.jPressed == true && hasSword == true) {
                attacking = true;
                return;
            }

            isMoving = false;
            speed = keyH.shiftPressed ? 8 : 4;

            if (keyH.upPressed) {
                direction = "up";
                isMoving = true;
            } else if (keyH.downPressed) {
                direction = "down";
                isMoving = true;
            } else if (keyH.leftPressed) {
                direction = "left";
                isMoving = true;
            } else if (keyH.rightPressed) {
                direction = "right";
                isMoving = true;
            }

            // Always check tile collision
            collisionOn = false;
            gp.cChecker.checkTile(this);

            // 🔥 TELEPORTATION LOGIC
            int playerWorldCol = worldx / gp.tileSize;
            int playerWorldRow = worldy / gp.tileSize;
            int currentTile = gp.tileM.mapTileNum[playerWorldCol][playerWorldRow];

            if (currentTile == 23 && gp.tileM.currentMap.equals("mymap.txt")) {
                gp.switchMap("Map2.txt", 48 * gp.tileSize, 48 * gp.tileSize); // Teleport to entrance (stairs) in Map 2
                                                                              // (Bottom Left)
            } else if (currentTile == 37 && gp.tileM.currentMap.equals("Map2.txt")) {
                gp.switchMap("mymap.txt", 1776, 192); // Teleport back near the portal in Map 1
            }

            // Interaction Check
            int objIndex = gp.cChecker.checkObject(this, true);
            if (objIndex != 999) {
                String name = gp.obj[objIndex].name;
                if (name.equals("Key") || name.equals("Door") || name.equals("Heart")) {
                    pickUpObject(objIndex);
                } else if (keyH.enterPressed) {
                    pickUpObject(objIndex);
                }
            }

            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            if (npcIndex != 999 && keyH.enterPressed) {
                interactNPC(npcIndex);
            }

            if (isMoving && !collisionOn) {
                switch (direction) {
                    case "up":
                        worldy -= speed;
                        break;
                    case "down":
                        worldy += speed;
                        break;
                    case "left":
                        worldx -= speed;
                        break;
                    case "right":
                        worldx += speed;
                        break;
                }
            }

            if (isMoving) {
                spriteCounter++;
                int animationSpeed = keyH.shiftPressed ? 6 : 10;
                if (spriteCounter > animationSpeed) {
                    spriteNum = (spriteNum == 1) ? 2 : 1;
                    spriteCounter = 0;
                }
            } else {
                spriteCounter = 0;
            }

        }

        // 🌊 SEA SOUND (UNCHANGED)
        nearSea = false;
        int playerCol = (worldx + gp.tileSize / 2) / gp.tileSize;
        int playerRow = (worldy + gp.tileSize / 2) / gp.tileSize;

        for (int col = playerCol - 1; col <= playerCol + 1; col++) {
            for (int row = playerRow - 1; row <= playerRow + 1; row++) {
                if (col >= 0 && row >= 0 && col < gp.maxworldcol && row < gp.maxworldrow) {
                    int tileNum = gp.tileM.mapTileNum[col][row];
                    if (tileNum == 12)
                        nearSea = true;
                }
            }
        }

        if (nearSea && !wasNearSea) {
            if (gp.ambient != null) {
                gp.ambient.setFile(0);
                gp.ambient.loop();
            }
        }
        if (!nearSea && wasNearSea) {
            if (gp.ambient != null)
                gp.ambient.stop();
        }
        wasNearSea = nearSea;
    }

    public void pickUpObject(int i) {

        if (gp.obj[i] == null)
            return;

        String objectName = gp.obj[i].name;

        switch (objectName) {

            case "Key":
                hasKey++;
                gp.fileHandler.removedItems.add(gp.tileM.currentMap + ":" + i); // 🔥 PERSIST REMOVAL
                gp.obj[i] = null;
                gp.ui.showMessage("You got a key!", new Color(255, 215, 0));
                gp.se.setFile(2);
                gp.se.play();
                gp.fileHandler.saveData(); // 🔥 AUTO-SAVE on key pickup
                break;

            case "Door":
                if (hasKey > 0) {
                    gp.fileHandler.removedItems.add(gp.tileM.currentMap + ":" + i); // 🔥 PERSIST REMOVAL
                    gp.obj[i] = null;
                    gp.ui.showMessage("You opened the door!", new Color(139, 69, 19));
                    gp.doorSE.setFile(3);
                    gp.doorSE.play();
                    hasKey--;
                    gp.fileHandler.saveData(); // 🔥 AUTO-SAVE on door open
                } else {
                    gp.ui.showMessage("You don't have a key!", Color.gray);
                }
                break;

            case "Apple":
                if (questStage < 2) {
                    gp.ui.showMessage("I should talk to the Wizard first!", Color.white);
                    return;
                }
                collectItem(i, objectName);
                break;
            case "Cherry":
            case "Coconut":
                collectItem(i, objectName);
                break;

            case "Sword":
                hasSword = true;
                gp.obj[i].setDialogue();
                gp.gameState = gp.dialogueState;
                gp.activeObj = gp.obj[i]; // 🔥 Set active object
                gp.obj[i].speak(gp);

                gp.obj[i] = null;
                gp.se.setFile(4);
                gp.se.play();
                break;

            case "Diamond":
                hasDiamond = true;
                gp.obj[i].setDialogue();
                gp.gameState = gp.dialogueState;
                gp.activeObj = gp.obj[i]; // 🔥 Set active object
                gp.obj[i].speak(gp);

                gp.obj[i] = null;
                gp.se.setFile(2);
                gp.se.play();
                break;

            default:
                gp.obj[i] = null;
                gp.se.setFile(2);
                gp.se.play();
                break;
        }
        gp.keyH.enterPressed = false;
    }

    public void interactNPC(int i) {
        if (i != 999) {
            if (gp.keyH.enterPressed == true) {
                gp.gameState = gp.dialogueState;
                gp.activeNPC = gp.npc[i]; // 🔥 Set active NPC
                gp.npc[i].speak();
            }
        }
        gp.keyH.enterPressed = false;
    }

    public void draw(Graphics2D g2) {

        BufferedImage image = null;

        switch (direction) {
            case "up":
                image = !isMoving ? up0 : (spriteNum == 1 ? up1 : up2);
                break;
            case "down":
                image = !isMoving ? down0 : (spriteNum == 1 ? down1 : down2);
                break;
            case "left":
                image = !isMoving ? left0 : (spriteNum == 1 ? left1 : left2);
                break;
            case "right":
                image = !isMoving ? right0 : (spriteNum == 1 ? right1 : right2);
                break;
        }

        int drawX = worldx - getCamX();
        int drawY = worldy - getCamY();

        g2.drawImage(image, drawX, drawY, gp.tileSize, gp.tileSize, null);
    }

    private void collectItem(int i, String objectName) {
        hasHearts++;
        gp.fileHandler.removedItems.add(gp.tileM.currentMap + ":" + i); // 🔥 PERSIST REMOVAL
        gp.obj[i].setDialogue();
        gp.gameState = gp.dialogueState;
        gp.activeObj = gp.obj[i];
        gp.obj[i].speak(gp);

        if (objectName.equals("Apple"))
            hasApple = true;
        if (objectName.equals("Cherry"))
            hasCherry = true;

        gp.obj[i] = null;
        gp.se.setFile(2);
        gp.se.play();
        gp.fileHandler.saveData();
    }

    @Override
    public void speak() {
        if (dialogues[dialogueIndex] != null) {
            gp.ui.currentDialogue = dialogues[dialogueIndex];
            dialogueIndex++;
        }
    }

    private void triggerInitialScene() {
        gp.gameState = gp.dialogueState;
        loadDialogue("“Ahh… so this is the village\nI’ve been searching for.”",
                "“I’ve come too far to\nturn back now…”",
                "“They say a mysterious diamond\nlies hidden somewhere beyond…”",
                "“I need to find it…\nno matter what.”");
        gp.activeNPC = this; // Set player as the active talker
        this.speak();
        questStage = 1; // Transition to 'Talk to Wizard'
        gp.fileHandler.saveData(); // 🔥 Save initial quest state
    }

    public int getCamX() {
        int camX = worldx - screenx;
        if (camX < 0)
            camX = 0;
        if (camX > gp.worldwidth - gp.screenWidth)
            camX = gp.worldwidth - gp.screenWidth;
        return camX;
    }

    public int getCamY() {
        int camY = worldy - screeny;
        if (camY < 0)
            camY = 0;
        if (camY > gp.worldheight - gp.screenHeight)
            camY = gp.worldheight - gp.screenHeight;
        return camY;
    }
}