package object;

import java.io.IOException;
import javax.imageio.ImageIO;

import entity.Projectile;
import main.GamePanel;

public class OBJ_Fireball extends Projectile {

    public OBJ_Fireball(GamePanel gp) {
        super(gp);

        name = "Fireball";
        speed = 5;
        maxLife = 80; // How far it flies before disappearing
        life = maxLife;
        attack = 1; // Damage dealt
        alive = false;

        // Let's adjust solid area for fireball
        solidArea.x = 12;
        solidArea.y = 12;
        solidArea.width = 24;
        solidArea.height = 24;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    public void getImage() {
        try {
            up1 = ImageIO.read(getClass().getResourceAsStream("/res/projectile/fireball_up_1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/res/projectile/fireball_up_2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/res/projectile/fireball_down_1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/res/projectile/fireball_down_2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/res/projectile/fireball_left_1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/res/projectile/fireball_left_2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/res/projectile/fireball_right_1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/res/projectile/fireball_right_2.png"));

            // set up3 and down3 etc to prevent null pointer in drawing if animation expects
            // 3 frames.
            up3 = up2;
            down3 = down2;
            left3 = left2;
            right3 = right2;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
