package byow.InputDemo;

import edu.princeton.cs.algs4.StdDraw;

import java.util.Random;

/**
 * Created by hug.
 */
public class RandomInputSource implements InputSource {
    Random r;

    public RandomInputSource(Long seed) {
        r = new Random(seed);
    }

    /** Returns a random letter between a and z.*/
    public char getNextKey() {
        return (char) (r.nextInt(26) + 'A');
    }

    public boolean possibleNextInput() {
        return true;
    }

    public static void main(String[] args) {
        long seed = 0;
        RandomInputSource ris = new RandomInputSource(seed);
        for (int i = 0; i < 10; i++) {
            System.out.println(ris.getNextKey());
        }
    }
}

