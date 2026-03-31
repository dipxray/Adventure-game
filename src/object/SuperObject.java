package object;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;


import main.GamePanel;

public class SuperObject {

    public BufferedImage image,image2;
    public String name;
    public boolean collision = false;

    public int worldX, worldY; 
    public Rectangle solidArea=new Rectangle(0,0,48,48);//making the whole object tile as solid
    public int solidAreaDefaultX=0;
    public int solidAreaDefaultY=0;

    public int dialogueIndex = 0;
    public String dialogues[] = new String[10];

    public void loadDialogue(String... lines) {
        for(int i = 0; i < lines.length; i++) {
            dialogues[i] = lines[i];
        }
    }

    public void setDialogue() {}

    public void speak(GamePanel gp) {
        gp.ui.currentDialogue = dialogues[dialogueIndex];
        dialogueIndex++;
    }
    
    

    public void draw(Graphics2D g2, GamePanel gp) {

        int screenX = worldX - gp.player.getCamX();
        int screenY = worldY - gp.player.getCamY();

        // Draw only if visible on screen (optimization)
        if (worldX + gp.tileSize > gp.player.getCamX() &&
            worldX - gp.tileSize < gp.player.getCamX() + gp.screenWidth &&
            worldY + gp.tileSize > gp.player.getCamY() &&
            worldY - gp.tileSize < gp.player.getCamY() + gp.screenHeight) {

            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
    }
}