package byow.WorldGenerator;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.abs;

public class WorldGenerator {
    // WorldGenerator variables
    private TETile[][] world;
    private Integer[][] worldBacking;
    private Random r;
    private int worldWidth;
    private int worldHeight;
    private double totalTiles;
    private int filledTiles;
    private double desiredRatio;
    private ArrayList<Room> rooms;
    private ArrayList<Hallway> hallways;
    public TETile floorTile = Tileset.SAND;
    public TETile wallTile = Tileset.GRASS;
    public TETile emptyTile = Tileset.WATER;
    public TETile lockedDoorTile = Tileset.LOCKED_DOOR;
    public TETile unlockedDoorTile = Tileset.UNLOCKED_DOOR;
    public TETile coinTile = Tileset.FLOWER;
    public final int num_coins = 8;
    public ArrayList<Integer> doorCoords;

    /** Nested class: Room. */
    public class Room {
        // Room variables
        private ArrayList<Integer> centreCoord;
        private ArrayList<ArrayList<Integer>> wallCoords;
        private Integer[][] floorCoords;

        /**
         * Constructor: constructs a room. Finds enough free space and "places" it by updating "world", "worldBacking",
         * "filledTiles" and "rooms".
         */
        private Room() {
            // Initializing instance variable
            centreCoord = new ArrayList<>();

            boolean spaceFound = false;
            // Try random x,y start points until an empty start point is found.
            while (!spaceFound) {
                int randomX = r.nextInt(38) + 1;
                // Leaving space at the top for HUD.
                int randomY = r.nextInt(37) + 1;
                if (worldBacking[randomX][randomY] == 0) {
                    int roomWidth = r.nextInt(8) + 5;
                    int roomHeight = r.nextInt(8) + 5;
                    int distance = 1;
                    // Checks for x, y, width, height and distance.
                    ArrayList<ArrayList<Integer>> coordinates = checkForSpace(randomX, randomY, roomWidth, roomHeight);

                    // If x, y, width, height combo found: place the room.
                    if (!coordinates.isEmpty() && checkPerimeter(randomX, randomY, roomWidth, roomHeight, distance)) {
                        placeRoom(randomX, randomY, roomWidth, roomHeight, coordinates);
                        // Updating Room instance variables
                        centreCoord.add(randomX + roomWidth / 2);
                        centreCoord.add(randomY + roomHeight / 2);
                        wallCoords = wallCoordinates(randomX, randomY, roomWidth, roomHeight);
                        spaceFound = true;
                    }
                }
            }
        }

        /** Room helper: checks top, bottom, left, right (?) perimeter for collisions within some given
         * range distance. */
        private boolean checkPerimeter(int x, int y, int width, int height, int distance) {
            // Check top and bottom perimeter
            for (int i = x + width - 1 + distance; i >= x - distance; i--) {
                if (i >= 0 && i < worldWidth && y >= 0 && y < worldHeight) {
                    if (worldBacking[i][y] != 0) {
                        return false;
                    }
                }
                if (i >= 0 && i < worldWidth && y + height - 1 + 2 * distance >= 0 && y + height - 1 + 2 * distance < worldHeight) {
                    if (worldBacking[i][y + height - 1 + 2 * distance] != 0) {
                        return false;
                    }
                }
                // Check for rooms in the column directly above
                if (i >= 0 && i < worldWidth && y + height + 2 * distance >= 0 && y + height + 2 * distance < worldHeight) {
                    for (int j = y + height + distance; j < y + height + 2 * distance; j++) {
                        if (worldBacking[i][j] != 0) {
                            return false;
                        }
                    }
                }
                // Check for rooms in the column directly below
                if (i >= 0 && i < worldWidth && y - 1 - 2 * distance >= 0 && y - 1 - 2 * distance < worldHeight) {
                    for (int j = y - 1 - distance; j >= y - 1 - 2 * distance; j--) {
                        if (worldBacking[i][j] != 0) {
                            return false;
                        }
                    }
                }
            }
            for (int j = y + height - 1 + distance; j >= y + distance; j--) {
                if (x - distance >= 0 && x - distance < worldWidth && j >= 0 && j < worldHeight) {
                    if (worldBacking[x - distance][j] != 0) {
                        return false;
                    }
                }
                if (x + width - 1 + distance >= 0 && x + width - 1 + distance < worldWidth && j >= 0 && j < worldHeight) {
                    if (worldBacking[x + width - 1 + distance][j] != 0) {
                        return false;
                    }
                }
            }
            return true;
        }

        /**
         * Room helper: checks if there is space starting at x,y for a rectangle of width, height.
         * Returns list of all coordinates if yes, or empty list if not.
         */
        private ArrayList<ArrayList<Integer>> checkForSpace(int startX, int startY, int width, int height) {
            ArrayList<ArrayList<Integer>> coordinates = new ArrayList<>();
            for (int x = startX; x < width + startX; x++) {
                for (int y = startY; y < height + startY; y++) {
                    if (worldBacking[x][y] != 0) {
                        return new ArrayList<>();
                    }
                    if (x < width + startX && y < height + startY) {
                        ArrayList<Integer> xyPair = new ArrayList<>();
                        xyPair.add(x);
                        xyPair.add(y);
                        coordinates.add(xyPair);
                    }
                }
            }
            return coordinates;
        }

        /**
         * Room helper: places room by updating world & worldBacking arrays in WorldGenerator class. Places
         * both floor and wall tiles. Updates filledTiles variable in WorldGenerator class.
         */
        private void placeRoom(int startX, int startY, int width, int height, ArrayList<ArrayList<Integer>> coordinates) {
            for (ArrayList<Integer> xyPair : coordinates) {
                world[xyPair.get(0)][xyPair.get(1)] = floorTile;
                worldBacking[xyPair.get(0)][xyPair.get(1)] = 1;
            }
            filledTiles += height * width;
        }

        /** Gets a room's centre coordinates. */
        public ArrayList<Integer> getCentreCoord() {
            return centreCoord;
        }

        /** Room helper: returns a room's perimeter coordinates. */
        private ArrayList<ArrayList<Integer>> wallCoordinates(int startX, int startY, int width, int height) {
            ArrayList<ArrayList<Integer>> wallCoords = new ArrayList<>();
            for (int x = startX; x < startX + width; x++) {
                // bottom wall
                ArrayList<Integer> coord = new ArrayList<>();
                coord.add(x); coord.add(startY);
                wallCoords.add(coord);

                // top wall
                ArrayList<Integer> coordB = new ArrayList<>();
                coordB.add(x); coordB.add(startY + height - 1);
                wallCoords.add(coordB);
            }

            for (int y = startY; y < startY + height; y++) {
                // left wall
                ArrayList<Integer> coord = new ArrayList<>();
                coord.add(startX); coord.add(y);
                wallCoords.add(coord);

                // right wall
                ArrayList<Integer> coordB = new ArrayList<>();
                coordB.add(startX + width - 1); coordB.add(y);
                wallCoords.add(coordB);
            }
            return wallCoords;
        }
    }

    /** Nested class: Hallway. */
    private class Hallway {
        // Class variables here
        private ArrayList<ArrayList<Integer>> vertPathCoords;
        private ArrayList<ArrayList<Integer>> horizPathCoords;
        private ArrayList<ArrayList<Integer>> vertPathWallCoords; // two extra coords along vert path, used to build walls at corners.

        /** Constructor: constructs a hallway between two given x,y points. Updates world, worldBacking,
         * and stores direction and coordinates of each hallway. Note that technically one hallway is made
         * up of one vertical and one horizontal path. */
        private Hallway(ArrayList<Integer> startXY, ArrayList<Integer> endXY) {
            // Initializing instance variables
            vertPathCoords = new ArrayList<>();
            horizPathCoords = new ArrayList<>();
            vertPathWallCoords = new ArrayList<>();

            int startX = startXY.get(0); int startY = startXY.get(1);
            int endX = endXY.get(0); int endY = endXY.get(1);
            int xDiff = endX - startX;
            int yDiff = endY - startY;

            // If xDiff is negative, move left; else move right. **Starting at sourceXY.**
            if (xDiff < 0) {
                for (int i = 1; i < abs(xDiff) + 1; i++) {
                    if (worldBacking[startX - i][startY] == 0 || worldBacking[startX - i][startY] == 3) {
                        worldBacking[startX - i][startY] = 2;
                        world[startX - i][startY] = floorTile;

                        ArrayList<Integer> coordPair = new ArrayList<>();
                        coordPair.add(startX - i); coordPair.add(startY);
                        horizPathCoords.add(coordPair);
                    }
                }
            } else {
                for (int i = 1; i < xDiff + 1; i++) {
                    if (worldBacking[startX + i][startY] == 0 || worldBacking[startX + i][startY] == 3) {
                        worldBacking[startX + i][startY] = 2;
                        world[startX + i][startY] = floorTile;

                        ArrayList<Integer> coordPair = new ArrayList<>();
                        coordPair.add(startX + i); coordPair.add(startY);
                        horizPathCoords.add(coordPair);
                    }
                }
            }
            // If yDiff is negative, move up; else move down. **Starting at endXY.**
            if (yDiff < 0) {
                for (int j = 1; j < abs(yDiff) + 1; j++) {
                    if (worldBacking[endX][endY + j] == 0 || worldBacking[endX][endY + j] == 3) {
                        worldBacking[endX][endY + j] = 2;
                        world[endX][endY + j] = floorTile;

                        ArrayList<Integer> coordPair = new ArrayList<>();
                        coordPair.add(endX); coordPair.add(endY + j);
                        vertPathCoords.add(coordPair);
                    }
                }
                for (int j = abs(yDiff); j < abs(yDiff) + 2; j++) {
                    ArrayList<Integer> coordPair = new ArrayList<>();
                    coordPair.add(endX); coordPair.add(endY + j);
                    vertPathWallCoords.add(coordPair);
                }

            } else {
                for (int j = 1; j < yDiff + 1; j++) {
                    if (worldBacking[endX][endY - j] == 0 || worldBacking[endX][endY - j] == 3) {
                        worldBacking[endX][endY - j] = 2;
                        world[endX][endY - j] = floorTile;

                        ArrayList<Integer> coordPair = new ArrayList<>();
                        coordPair.add(endX); coordPair.add(endY - j);
                        vertPathCoords.add(coordPair);
                    }
                }
                for (int j = yDiff; j < abs(yDiff) + 2; j++) {
                    ArrayList<Integer> coordPair = new ArrayList<>();
                    coordPair.add(endX); coordPair.add(endY - j);
                    vertPathWallCoords.add(coordPair);
                }
            }
        }
    }

    /** Constructor: generates empty world with all tiles in world set to NOTHING, and worldBacking all set to 0. */
    public WorldGenerator(int width, int height, double desiredRatio, long seed) {
        this.worldWidth = width;
        this.worldHeight = height;
        this.desiredRatio = desiredRatio;
        this.r = new Random(seed);

        this.totalTiles = width * height;
        this.filledTiles = 0;

        this.world = new TETile[width][height];
        this.worldBacking = new Integer[width][height];
        this.rooms = new ArrayList<>();
        this.hallways = new ArrayList<>();
        this.doorCoords = new ArrayList<>();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                world[x][y] = emptyTile;
                worldBacking[x][y] = 0;
            }
        }

        addRooms();
        addRoomWalls();
        addHallways();
        addHallwayWalls();
        placeDoor();
        placeCoins();
        placeTrees();
    }

    /** Adds rooms repeatedly, until board is adequately populated (i.e. until desiredRatio is reached). */
    public void addRooms() {
        while (filledTiles / totalTiles < desiredRatio) {
            rooms.add(new Room());
        }
    }

    /** Adds hallways by connecting centre tiles of all rooms. */
    public void addHallways() {
        ArrayList<Integer> endXY = rooms.get(0).getCentreCoord();
        for (int i = 1; i < rooms.size(); i++) {
            ArrayList<Integer> startXY = endXY;
            endXY = rooms.get(i).getCentreCoord();

            hallways.add(new Hallway(startXY, endXY));
        }

    }

    public void addHallwayWalls() {
        for (Hallway hallway : hallways) {
            for (ArrayList<Integer> coord : hallway.horizPathCoords) {
                if (worldBacking[coord.get(0)][coord.get(1) - 1] == 0) {
                    worldBacking[coord.get(0)][coord.get(1) - 1] = 3;
                    world[coord.get(0)][coord.get(1) - 1] = wallTile;
                }
                if (worldBacking[coord.get(0)][coord.get(1) + 1] == 0) {
                    worldBacking[coord.get(0)][coord.get(1) + 1] = 3;
                    world[coord.get(0)][coord.get(1) + 1] = wallTile;
                }
            }

            for (ArrayList<Integer> coord : hallway.vertPathCoords) {
                if (worldBacking[coord.get(0) - 1][coord.get(1)] == 0) {
                    worldBacking[coord.get(0) - 1][coord.get(1)] = 3;
                    world[coord.get(0) - 1][coord.get(1)] = wallTile;
                }
                if (worldBacking[coord.get(0) + 1][coord.get(1)] == 0) {
                    worldBacking[coord.get(0) + 1][coord.get(1)] = 3;
                    world[coord.get(0) + 1][coord.get(1)] = wallTile;
                }
            }

            for (ArrayList<Integer> coord : hallway.vertPathWallCoords) {
                if (worldBacking[coord.get(0) - 1][coord.get(1)] == 0) {
                    worldBacking[coord.get(0) - 1][coord.get(1)] = 3;
                    world[coord.get(0) - 1][coord.get(1)] = wallTile;
                }
                if (worldBacking[coord.get(0) + 1][coord.get(1)] == 0) {
                    worldBacking[coord.get(0) + 1][coord.get(1)] = 3;
                    world[coord.get(0) + 1][coord.get(1)] = wallTile;
                }
            }
        }
    }

    public void addRoomWalls() {
        for (Room room : rooms) {
            for (ArrayList<Integer> coord : room.wallCoords) {
                worldBacking[coord.get(0)][coord.get(1)] = 3;
                world[coord.get(0)][coord.get(1)] = wallTile;
            }
        }
    }

    /** Places door in centre of last room. */
    public void placeDoor() {
        ArrayList<Integer> doorCoords = rooms.get(rooms.size() - 1).getCentreCoord();
        this.doorCoords = doorCoords;
        worldBacking[doorCoords.get(0)][doorCoords.get(1)] = 10;
        world[doorCoords.get(0)][doorCoords.get(1)] = lockedDoorTile;
    }

    public void placeCoins() {
        int coins = 0;
        while (coins < num_coins) {
            int randomX = r.nextInt(50);
            int randomY = r.nextInt(49);
            if (worldBacking[randomX][randomY] == 1 || worldBacking[randomX][randomY] == 2) {
                worldBacking[randomX][randomY] = 4;
                world[randomX][randomY] = coinTile;
                coins += 1;
            }
        }
    }

    public void placeTrees() {
        int trees = 20;
        while (trees > 0) {
            int randomX = r.nextInt(50);
            int randomY = r.nextInt(49);
            if (worldBacking[randomX][randomY] == 1) {
                worldBacking[randomX][randomY] = 1;
                world[randomX][randomY] = Tileset.TREE;
                trees -= 1;
            }
        }
    }

    /** METHODS TO LET OUTSIDE CLASSES ACCESS PRIVATE ATTRIBUTES */
    public Integer[][] getWorldBacking() {
        return worldBacking;
    }

    /** DISPLAY #### */
    public TETile[][] getWorld() {
        return world;
    }
    public ArrayList<Room> getRooms() {return rooms;}

    public static void main(String[] args) {
        int width = 50;
        int height = 49;
        long seed = 0;
        double desiredRatio = 0.3;

        WorldGenerator w = new WorldGenerator(width, height, desiredRatio, seed);

        TERenderer ter = new TERenderer();
        ter.initialize(50, 50, 0, 0);
        ter.renderFrame(w.getWorld());
    }
}

