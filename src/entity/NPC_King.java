package entity;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import main.GamePanel;

public class NPC_King extends Entity {

    public NPC_King(GamePanel gp) {
        super(gp);
        direction = "down";
        speed = 1; // King moves slowly

        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
        setDialogue();
    }

    public void getImage() {
        up1 = setupImage("/res/npc/king_up_1.png", "/res/back.png");
        up2 = setupImage("/res/npc/king_up_2.png", "/res/back1.png");
        up3 = setupImage("/res/npc/king_up_3.png", "/res/back2.png");

        down1 = setupImage("/res/npc/king_down_1.png", "/res/front.png");
        down2 = setupImage("/res/npc/king_down_2.png", "/res/front1.png");
        down3 = setupImage("/res/npc/king_down_3.png", "/res/front2.png");

        left1 = setupImage("/res/npc/king_left_1.png", "/res/left.png");
        left2 = setupImage("/res/npc/king_left_2.png", "/res/left1.png");
        left3 = setupImage("/res/npc/king_left_3.png", "/res/left2.png");

        right1 = setupImage("/res/npc/king_right_1.png", "/res/right.png");
        right2 = setupImage("/res/npc/king_right_2.png", "/res/right1.png");
        right3 = setupImage("/res/npc/king_right_3.png", "/res/right2.png");
    }

    public BufferedImage setupImage(String imagePath, String fallbackPath) {
        BufferedImage image = null;
        try {
            java.io.InputStream is = getClass().getResourceAsStream(imagePath);
            if (is == null) {
                is = getClass().getResourceAsStream(fallbackPath);
            }
            if (is != null) {
                image = ImageIO.read(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void setDialogue() {
        if (gp.player.questStage == 6 && gp.player.hasCherry) {
            loadDialogue("King: \n Welcome, traveler. \n Few make it this far.",
                         "You: \n I’m looking for \n a diamond.",
                         "King (smiles): \n Many seek it… \n few succeed.",
                         "You: \n I didn’t come \n this far to fail.",
                         "King: \n Hmm… what do \n you offer in return?",
                         "You: \n I found this \n along the way.",
                         "(Player gives cherry)",
                         "King: \n Ah… a fine cherry. \n You have my attention.",
                         "King (serious): \n The diamond lies \n within a maze…",
                         "King: \n Many have entered. \n None have returned.",
                         "Player: \n Then I’ll be \n the first.");
        } else if (gp.player.questStage == 7) {
            loadDialogue("King: \n Good luck in \n the maze!");
        } else {
            loadDialogue("King: \n The castle \n is quiet.");
        }
    }

    public void setAction() {
        actionLockCounter++;
        if (actionLockCounter == 120) {
            Random random = new Random();
            int i = random.nextInt(100) + 1;

            if (i <= 25) {
                direction = "up";
            } else if (i <= 50) {
                direction = "down";
            } else if (i <= 75) {
                direction = "left";
            } else {
                direction = "right";
            }
            
            // 🔥 Safety check: if picking a direction that goes out of bounds, flip it
            int minX = 40 * gp.tileSize;
            int maxX = 47 * gp.tileSize;
            int minY = 4 * gp.tileSize;
            int maxY = 9 * gp.tileSize;

            if (worldx < minX && direction.equals("left")) direction = "right";
            if (worldx > maxX && direction.equals("right")) direction = "left";
            if (worldy < minY && direction.equals("up")) direction = "down";
            if (worldy > maxY && direction.equals("down")) direction = "up";

            actionLockCounter = 0;
        }
    }

    @Override
    public void update() {
        // Enforce boundaries before the normal update
        int minX = 40 * gp.tileSize;
        int maxX = 47 * gp.tileSize;
        int minY = 4 * gp.tileSize;
        int maxY = 9 * gp.tileSize;

        if (worldx < minX) worldx = minX;
        if (worldx > maxX) worldx = maxX;
        if (worldy < minY) worldy = minY;
        if (worldy > maxY) worldy = maxY;

        super.update();
    }

    public void speak() {
        if (gp.player.questStage == 6 && gp.player.hasCherry) {
            setDialogue();
            super.speak();
            if (dialogues[dialogueIndex] == null) {
                gp.player.questStage = 7; // Enter the Maze and find the Diamond
                gp.player.hasCherry = false; // Give to King
                gp.player.hasSword = true; // Still give the sword as a reward/aid
                
                // 🔥 King vanishes
                for (int i = 0; i < gp.npc.length; i++) {
                    if (gp.npc[i] == this) {
                        gp.npc[i] = null;
                        break;
                    }
                }
                
                gp.fileHandler.saveData(); // 🔥 Save progress
            }
        } else {
            setDialogue();
            super.speak();
        }
    }
}
