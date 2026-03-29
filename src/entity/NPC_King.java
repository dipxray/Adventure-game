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
            actionLockCounter = 0;
        }
    }
}
