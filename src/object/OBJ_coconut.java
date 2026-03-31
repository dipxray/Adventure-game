package object;

import java.io.IOException;
import javax.imageio.ImageIO;

public class OBJ_coconut extends SuperObject {

    public OBJ_coconut() {
        name = "Coconut";

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/coconut_object.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setDialogue();
    }

    public void setDialogue() {
        loadDialogue(
                "You crack \n open a coconut…\n",
                "Refreshing… you feel \n revitalized.\n",
                "Your stamina increases.\n");
    }

}