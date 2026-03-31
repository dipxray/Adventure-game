package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import object.OBJ_heart;
import object.OBJ_key;
import object.SuperObject;

public class UI {

    GamePanel gp;
    Graphics2D g2;
    Font arial_40;

    BufferedImage keyImage;
    BufferedImage heart_full, heart_half;
    BufferedImage pixelHeart;

    // 🔥 MAIN MENU SELECTOR
    public int menuCommandNum = 0;

    // 🔥 MESSAGE SYSTEM
    String message = "";
    Color messageColor = Color.white;
    int messageCounter = 0;
    boolean messageOn = false;
    public String currentDialogue="";

    public UI(GamePanel gp) {
        this.gp = gp;

        try {
            Font arcadeFont = Font.createFont(Font.TRUETYPE_FONT,
                    getClass().getResourceAsStream("/res/font/PressStart2P-Regular.ttf"));
            arial_40 = arcadeFont.deriveFont(Font.PLAIN, 40f);

        } catch (Exception e) {
            e.printStackTrace();
        }

        OBJ_key key = new OBJ_key();
        keyImage = key.image;

        SuperObject heart = new OBJ_heart(gp);
        heart_full = heart.image;
        heart_half = heart.image2;

        try {
            pixelHeart = ImageIO.read(getClass().getResourceAsStream("/res/heart/fullheart.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;

        if (gp.gameState == gp.mainMenuState) {
            drawMainMenuScreen();
        } else if (gp.gameState == gp.titleState) {
            drawTitleScreen();

        } //Play state
        else if (gp.gameState == gp.playState) {
            drawPlayScreen();
        }//Pause State
         else if (gp.gameState == gp.pauseState) {
            drawPlayScreen();
            drawPauseScreen();
        }
         //Dialogue State
        else if (gp.gameState == gp.dialogueState) {
            drawPlayScreen();
            drawDialogueScreen();
        }
    }

    // =========================
    // 🔥 MAIN MENU SCREEN
    // =========================
    public void drawMainMenuScreen() {

        g2.setColor(Color.black);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setFont(arial_40.deriveFont(Font.BOLD, 48F));
        String text = "ADVENTURE RUN";
        int x = getXforcenteredText(text);
        int y = gp.tileSize * 3;

        g2.setColor(Color.gray);
        g2.drawString(text, x + 5, y + 5);

        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        g2.setFont(arial_40.deriveFont(Font.BOLD, 28F));

        // NEW GAME
        text = "NEW GAME";
        x = getXforcenteredText(text);
        y += gp.tileSize * 3;
        if (menuCommandNum == 0)
            g2.drawString(">", x - 40, y);
        g2.drawString(text, x, y);

        // LOAD
        text = "LOAD GAME";
        x = getXforcenteredText(text);
        y += gp.tileSize;
        if (menuCommandNum == 1)
            g2.drawString(">", x - 40, y);
        g2.drawString(text, x, y);

        // EXIT
        text = "EXIT";
        x = getXforcenteredText(text);
        y += gp.tileSize;
        if (menuCommandNum == 2)
            g2.drawString(">", x - 40, y);
        g2.drawString(text, x, y);
    }

    // =========================
    // 🎮 TITLE SCREEN
    // =========================
    public void drawTitleScreen() {
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setFont(arial_40.deriveFont(Font.BOLD, 48F));
        String text = "ADVENTURE RUN";
        int x = getXforcenteredText(text);
        int y = gp.tileSize * 3;

        g2.setColor(Color.gray);
        g2.drawString(text, x + 5, y + 5);
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        g2.setFont(arial_40.deriveFont(Font.PLAIN, 24F));

        text = "W: Move Up";
        x = getXforcenteredText(text);
        y += gp.tileSize * 2;
        g2.drawString(text, x, y);

        text = "S: Move Down";
        x = getXforcenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);

        text = "A: Move Left";
        x = getXforcenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);

        text = "D: Move Right";
        x = getXforcenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);

        g2.setFont(arial_40.deriveFont(Font.PLAIN, 18F));
        g2.setColor(Color.yellow);

        text = "Collect keys to open doors";
        x = getXforcenteredText(text);
        y += gp.tileSize * 1.5;
        g2.drawString(text, x, y);

        text = "Collect items for hearts";
        x = getXforcenteredText(text);
        y += gp.tileSize * 0.8;
        g2.drawString(text, x, y);

        g2.setFont(arial_40.deriveFont(Font.BOLD, 24F));
        g2.setColor(Color.white);
        text = "Press ENTER to Start";
        x = getXforcenteredText(text);
        y += gp.tileSize * 1.5;
        g2.drawString(text, x, y);
    }

    // =========================
    // 🎮 GAME PLAY UI
    // =========================
    public void drawPlayScreen() {

        int margin = 10;
        int keySize = gp.tileSize;

        // 🔑 Key
        g2.drawImage(keyImage, margin, margin, keySize, keySize, null);

        g2.setFont(arial_40.deriveFont(30f));
        g2.setColor(Color.YELLOW);

        int textX = margin + keySize + 10;
        int textY = margin + keySize - 10;

        g2.drawString("x " + gp.player.hasKey, textX, textY);

        // ❤️ Hearts
        if (gp.player.hasHearts > 0) {
            int heartY = margin + keySize + 5;
            g2.drawImage(pixelHeart, margin, heartY, keySize, keySize, null);

            int heartTextY = heartY + keySize - 10;
            g2.drawString("x " + gp.player.hasHearts, textX, heartTextY);
        }

        // 🔥 BOSS FIGHT UI REMOVED

        // 🔥 MESSAGE DISPLAY
        if (messageOn) {
            drawMessage();
        }

        // 🎯 Objective Box
        drawObjectiveBox();
    }

    // =========================
    // 🔥 MESSAGE METHODS
    // =========================
    public void showMessage(String text, Color color) {
        message = text;
        messageColor = color;
        messageOn = true;
        messageCounter = 0;
    }

    public void drawMessage() {

        g2.setFont(arial_40.deriveFont(Font.BOLD, 28F));

        int x = getXforcenteredText(message);
        int y = gp.screenHeight / 2 - 50;

        g2.setColor(Color.black);
        g2.drawString(message, x + 2, y + 2);

        g2.setColor(messageColor);
        g2.drawString(message, x, y);

        messageCounter++;
        if (messageCounter > 120) {
            messageOn = false;
        }
    }

    public void drawDialogueScreen() {
        // Window
        int width = gp.screenWidth - (gp.tileSize * 4);
        int height = gp.tileSize * 4;
        int x = (gp.screenWidth / 2) - (width / 2);
        int y = (gp.screenHeight / 2) - (height / 2);

        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 18F));
        x += gp.tileSize * 0.8;
        y += gp.tileSize * 1.2;

        for (String line : currentDialogue.split("\n")) {
            g2.drawString(line, x, y);
            y += 32;
        }
    }
    
    public void drawSubWindow(int x,int y,int width,int height){
        Color c=new Color(0,0,0,210);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);
        
        c=new Color(255,255,255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
    }
    public void drawPauseScreen() {

        String text = "PAUSED";
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 18F));

        int x = getXforcenteredText(text);
        int y = gp.screenHeight / 2;

        g2.setColor(Color.white);
        g2.drawString(text, x, y);
    }

    public void drawObjectiveBox() {
        String objective = "";
        switch (gp.player.questStage) {
            case 1: objective = "Explore the village\nand talk to the Wizard"; break;
            case 2: objective = "Find an Apple"; break;
            case 3: objective = "Find the\nWizard's Cat"; break;
            case 4: objective = "Return the Cat\nto the Wizard"; break;
            case 5: objective = "Go to the King"; break;
            case 6: objective = "Take the Cherry\nto the King"; break;
            case 7: objective = "Enter the Maze and\nfind the Diamond"; break;
        }

        if (!objective.isEmpty()) {
            g2.setFont(arial_40.deriveFont(Font.PLAIN, 10f)); // Smaller font
            String[] lines = ("👉 Obj: " + objective).split("\n");
            
            // Find max width
            int maxWidth = 0;
            for (String line : lines) {
                int w = g2.getFontMetrics().stringWidth(line);
                if (w > maxWidth) maxWidth = w;
            }

            int x = gp.screenWidth - maxWidth - 20;
            int y = 30;

            // Box shadow
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRoundRect(x - 10, y - 15, maxWidth + 20, (lines.length * 15) + 5, 10, 10);

            g2.setColor(Color.WHITE);
            for (int i = 0; i < lines.length; i++) {
                g2.drawString(lines[i], x, y + (i * 15));
            }
        }
    }

    // =========================
    public int getXforcenteredText(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth / 2 - length / 2;
    }

    // =========================
    // 🔹 RESET UI FOR MAIN MENU / TITLE
    // =========================
    public void resetUI() {
        message = "";
        messageOn = false;
        messageCounter = 0;
        menuCommandNum = 0;
    }
}