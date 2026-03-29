package main;

import object.OBJ_apple;
import object.OBJ_cherry;
import object.OBJ_coconut;
import object.OBJ_door;
import object.OBJ_key;
import object.OBJ_sword;
import entity.wizard;
import entity.NPC_King;
import entity.NPC_Cat;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {

        gp.obj[0] = new OBJ_apple();
        gp.obj[0].worldX = 7 * gp.tileSize;
        gp.obj[0].worldY = 28 * gp.tileSize;

        gp.obj[1] = new OBJ_sword();
        gp.obj[1].worldX = 34 * gp.tileSize;
        gp.obj[1].worldY = 8 * gp.tileSize;

        gp.obj[2] = new OBJ_cherry();
        gp.obj[2].worldX = 43 * gp.tileSize;
        gp.obj[2].worldY = 14 * gp.tileSize;

        gp.obj[3] = new OBJ_coconut();
        gp.obj[3].worldX = 1 * gp.tileSize;
        gp.obj[3].worldY = 44 * gp.tileSize;

        gp.obj[4] = new OBJ_key();
        gp.obj[4].worldX = 33 * gp.tileSize;
        gp.obj[4].worldY = 29 * gp.tileSize;

        gp.obj[5] = new OBJ_key();
        gp.obj[5].worldX = 6 * gp.tileSize;
        gp.obj[5].worldY = 13 * gp.tileSize;

        gp.obj[6] = new OBJ_key();
        gp.obj[6].worldX = 6 * gp.tileSize;
        gp.obj[6].worldY = 1 * gp.tileSize;

        gp.obj[7] = new OBJ_key();
        gp.obj[7].worldX = 49 * gp.tileSize;
        gp.obj[7].worldY = 46 * gp.tileSize;

        gp.obj[8] = new OBJ_door();
        gp.obj[8].worldX = 24 * gp.tileSize;
        gp.obj[8].worldY = 40 * gp.tileSize;

        gp.obj[9] = new OBJ_door();
        gp.obj[9].worldX = 5 * gp.tileSize;
        gp.obj[9].worldY = 39 * gp.tileSize;

        gp.obj[10] = new OBJ_door();
        gp.obj[10].worldX = 43 * gp.tileSize;
        gp.obj[10].worldY = 33 * gp.tileSize;

        gp.obj[11] = new OBJ_door();
        gp.obj[11].worldX = 36 * gp.tileSize;
        gp.obj[11].worldY = 8 * gp.tileSize;
    }

    public void setNpc() {
        gp.npc[0] = new wizard(gp);
        gp.npc[0].worldx = 23 * gp.tileSize;
        gp.npc[0].worldy = 44 * gp.tileSize;

        gp.npc[1] = new NPC_King(gp);
        gp.npc[1].worldx = 45 * gp.tileSize;
        gp.npc[1].worldy = 8 * gp.tileSize;

        gp.npc[2] = new NPC_Cat(gp);
        gp.npc[2].worldx = 48 * gp.tileSize;
        gp.npc[2].worldy = 39 * gp.tileSize;
    }
}