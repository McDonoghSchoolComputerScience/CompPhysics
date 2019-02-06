
package splitBounce;

import processing.core.PVector;

/**
 * @author smithk
 */
public class Universe {
    public static final PVector GRAVITY;
    static {
        GRAVITY = new PVector(0, 9.8F);
    }

    public static void main(String[] args) {
        new PWindow(200, 500, 0);
        new PWindow(300, 600, 1);
        new PWindow(200, 400, 2);
    }
}
