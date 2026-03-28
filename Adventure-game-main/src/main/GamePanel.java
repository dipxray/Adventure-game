package main;

import javax.swing.JPanel;

import entity.Player;
import object.SuperObject;
import tile.TileManager;

import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

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

    public FileHandler fileHandler = new FileHandler(this);

    // 🔥 NEW
    public FightPanel fight = new FightPanel(this);

    int FPS = 60;

    // STATES
    public int gameState;
    public final int mainMenuState = -1;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int fightState = 3;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);

        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void setupGame() {
        aSetter.setObject();
        fileHandler.loadData();

        music.setFile(7);
        music.play();
        music.loop();

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
            fileHandler.saveData();
        }

        if (gameState == fightState) {
            fight.update();
        }

        if (gameState == pauseState) {
            if (ambient.clip != null) ambient.stop();
        }
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (gameState == playState || gameState == pauseState) {

            tileM.draw(g2);

            for (int i = 0; i < obj.length; i++) {
                if (obj[i] != null) {
                    obj[i].draw(g2, this);
                }
            }

            player.draw(g2);
        }

        if (gameState == fightState) {
            fight.draw(g2);
        }

        ui.draw(g2);

        g2.dispose();
    }

    public void playSE(int i) {
        se.setFile(i);
        se.play();
    }
}