package object;

import java.io.IOException;
import javax.imageio.ImageIO;

public class OBJ_cherry extends SuperObject {

    public OBJ_cherry() {
        name = "Cherry";

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/cherry_object.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}