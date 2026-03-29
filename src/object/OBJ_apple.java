package object;

import java.io.IOException;
import javax.imageio.ImageIO;

public class OBJ_apple extends SuperObject {

    public OBJ_apple() {
        name = "Apple";

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/apple_object.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}