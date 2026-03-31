package object;

import java.io.IOException;
import javax.imageio.ImageIO;

public class OBJ_Diamond extends SuperObject {

    public OBJ_Diamond() {
        name = "Diamond";

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/diamond.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setDialogue();
    }

    public void setDialogue() {
        loadDialogue(
                "You found the magical Diamond!\n",
                "Its glowing energy fills the room.\n",
                "Congratulations! You win!\n"
        );
    }
}
