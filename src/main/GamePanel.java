package main;

import javax.swing.JPanel;

import entity.Player;
import entity.Entity;
import object.SuperObject;
import tile.TileManager;

import java.util.ArrayList;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.MultipleGradientPaint.CycleMethod;

public class GamePanel extends JPanel implements Runnable {

    final int originalTileSize = 16;
    final int scale = 3;
    public final int tileSize = originalTileSize * scale;

    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    public final int maxworldcol = 50;
    public final int maxworldrow = 50;
    public final int worldwidth = tileSize * maxworldcol;
    public final int worldheight = tileSize * maxworldrow;

    Thread gameThread;

    public Sound se = new Sound();
    public Sound doorSE = new Sound();
    public Sound ambient = new Sound();
    public Sound music = new Sound();

    public TileManager tileM = new TileManager(this);
    public keyhandler keyH = new keyhandler(this);
    public CollisionChecker cChecker = new CollisionChecker(this);
    public Player player = new Player(this, keyH);

    public SuperObject obj[] = new SuperObject[20];
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);

    public Entity[] npc = new Entity[10];
    public Entity activeNPC = null;
    public SuperObject activeObj = null;
    public ArrayList<Entity> projectileList = new ArrayList<>();

    public FileHandler fileHandler = new FileHandler(this);

    int FPS = 60;

    // STATES
    public int gameState;
    public final int mainMenuState = -1;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState=4;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);

        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void setupGame() {
        aSetter.setObject();
        aSetter.setNpc();   
        fileHandler.loadData();

        gameState = mainMenuState;
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double drawInterval = 1000000000 / FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {

            update();
            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime /= 1000000;

                if (remainingTime < 0) remainingTime = 0;

                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {

        if (gameState == playState) {
            player.update();
            for(int i=0;i<npc.length;i++){
                if(npc[i]!=null){
                    npc[i].update();
                }
            }
            
            for(int i = 0; i < projectileList.size(); i++) {
                if(projectileList.get(i) != null) {
                    if (projectileList.get(i).alive) {
                        projectileList.get(i).update();
                    }
                    if (!projectileList.get(i).alive) {
                        projectileList.remove(i);
                    }
                }
            }
        }

        if (gameState == pauseState) {
            if (ambient.clip != null) ambient.stop();
        }
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (gameState == playState || gameState == pauseState|| gameState == dialogueState) {

            tileM.draw(g2);

            for (int i = 0; i < obj.length; i++) {
                if (obj[i] != null) {
                    obj[i].draw(g2, this);
                }
            }

            player.draw(g2);

            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null) {
                    npc[i].draw(g2);
                }
            }
            
            for (int i = 0; i < projectileList.size(); i++) {
                if(projectileList.get(i) != null) {
                    projectileList.get(i).draw(g2);
                }
            }

            // 💡 Map Diagnostic (Subtle)
            g2.setColor(new Color(255, 255, 255, 100));
            g2.setFont(g2.getFont().deriveFont(12f));
            g2.drawString("Map: " + tileM.currentMap, 10, 20);

            if (tileM.currentMap.toLowerCase().contains("map2")) {
                drawDarkness(g2);
            }
        }

        ui.draw(g2);

        g2.dispose();
    }

    private void drawDarkness(Graphics2D g2) {
        // Create a black overlay
        int centerX = player.worldx - player.getCamX() + (tileSize / 2);
        int centerY = player.worldy - player.getCamY() + (tileSize / 2);

        // Gradient for light
        float[] fractions = {0f, 1f};
        Color[] colors = {new Color(0, 0, 0, 0f), new Color(0, 0, 0, 255)}; // 100% Pitch Black

        // Radius of light (around 4 tiles)
        int lightRadius = tileSize * 4;

        RadialGradientPaint rgp = new RadialGradientPaint(centerX, centerY, lightRadius, fractions, colors, CycleMethod.NO_CYCLE);
        g2.setPaint(rgp);
        g2.fillRect(0, 0, screenWidth, screenHeight);
    }

    public void playSE(int i) {
        se.setFile(i);
        se.play();
    }

    public void switchMap(String mapPath, int nextX, int nextY) {
        tileM.loadMap("/res/tiles/" + mapPath);
        tileM.currentMap = mapPath;

        // 🔥 Refresh assets for the new map
        aSetter.setObject();
        aSetter.setNpc();
        fileHandler.applyRemovals(); // 🔥 Restore previous progress on this map

        player.worldx = nextX;
        player.worldy = nextY;
        player.collisionOn = false; // 🔥 IMPORTANT: prevent getting stuck on arrival

        fileHandler.saveData(); // 🔥 AUTO-SAVE on map switch

        if (mapPath.equals("Map2.txt")) {
            ui.showMessage("Welcome to the Temple!", java.awt.Color.CYAN);
        }
    }
}