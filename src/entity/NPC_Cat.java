package entity;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import main.GamePanel;

public class NPC_Cat extends Entity {

    public NPC_Cat(GamePanel gp) {
        super(gp);
        direction = "down";
        speed = 2; // Cat moves faster
        
        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    public void getImage() {
        up1 = setupImage("/res/npc/cat_up_1.png", "/res/back.png");
        up2 = setupImage("/res/npc/cat_up_2.png", "/res/back1.png");
        up3 = setupImage("/res/npc/cat_up_3.png", "/res/back2.png");

        down1 = setupImage("/res/npc/cat_down_1.png", "/res/front.png");
        down2 = setupImage("/res/npc/cat_down_2.png", "/res/front1.png");
        down3 = setupImage("/res/npc/cat_down_3.png", "/res/front2.png");

        left1 = setupImage("/res/npc/cat_left_1.png", "/res/left.png");
        left2 = setupImage("/res/npc/cat_left_2.png", "/res/left1.png");
        left3 = setupImage("/res/npc/cat_left_3.png", "/res/left2.png");

        right1 = setupImage("/res/npc/cat_right_1.png", "/res/right.png");
        right2 = setupImage("/res/npc/cat_right_2.png", "/res/right1.png");
        right3 = setupImage("/res/npc/cat_right_3.png", "/res/right2.png");
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

    public void setAction() {
        actionLockCounter++;
        if (actionLockCounter == 60) { // Cat changes direction more frequently
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
            actionLockCounter = 0;
        }
    }
}
