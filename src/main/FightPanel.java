package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class FightPanel {

    GamePanel gp;

    // 🎮 PLAYER
    int playerX = 100;
    int playerY = 300;
    int playerSpeed = 5;
    int playerHP = 3;

    boolean jumping = false;
    int jumpVelocity = 0;
    int gravity = 1;

    // ⚔ ATTACK
    boolean attacking = false;
    int attackTimer = 0;

    // 👹 BOSS
    int bossX = 500;
    int bossY = 300;
    int bossHP = 5;
    int bossSpeed = 2;

    int bossAttackTimer = 0;

    // 🎯 GAME STATE
    public boolean battleEnd = false;
    String result = "";

    public FightPanel(GamePanel gp){
        this.gp = gp;
    }

    // ================= UPDATE =================
    public void update(){

        if(battleEnd) return;

        // 🎮 PLAYER MOVEMENT
        if(gp.keyH.leftPressed) playerX -= playerSpeed;
        if(gp.keyH.rightPressed) playerX += playerSpeed;

        // 🪂 JUMP
        if(gp.keyH.upPressed && !jumping){
            jumping = true;
            jumpVelocity = -15;
        }

        if(jumping){
            playerY += jumpVelocity;
            jumpVelocity += gravity;

            if(playerY >= 300){
                playerY = 300;
                jumping = false;
            }
        }

        // ⚔ ATTACK TIMER
        if(attacking){
            attackTimer++;
            if(attackTimer > 15){
                attacking = false;
                attackTimer = 0;
            }
        }

        // 👹 BOSS AI (simple follow)
        if(playerX < bossX) bossX -= bossSpeed;
        else bossX += bossSpeed;

        // 👹 BOSS ATTACK
        bossAttackTimer++;
        if(bossAttackTimer > 60){
            bossAttackTimer = 0;

            if(getPlayerBounds().intersects(getBossBounds())){
                playerHP--;
            }
        }

        // ⚔ PLAYER HIT
        if(attacking){
            Rectangle attackBox = new Rectangle(playerX + 40, playerY, 40, 40);
            if(attackBox.intersects(getBossBounds())){
                bossHP--;
                attacking = false;
            }
        }

        // 🏁 END CONDITIONS
        if(playerHP <= 0){
            battleEnd = true;
            result = "YOU LOSE";

            gp.music.stop();
            gp.music.setFile(1); // lose sound
            gp.music.play();
        }

        if(bossHP <= 0){
            battleEnd = true;
            result = "YOU WIN";

            gp.music.stop();
            gp.music.setFile(2); // win sound
            gp.music.play();
        }
    }

    // ================= INPUT =================
    public void playerAttack(){
        if(!attacking){
            attacking = true;
        }
    }

    // ================= DRAW =================
    public void draw(Graphics2D g2){

        // BACKGROUND
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // ================= PLAYER =================
        // 🔥 REPLACE WITH PLAYER SPRITE
        g2.setColor(Color.BLUE);
        g2.fillRect(playerX, playerY, 40, 40);

        // ================= BOSS =================
        // 🔥 REPLACE WITH BOSS SPRITE
        g2.setColor(Color.RED);
        g2.fillRect(bossX, bossY, 50, 50);

        // ================= ATTACK =================
        if(attacking){
            g2.setColor(Color.YELLOW);
            g2.fillRect(playerX + 40, playerY, 40, 40);
        }

        // ================= HEALTH =================
        g2.setColor(Color.WHITE);
        g2.drawString("Player HP: " + playerHP, 50, 50);
        g2.drawString("Boss HP: " + bossHP, 400, 50);

        g2.drawString("A/D Move | W Jump | SPACE Attack", 150, 80);

        // ================= RESULT =================
        if(battleEnd){
            g2.setColor(Color.YELLOW);
            g2.drawString(result, 250, 200);
            g2.drawString("Press ENTER", 240, 250);
        }
    }

    // ================= COLLISION =================
    private Rectangle getPlayerBounds(){
        return new Rectangle(playerX, playerY, 40, 40);
    }

    private Rectangle getBossBounds(){
        return new Rectangle(bossX, bossY, 50, 50);
    }

    // ================= RESET =================
    public void resetBattle(){
        playerHP = 3;
        bossHP = 5;
        playerX = 100;
        bossX = 500;
        battleEnd = false;
        result = "";
    }
}