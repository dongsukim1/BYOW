package byow.WorldGenerator;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;

public class Interactivity {
    private WorldGenerator wg;
    private Integer[][] worldBacking;
    public TETile[][] world;
    private int currX;
    private int currY;
    private boolean worldLit;
    public boolean gameWon;
    private TERenderer ter;
    private int coins;
    private int delayMovement = 25;


    public Interactivity(long seed, TETile avatar) {
        wg = new WorldGenerator(50, 49, 0.27, seed);
        worldBacking = wg.getWorldBacking();
        world = wg.getWorld();
        worldLit = true;
        gameWon = false;
        coins = wg.num_coins;

        ter = new TERenderer();
        ter.initialize(50, 50);
        ter.renderFrame(wg.getWorld());
        CharacterSpawn(avatar);

        }

    public void startHUD(TETile avatar) {
        int mouseX = (int) StdDraw.mouseX();
        int mouseY = (int) StdDraw.mouseY();
        if (mouseY < 49) {
            StdDraw.setPenColor(Color.BLACK);
            StdDraw.filledRectangle(25.0, 49.5, 25.0, 0.5);
            StdDraw.setPenColor(Color.WHITE);
            Font fontSmall = new Font("Monaco", Font.BOLD, 15);
            StdDraw.setFont(fontSmall);
            String description = world[mouseX][mouseY].description();
            if (mouseX == currX && mouseY == currY) {
                description = avatar.description();
            }
            StdDraw.textLeft(2.0, 49.5, description);
            StdDraw.show();
        }
    }

    private void displayDiamond(int x, int y, TETile avatar) {
        for (int i = -3; i < 4; i++) {
            if (i == 0) {
                avatar.draw(x, y);
            } else {
                if (withinBounds(x+i, y)) {
                    world[x+i][y].draw(x+i, y);
                }
            }
        }
        for (int i = -2; i < 3; i++) {
            if (withinBounds(x+i, y+1)) {
                world[x+i][y+1].draw(x+i, y+1);
            }
            if (withinBounds(x+i, y-1)) {
                world[x+i][y-1].draw(x+i, y-1);
            }
        }
        for (int i = -1; i < 2; i++) {
            if (withinBounds(x+i, y+2)) {
                world[x+i][y+2].draw(x+i, y+2);
            }
            if (withinBounds(x+i, y-2)) {
                world[x+i][y-2].draw(x+i, y-2);
            }
        }
        if (withinBounds(x, y+3)) {
            world[x][y+3].draw(x, y+3);
        }
        if (withinBounds(x, y-3)) {
            world[x][y-3].draw(x, y-3);
        }
    }

    /** Helper: returns true if x,y pair is within bounds. */
    private boolean withinBounds(int x, int y) {
        if (x >= 0 && x < 50 && y >= 0 && y < 49) {
            return true;
        } else {
            return false;
        }
    }
    public void processMovement(char c, TETile avatar) {
        if (Character.toLowerCase(c) == 'g') {
            if (!worldLit) {
                ter.renderFrame(wg.getWorld());
                avatar.draw(currX, currY);
                StdDraw.show();
                worldLit = !worldLit;
            } else {
                StdDraw.clear(StdDraw.BLACK);
                displayDiamond(currX, currY, avatar);
                worldLit = !worldLit;
                StdDraw.show();
            }
        }
        if (Character.toLowerCase(c) == 'p') {
            if (worldBacking[currX + 1][currY] == 4) {
                world[currX + 1][currY] = wg.floorTile;
                world[currX + 1][currY].draw(currX + 1, currY);
                worldBacking[currX + 1][currY] = 1;
                coins--;
            }
            if (worldBacking[currX][currY - 1] == 4) {
                world[currX][currY - 1] = wg.floorTile;
                worldBacking[currX][currY - 1] = 1;
                world[currX][currY - 1].draw(currX, currY - 1);
                coins--;
            }
            if (worldBacking[currX - 1][currY] == 4) {
                world[currX - 1][currY] = wg.floorTile;
                worldBacking[currX - 1][currY] = 1;
                world[currX - 1][currY].draw(currX - 1, currY);
                coins--;
            }
            if (worldBacking[currX][currY + 1] == 4) {
                world[currX][currY + 1] = wg.floorTile;
                worldBacking[currX][currY + 1] = 1;
                world[currX][currY + 1].draw(currX, currY + 1);
                coins--;
            }
            //unlockDoor();
        }
        if (Character.toLowerCase(c) == 'w') {
            if (worldBacking[currX][currY + 1] == 5) {winGame();}
            if (worldBacking[currX][currY + 1] == 1 || worldBacking[currX][currY + 1] == 2) {
                world[currX][currY].draw(currX, currY);
                currY++;
                if (!worldLit) {
                    StdDraw.clear(StdDraw.BLACK);
                    displayDiamond(currX, currY, avatar);
                }
                avatar.draw(currX, currY);
                StdDraw.show();
                StdDraw.pause(delayMovement);
            }
        }
        if (Character.toLowerCase(c) == 'a') {
            if (worldBacking[currX - 1][currY] == 5) {winGame();}
            if (worldBacking[currX - 1][currY] == 1 || worldBacking[currX - 1][currY] == 2) {
                world[currX][currY].draw(currX, currY);
                currX--;
                if (!worldLit) {
                    StdDraw.clear(StdDraw.BLACK);
                    displayDiamond(currX, currY, avatar);
                }
                avatar.draw(currX, currY);
                StdDraw.show();
                StdDraw.pause(delayMovement);
            }
        }
        if (Character.toLowerCase(c) == 's') {
            if (worldBacking[currX][currY - 1] == 5) {winGame();}
            if (worldBacking[currX][currY - 1] == 1 || worldBacking[currX][currY - 1] == 2) {
                world[currX][currY].draw(currX, currY);
                currY--;
                if (!worldLit) {
                    StdDraw.clear(StdDraw.BLACK);
                    displayDiamond(currX, currY, avatar);
                }
                avatar.draw(currX, currY);
                StdDraw.show();
                StdDraw.pause(delayMovement);
            }
        }
        if (Character.toLowerCase(c) == 'd') {
            if (worldBacking[currX + 1][currY] == 5) {winGame();}
            if (worldBacking[currX + 1][currY] == 1 || worldBacking[currX + 1][currY] == 2) {
                world[currX][currY].draw(currX, currY);
                currX++;
                if (!worldLit) {
                    StdDraw.clear(StdDraw.BLACK);
                    displayDiamond(currX, currY, avatar);
                }
                avatar.draw(currX, currY);
                StdDraw.show();
                StdDraw.pause(delayMovement);
            }
        }
    }
    public void CharacterSpawn(TETile avatar) {
        currX = wg.getRooms().get(0).getCentreCoord().get(0);
        currY = wg.getRooms().get(0).getCentreCoord().get(1);
        avatar.draw(currX, currY);
        StdDraw.show();
    }

    public void winGame() {
        gameWon = true;
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 35);
        StdDraw.setFont(fontBig);
        StdDraw.text(50 / 2, 49 / 2, "freeeeedom");
        StdDraw.show();
    }

    public void unlockDoor() {
        if (coins == 0) {
            world[wg.doorCoords.get(0)][wg.doorCoords.get(1)] = wg.unlockedDoorTile;
            worldBacking[wg.doorCoords.get(0)][wg.doorCoords.get(1)] = 5;
            world[wg.doorCoords.get(0)][wg.doorCoords.get(1)].draw(wg.doorCoords.get(0), wg.doorCoords.get(1));
        }
    }
}
