package entity;

import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import main.GamePanel;

public class wizard extends Entity {

    public wizard(GamePanel gp) {
        super(gp);
        direction = "down";
        speed = 2;

        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getplayerimage();
        setDialogue();
    }

    public void getplayerimage() {
        try {

            left1 = ImageIO.read(getClass().getResourceAsStream("/res/npc/wizard_left.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/res/npc/wizard_left1.png"));
            left3 = ImageIO.read(getClass().getResourceAsStream("/res/npc/wizard_left2.png"));

            right1 = ImageIO.read(getClass().getResourceAsStream("/res/npc/wizard_right.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/res/npc/wizard_right1.png"));
            right3 = ImageIO.read(getClass().getResourceAsStream("/res/npc/wizard_right2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setDialogue() {
        if (gp.player.questStage == 1) {
            loadDialogue("Wizard: \n Halt! The road ahead\n is not for you.",
                         "You: \n I don’t have time for this. \n I need to pass.",
                         "Wizard: \n No one passes without\n my permission.",
                         "Wizard: \n My cat has run away… \n I am too old to chase it.",
                         "You: \n …That’s your problem, \n not mine.",
                         "Wizard (firm): \n It is now yours. Find my cat… \n and bring it back safely.",
                         "You (sighs): \n …Fine. \n Where do I start?",
                         "Wizard: \n He loves apples. \n Bring one or he won’t come.");
        } else if (gp.player.questStage == 4 && gp.player.hasCat) {
            loadDialogue("Wizard: \n My cat! You actually \n found him…",
                         "You: \n Told you I would. \n Now let me pass.",
                         "Wizard: \n You have my gratitude.",
                         "Wizard: \n The path ahead is dangerous… \n I won’t stop you.",
                         "You: \n Finally.",
                         "Wizard: \n If you seek something valuable… \n speak to the King.",
                         "Wizard: \n He knows more about this land \n than anyone.");
        } else if (gp.player.questStage == 2 || gp.player.questStage == 3) {
            loadDialogue("Wizard: \n Have you found my \n cat Milo yet?",
                         "Wizard: \n He loves apples!");
        } else {
             loadDialogue("Wizard: \n Be careful \n on your journey.");
        }
    }

    public void setAction() {
        if (gp.player.hasCat) {
            speed = 0; // stop moving when waiting for player to finish talking
        }
        actionLockCounter++;
        if (actionLockCounter == 120) {
            Random random = new Random();
            int i = random.nextInt(50) + 1;
            if (i <= 25) {
                direction = "left";
            } else {
                direction = "right";
            }
            actionLockCounter = 0;
        }
    }

    public void speak() {
        setDialogue();
        super.speak();
        
        if (gp.player.questStage == 1 && dialogues[dialogueIndex] == null) {
            gp.player.questStage = 2; // Transition to 'Find an Apple'
        } else if (gp.player.questStage == 4 && gp.player.hasCat && dialogues[dialogueIndex] == null) {
            gp.player.questStage = 5; // Transition to 'Go to the King'
            gp.player.hasCat = false; // "Given" to wizard
            
            // 🔥 Wizard vanishes
            for (int i = 0; i < gp.npc.length; i++) {
                if (gp.npc[i] == this) {
                    gp.fileHandler.removedNPCs.add(gp.tileM.currentMap + ":" + i); // 🔥 PERSIST REMOVAL
                    gp.npc[i] = null;
                    break;
                }
            }
        }
        gp.fileHandler.saveData(); // 🔥 Save progress
    }

}
