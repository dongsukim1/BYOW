package byow.TileEngine;

import java.awt.Color;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    public static final TETile AVATAR = new TETile('@', Color.black, new Color(235, 223, 122), "you");
    public static final TETile AVATAR_HUG = new TETile('H', Color.black, new Color(235, 223, 122), "Josh Hug", "proj3/byow/TileEngine/hug.png");
    public static final TETile AVATAR_DENERO = new TETile('D', Color.black, new Color(235, 223, 122), "John DeNero", "proj3/byow/TileEngine/denero.png");

    public static final TETile WALL = new TETile('#', new Color(216, 128, 128), Color.darkGray,
            "wall");
    public static final TETile FLOOR = new TETile('·', new Color(128, 192, 128), Color.black,
            "floor");
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing");
    public static final TETile GRASS = new TETile('"', Color.white, new Color(96, 168, 101), "grass: KEEP OFF THE GRASS");
    public static final TETile WATER = new TETile('≈', Color.white, new Color(118, 189, 219), "water (you wake up trapped on an island)");
    public static final TETile FLOWER = new TETile('❀', Color.magenta, new Color(235, 223, 122), "flower: press <p> to pick");
    public static final TETile LOCKED_DOOR = new TETile('█', Color.red, new Color(235, 223, 122),
            "locked door: collect all the flowers to escape!");
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.red, new Color(235, 223, 122),
            "unlocked door: good job!");
    public static final TETile SAND = new TETile('▒', new Color(153, 145, 78), new Color(235, 223, 122), "sand");
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain");
    public static final TETile TREE = new TETile('♠', new Color(26, 97, 31), new Color(235, 223, 122), "tree");
}


