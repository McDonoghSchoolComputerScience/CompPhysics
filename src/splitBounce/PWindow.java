
package splitBounce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import processing.core.PApplet;
import processing.core.PVector;

/**
 * @author smithk
 */
public class PWindow extends PApplet {
    private PVector origin;
    private int w;
    private int h;

    private static Map<Bounce, PWindow> bouncers;
    private static List<PWindow> windows;
    static {
        bouncers = new HashMap();
        windows = new ArrayList();
    }
    
    public static void addBouncer (Bounce ball, PWindow canvas) {
        bouncers.put (ball, canvas);
    }
    
    public static PWindow previous(PWindow aWin) {
        int index = windows.indexOf(aWin);
        return (index == 0 ? null : windows.get(index-1));
    }
    
    public static PWindow next(PWindow aWin) {
        int index = windows.indexOf(aWin);
        return (index == windows.size() - 1 ? null : windows.get(index+1));
    }
    
    public PWindow(int w, int h, int n) {
        this.w = w;
        this.h = h;
        windows.add (this);
        
        PWindow prev = previous (this);
        if (prev != null)
            origin = new PVector (0, prev.origin.y + prev.h);
        else 
            origin = new PVector();
        System.out.println (n + ", " + origin);
        
        PApplet.runSketch (new String[] {this.getClass().getSimpleName()}, this);
    }
    
    public PVector getOrigin() {
        return origin;
    }
    
    public void settings() {
        size (w, h);
    }
    
    public void setup() {
        frameRate (20);
    }
    
    public void draw() {
        background (0);
        for (Bounce ball : bouncers.keySet())
            if (bouncers.get(ball) == this) {
                ball.render (this);
                ball.update (0.1F);
                
                PVector current = ball.getPosition();
                if (current.y < origin.y) {
                    PWindow prev = previous(this);
                    if (prev != null)
                        bouncers.put (ball, prev);
                    else
                        ball.bounce();
                }
                else
                if (current.y > origin.y + h) {
                    PWindow next = next(this);
                    if (next != null)
                        bouncers.put (ball, next);
                    else
                        ball.bounce();
                }
            }
    }
    
    public void mouseClicked() {
        addBouncer (new Bounce(new PVector(origin.x + mouseX, origin.y + mouseY)), this);
    }
}
