package entity;

import java.awt.Rectangle;       
import java.awt.image.BufferedImage;

public class Entity {

    public int worldx, worldy;
    public int speed;

    public BufferedImage up, down, right, left;

    public String direction;

    public Rectangle solidArea;     // collision box
    public int solidAreaDefaultX,solidAreaDefaultY;
    public boolean collisionOn = false;

    //Character status
    public int maxLife;
    public int life;

    public int spriteCounter = 0;
    public int spriteNum = 1;

}