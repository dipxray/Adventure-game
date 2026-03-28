package object;

import java.io.IOException;

import javax.imageio.ImageIO;

public class OBJ_sword extends SuperObject{
    
	public OBJ_sword() {
		name="Sword";
		
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/objects/sword_object.png"));
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
