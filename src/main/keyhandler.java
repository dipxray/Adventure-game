package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class keyhandler implements KeyListener {

    GamePanel gp;

    public boolean upPressed, downPressed, leftPressed, rightPressed, shiftPressed, enterPressed, jPressed;

    public keyhandler(GamePanel gp){
        this.gp = gp;
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();

        //Play State
        if(gp.gameState==gp.playState){
            if (code == KeyEvent.VK_W) upPressed = true;
            if (code == KeyEvent.VK_S) downPressed = true;
            if (code == KeyEvent.VK_A) leftPressed = true;
            if (code == KeyEvent.VK_D) rightPressed = true;
            if (code == KeyEvent.VK_SHIFT) shiftPressed = true;
            if (code == KeyEvent.VK_ENTER) enterPressed = true;
            if (code == KeyEvent.VK_J) jPressed = true;
        }

        //Dialogue State
        else if(gp.gameState==gp.dialogueState){
            if(code==KeyEvent.VK_ENTER){
                if (gp.activeNPC != null) {
                    if (gp.activeNPC.dialogues[gp.activeNPC.dialogueIndex] == null) {
                        gp.gameState = gp.playState;
                        gp.activeNPC.dialogueIndex = 0;
                        gp.activeNPC = null;
                    } else {
                        gp.activeNPC.speak();
                    }
                } else if (gp.activeObj != null) {
                    if (gp.activeObj.dialogues[gp.activeObj.dialogueIndex] == null) {
                        gp.gameState = gp.playState;
                        gp.activeObj.dialogueIndex = 0;
                        gp.activeObj = null;
                    } else {
                        gp.activeObj.speak(gp);
                    }
                } else {
                    gp.gameState = gp.playState;
                }
            }
        }


        // 🔥 MAIN MENU (UPDATED LOGIC)
        if (gp.gameState == gp.mainMenuState) {

            if(code == KeyEvent.VK_W){
                gp.ui.menuCommandNum--;
                if(gp.ui.menuCommandNum < 0) gp.ui.menuCommandNum = 2;
            }

            if(code == KeyEvent.VK_S){
                gp.ui.menuCommandNum++;
                if(gp.ui.menuCommandNum > 2) gp.ui.menuCommandNum = 0;
            }

            if(code == KeyEvent.VK_ENTER){

                // ✅ NEW GAME
                if(gp.ui.menuCommandNum == 0){

                    gp.fileHandler.clearData();   // 🔥 clear old save
                    gp.aSetter.setObject();      // 🔥 reset objects
                    gp.aSetter.setNpc();         // 🔥 reset NPCs
                    gp.player.setDefaultValues();// 🔥 reset player

                    gp.gameState = gp.titleState;
                }

                // ✅ LOAD GAME
                if(gp.ui.menuCommandNum == 1){

                    gp.aSetter.setObject();      // load all objects first
                    gp.aSetter.setNpc();         // load all NPCs first
                    gp.fileHandler.loadData();   // then remove collected ones

                    gp.gameState = gp.playState; // 🔥 Jump straight into the game

                    // Start music
                    gp.music.stop();
                    gp.music.setFile(6);
                    gp.music.play();
                    gp.music.loop();
                }

                // ✅ EXIT
                if(gp.ui.menuCommandNum == 2){
                    gp.fileHandler.clearData();   // 🔥 Wipe data on exit
                    System.exit(0);
                }
            }

            return;
        }

        // 🔥 TITLE SCREEN (UNCHANGED)
        if (gp.gameState == gp.titleState) {
            if (code == KeyEvent.VK_ENTER) {
                gp.gameState = gp.playState;

                gp.music.stop();
                gp.music.setFile(6);
                gp.music.play();
                gp.music.loop();
            }
            return;
        }

        // 🔥 GAME CONTROLS (UNCHANGED)
        if (code == KeyEvent.VK_W) upPressed = true;
        if (code == KeyEvent.VK_S) downPressed = true;
        if (code == KeyEvent.VK_A) leftPressed = true;
        if (code == KeyEvent.VK_D) rightPressed = true;
        if (code == KeyEvent.VK_SHIFT) shiftPressed = true;

        if (code == KeyEvent.VK_P) {
            if (gp.gameState == gp.playState) {
                gp.gameState = gp.pauseState;
                gp.music.stop();
            } else if (gp.gameState == gp.pauseState) {
                gp.gameState = gp.playState;
                gp.music.play();
                gp.music.loop();
            }
        }

        // 🔥 BACK TO MENU (CLEAR DATA)
        if (code == KeyEvent.VK_Q) {
            gp.fileHandler.saveData();   // 🔥 Save progress when returning to menu
            gp.gameState = gp.mainMenuState;

            gp.music.stop();
            gp.music.setFile(7);
            gp.music.play();
            gp.music.loop();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) upPressed = false;
        if (code == KeyEvent.VK_S) downPressed = false;
        if (code == KeyEvent.VK_A) leftPressed = false;
        if (code == KeyEvent.VK_D) rightPressed = false;
        if (code == KeyEvent.VK_SHIFT) shiftPressed = false;
        if (code == KeyEvent.VK_ENTER) enterPressed = false;
        if (code == KeyEvent.VK_J) jPressed = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}