package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class OBJ_heart extends SuperObject{
    
    GamePanel gp;
	public OBJ_heart(GamePanel gp) {
        this.gp=gp;
		name="Heart";
		
		try {
			UtilityTool uTool = new UtilityTool();

			image = ImageIO.read(getClass().getResourceAsStream("/objects/heart_full.png"));
            image2 = ImageIO.read(getClass().getResourceAsStream("/objects/heart_half.png"));
           
            uTool.scaleImage(image,gp.tileSize,gp.tileSize);
			uTool.scaleImage(image2,gp.tileSize,gp.tileSize);
			
            
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
