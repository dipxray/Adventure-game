package entity;

import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import main.GamePanel;

public class wizard extends Entity {

    public wizard(GamePanel gp) {
        super(gp);
        direction = "down";
        speed = 2;
        
        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getplayerimage();
    }

    public void getplayerimage() {
        try {
            up1 = ImageIO.read(getClass().getResourceAsStream("/res/npc/wizard_back.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/res/npc/wizard_back1.png"));
            up3 = ImageIO.read(getClass().getResourceAsStream("/res/npc/wizard_back2.png"));

            down1 = ImageIO.read(getClass().getResourceAsStream("/res/npc/wizard_front.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/res/npc/wizard_front1.png"));
            down3 = ImageIO.read(getClass().getResourceAsStream("/res/npc/wizard_front2.png"));

            left1 = ImageIO.read(getClass().getResourceAsStream("/res/npc/wizard_left.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/res/npc/wizard_left1.png"));
            left3 = ImageIO.read(getClass().getResourceAsStream("/res/npc/wizard_left2.png"));

            right1 = ImageIO.read(getClass().getResourceAsStream("/res/npc/wizard_right.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/res/npc/wizard_right1.png"));
            right3 = ImageIO.read(getClass().getResourceAsStream("/res/npc/wizard_right2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }   public void setAction() {
        actionLockCounter++;
        if (actionLockCounter == 120) {
            Random random = new Random();
            int i = random.nextInt(50) + 1; // pick up a number from one to fifty
            if (i <= 25) {
                direction = "left";
            } else {
                direction = "right";
            }
            actionLockCounter = 0;
        }
    }

    
}
