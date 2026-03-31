package tile;

import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import main.GamePanel;
import main.UtilityTool;

import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TileManager {

    GamePanel gp;

    public Tile[] tile;
    public int mapTileNum[][];
    public String currentMap = "mymap.txt";

    public TileManager(GamePanel gp) {
        this.gp = gp;

        tile = new Tile[100];
        mapTileNum = new int[gp.maxworldcol][gp.maxworldrow];

        getTileImage();
        loadMap("/res/tiles/mymap.txt");
    }

    public void getTileImage() {
        try {
            tile[0] = new Tile();
            tile[0].collision = true;
            tile[0].image = ImageIO.read(getClass().getResource("/res/tiles/lava01.png"));

            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResource("/res/tiles/temple01.png"));
            tile[1].collision = true;

            tile[2] = new Tile();
            tile[2].image = ImageIO.read(getClass().getResource("/res/tiles/skull01.png"));

            tile[3] = new Tile();
            tile[3].image = ImageIO.read(getClass().getResource("/res/tiles/castle.png"));
            tile[3].collision = true;

            tile[4] = new Tile();
            tile[4].image = ImageIO.read(getClass().getResource("/res/tiles/road.png"));

            tile[5] = new Tile();
            tile[5].collision = true;
            tile[5].image = ImageIO.read(getClass().getResource("/res/tiles/flametree01.png"));

            tile[6] = new Tile();
            tile[6].image = ImageIO.read(getClass().getResource("/res/tiles/coconut01.png"));
            tile[6].collision = true;

            tile[7] = new Tile();
            tile[7].image = ImageIO.read(getClass().getResource("/res/tiles/tile_road_1772650324566.png"));

            tile[8] = new Tile();
            tile[8].image = ImageIO.read(getClass().getResource("/res/tiles/bossroad.png"));

            tile[9] = new Tile();
            tile[9].image = ImageIO.read(getClass().getResource("/res/tiles/house.png"));
            tile[9].collision = true;

            tile[10] = new Tile();
            tile[10].image = ImageIO.read(getClass().getResource("/res/tiles/cottage01.png"));
            tile[10].collision = true;

            tile[11] = new Tile();
            tile[11].image = ImageIO.read(getClass().getResource("/res/tiles/cherry01.png"));
            tile[11].collision = true;

            tile[12] = new Tile();
            tile[12].image = ImageIO.read(getClass().getResource("/res/tiles/beach.png"));

            tile[13] = new Tile();
            tile[13].image = ImageIO.read(getClass().getResource("/res/tiles/grass01.png"));
            tile[13].collision = true;

            tile[14] = new Tile();
            tile[14].collision = true;
            tile[14].image = ImageIO.read(getClass().getResource("/res/tiles/jungle.png"));

            tile[15] = new Tile();
            tile[15].collision = true;
            tile[15].image = ImageIO.read(getClass().getResource("/res/tiles/flowerbed01.png"));

            tile[16] = new Tile();
            tile[16].collision = true;
            tile[16].image = ImageIO.read(getClass().getResource("/res/tiles/water01.png"));

            tile[17] = new Tile();
            tile[17].collision = true;
            tile[17].image = ImageIO.read(getClass().getResource("/res/tiles/apple.png"));

            tile[18] = new Tile();
            tile[18].collision = true;
            tile[18].image = ImageIO.read(getClass().getResource("/res/tiles/flower01.png"));

            tile[19] = new Tile();
            tile[19].collision = true;
            tile[19].image = ImageIO.read(getClass().getResource("/res/tiles/pine01.png"));

            tile[20] = new Tile();
            tile[20].image = ImageIO.read(getClass().getResource("/res/tiles/waves01.png"));
            tile[20].collision = true;

            tile[21] = new Tile();
            tile[21].image = ImageIO.read(getClass().getResource("/res/tiles/royalroad01.png"));

            tile[22] = new Tile();
            tile[22].image = ImageIO.read(getClass().getResource("/res/tiles/bcastle.png"));
            tile[22].collision = true;

            tile[23] = new Tile();
            tile[23].image = ImageIO.read(getClass().getResource("/res/tiles/036.png"));

            // MAP 2
            tile[32] = new Tile();
            tile[32].image = ImageIO.read(getClass().getResource("/res/tiles/032.png"));
            tile[32].collision = true;

            tile[57] = new Tile();
            tile[57].image = ImageIO.read(getClass().getResource("/res/tiles/017.png"));

            tile[37] = new Tile();
            tile[37].image = ImageIO.read(getClass().getResource("/res/tiles/037.png"));

            tile[39] = new Tile();
            tile[39].image = ImageIO.read(getClass().getResource("/res/tiles/039.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setup(int index, String imagePath, boolean collision) {
        UtilityTool uTool = new UtilityTool();
        try {
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResource("/res/tiles/" + imagePath + ".png"));
            tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
            tile[index].collision = collision;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMap(String mapPath) {
        try {
            InputStream is = getClass().getResourceAsStream(mapPath);
            if (is == null) {
                System.out.println("Map file not found: " + mapPath);
                return;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int row = 0;

            while (row < gp.maxworldrow) {

                String line = br.readLine();
                String numbers[] = line.split(" ");

                int col = 0;
                for (String number : numbers) {
                    if (!number.isEmpty()) {
                        mapTileNum[col][row] = Integer.parseInt(number);
                        col++;
                    }
                }

                row++;
            }

            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {

        int worldcol = 0;
        int worldrow = 0;

        while (worldcol < gp.maxworldcol && worldrow < gp.maxworldrow) {

            int tileNum = mapTileNum[worldcol][worldrow];

            int worldx = worldcol * gp.tileSize;
            int worldy = worldrow * gp.tileSize;

            int screenx = worldx - gp.player.getCamX();
            int screeny = worldy - gp.player.getCamY();

            if (tile[tileNum] != null && tile[tileNum].image != null) {
                g2.drawImage(tile[tileNum].image, screenx, screeny, gp.tileSize, gp.tileSize, null);
            }

            worldcol++;

            if (worldcol == gp.maxworldcol) {
                worldcol = 0;
                worldrow++;
            }
        }
    }
}