
package splitBounce;

import processing.core.PVector;

/**
 * @author smithk
 */
public class Bounce {
    private PVector position;
    private PVector velocity;
    
    public Bounce(PVector position) {
        this.position = position;
        velocity = new PVector();
    }
    
    public void render (PWindow canvas) {
        canvas.pushStyle();
        canvas.fill (255, 100, 0);
        PVector canvasPos = PVector.sub (position, canvas.getOrigin());
        // System.out.println (canvasPos);
        canvas.ellipse (canvasPos.x, canvasPos.y, 20, 20);
    }
    
    public void update (float deltaT) {
        position.add (PVector.mult (velocity, deltaT));
        velocity.add (PVector.mult (Universe.GRAVITY, deltaT));
    }

    public void bounce () {
        velocity.y = - velocity.y;
    }
    
    public PVector getPosition() {
        return position;
    }
}
