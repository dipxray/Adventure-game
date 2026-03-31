package object;

import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_cherry extends SuperObject {

    public OBJ_cherry() {
        name = "Cherry";

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/cherry_object.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setDialogue();
    }

    public void setDialogue() {
        loadDialogue("You picked up \n a cherry.");
    }

    public void speak(GamePanel gp) {
        if (gp.player.questStage == 5) {
            loadDialogue("Player: \n A cherry? \n That’s… oddly placed.",
                         "Player: \n Hmm… it \n might be useful.");
            gp.player.questStage = 6; // Take the Cherry to the King
        } else {
            setDialogue();
        }
        super.speak(gp);
    }
}