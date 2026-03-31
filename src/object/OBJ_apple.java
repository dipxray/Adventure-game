package object;

import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_apple extends SuperObject {

    public OBJ_apple() {
        name = "Apple";

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/apple_object.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setDialogue();
    }

    public void setDialogue() {
        loadDialogue("You picked up \n an apple.");
    }

    public void speak(GamePanel gp) {
        if (gp.player.questStage == 2) {
            loadDialogue("Player: \n This should work… \n hopefully the cat \n isn’t too far.");
            gp.player.questStage = 3; // Find the Wizard's Cat
        } else {
            setDialogue();
        }
        super.speak(gp);
    }
}