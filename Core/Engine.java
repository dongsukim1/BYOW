package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.WorldGenerator.Interactivity;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.StdDraw;
import java.awt.*;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 50;
    public static final int HEIGHT = 50;
    private long seed;
    private String gameData = "";
    private Interactivity currentGame;
    private int avatarNum = 0;
    private TETile currAvatar = Tileset.AVATAR;
    public static final int MAGICNUMBER1 = 16;
    public static final int MAGICNUMBER2 = 40;
    public static final int MAGICNUMBER3 = 10;
    public static final int MAGICNUMBER4 = 20;
    public static final int MAGICNUMBER5 = 14;
    public static final double MAGICNUMBER6 = 14.45;
    public static final int MAGICNUMBER7 = 11;
    private Out out;
    private static final String SAVEFILE = "byow/Core/saveFile1.txt";

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        displayMenu(currAvatar);
        String pressedKey;
        String newSeed = "";
        boolean check = true;

        while (check || !currentGame.gameWon) {
            if (StdDraw.hasNextKeyTyped()) {
                pressedKey = Character.toString(StdDraw.nextKeyTyped());
                if (pressedKey.equals("N") || pressedKey.equals("n")) {
                    newSeed += pressedKey;
                    check = false;

                    enterSeed("");

                    while (true) {
                        if (StdDraw.hasNextKeyTyped()) {
                            StdDraw.clear(Color.BLACK);
                            pressedKey = Character.toString(StdDraw.nextKeyTyped());
                            newSeed += pressedKey;
                            if (pressedKey.equals("S") || pressedKey.equals("s")) {
                                interactWithInputString(newSeed);
                                break;
                            } else {
                                enterSeed(newSeed.substring(1));
                            }
                        }
                    }
                } else if (pressedKey.equals(":") && currentGame != null) {
                    String newKey = "";
                    while (newKey.equals("")) {
                        if (StdDraw.hasNextKeyTyped()) {
                            newKey = Character.toString(StdDraw.nextKeyTyped());
                        }
                    }
                    if (newKey.equals("Q") || newKey.equals("q")) {
                        saveGame();
                    } else {
                        interactWithInputString(newKey);
                    }
                } else if (pressedKey.equals("Q") || pressedKey.equals("q") && currentGame == null) {
                    System.exit(0);
                } else if (pressedKey.equals("C") || pressedKey.equals("c") && currentGame == null) {
                    changeAvatar();
                    displayMenu(currAvatar);
                } else {
                    interactWithInputString(pressedKey);
                }
            }
            if (currentGame != null && !currentGame.gameWon) {
                currentGame.startHUD(currAvatar);
            }
        }
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, running both of these:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input)  {
        StringBuilder seedBuilder = null;
        for (char c : input.toCharArray()) {
            if (seedBuilder != null) {
                if (Character.isDigit(c)) {
                    seedBuilder.append(c);
                } else if (c == 'S' || c == 's') {
                    seed = Long.parseLong(seedBuilder.toString());
                    currentGame = new Interactivity(seed, currAvatar);
                    seedBuilder = null;
                }
            } else {
                if (c == 'N' || c == 'n') {
                    seedBuilder = new StringBuilder();
                } else if (c == ':') {
                    int idx = input.indexOf(":q");
                    int idx2 = input.indexOf(":Q");
                    if (idx != -1 || idx2 != -1) {
                        saveGame();
                    }
                } else if (c == 'L' || c == 'l') {
                    interactWithInputString(new In(SAVEFILE).readString());
                } else {
                    gameData += c;
                    currentGame.processMovement(c, currAvatar);
                }
            }
        }
        return currentGame.world;
    }
    public void saveGame() {
        String data = "N" + seed + "S";

        out = new Out(SAVEFILE);
        out.print(data);

        for (char c : gameData.toCharArray()) {
            if (Character.toLowerCase(c) == 'w' || Character.toLowerCase(c) == 'a' || Character.toLowerCase(c) == 's'
                    || Character.toLowerCase(c) == 'd' || Character.toLowerCase(c) == 'g'
                    || Character.toLowerCase(c) == 'p') {
                out.print(c);
            }
        }
        //System.exit(0);
    }

    /** friday apr 21st additions; building interactWithKeyboard() */
    public void displayMenu(TETile avatar) {
        StdDraw.setCanvasSize(WIDTH * MAGICNUMBER1, HEIGHT * MAGICNUMBER1);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);

        Font fontBig = new Font("Monaco", Font.BOLD, MAGICNUMBER2);
        StdDraw.setFont(fontBig);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + MAGICNUMBER3, "61B: THE GAME");

        Font fontSmall = new Font("Monaco", Font.BOLD, MAGICNUMBER4);
        StdDraw.setFont(fontSmall);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 5, "New Game(N)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 7, "Load Game(L)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 9, "Save Game(S)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - MAGICNUMBER7, "Quit Game(Q)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - MAGICNUMBER5, "Change Avatar(C)");
        avatar.draw((WIDTH / 2) + 7, HEIGHT / 2 - MAGICNUMBER6);
        StdDraw.show();
    }

    /** Used to display seed as user is typing it. */
    public void enterSeed(String currSeed) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "enter a seed:");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 5, currSeed);
        StdDraw.show();
    }
    public void changeAvatar() {
        if (avatarNum == 0) {
            avatarNum = 1;
            currAvatar = Tileset.AVATAR_HUG;
        } else if (avatarNum == 1) {
            avatarNum = 2;
            currAvatar = Tileset.AVATAR_DENERO;
        } else if (avatarNum == 2) {
            avatarNum = 0;
            currAvatar = Tileset.AVATAR;
        }
    }
}
