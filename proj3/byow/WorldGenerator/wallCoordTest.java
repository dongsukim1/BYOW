package byow.WorldGenerator;

import java.util.ArrayList;

/** Not written as an actual test. See the main method. **/
public class wallCoordTest {
    private Integer[][] OurWorldBacking;
    public wallCoordTest(Integer[][] oWB) {
        OurWorldBacking = oWB;
    }

    public ArrayList<ArrayList<Integer>> wallCoordinates(int x, int y, int height, int width) {
        ArrayList<ArrayList<Integer>> wallCoords = new ArrayList<ArrayList<Integer>>();
        // try-catch block deals with a room going out-of-bounds, or off the board
        try {
            // checking bottom and top walls
            for (int i = x; i < x + width; i++) {
                if (OurWorldBacking[i][y] == 0 && OurWorldBacking[i][y + height - 1] == 0) {
                    wallCoords.add(wallCoordinatesHelper(i, y));
                    wallCoords.add(wallCoordinatesHelper(i, y + height - 1));
                } else {
                    return new ArrayList<>();
                }
            }
            // checking left and right walls
            for (int j = y + 1; j < y + height - 1; j++) {
                if (OurWorldBacking[x][j] == 0 && OurWorldBacking[x + width - 1][j] == 0) {
                    wallCoords.add(wallCoordinatesHelper(x, j));
                    wallCoords.add(wallCoordinatesHelper(x + width - 1, j));
                } else {
                    return new ArrayList<>();
                }
            }

        } catch(ArrayIndexOutOfBoundsException e) {
            return new ArrayList<>();
        }
        return wallCoords;
    }

    private ArrayList<Integer> wallCoordinatesHelper(int x, int y) {
        ArrayList<Integer> coordPair = new ArrayList<>();
        coordPair.add(x);
        coordPair.add(y);
        return coordPair;
    }

    public static void main(String[] args) {
        // all empty
        Integer[][] oWB_A = new Integer[5][5];
        for (int i = 0; i < 5; i ++) {
            for (int j = 0; j < 5; j++) {
                oWB_A[i][j] = 0;
            }
        }

        wallCoordTest demoWorld = new wallCoordTest(oWB_A);
        // assertThat bottom: 0,0 / 1,0 / 2,0 top: 0,2 / 1,2 / 2,2 left: 0,1 right: 2,1
        ArrayList<ArrayList<Integer>> coords = demoWorld.wallCoordinates(0, 0, 8, 3);
        int x = 8;
    }
}
