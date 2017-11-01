package nbody;

import java.util.ArrayList;
import java.util.List;
import processing.core.PApplet;
import vector.Vector;

/**
 * Quadrant describes a single quadrant of space, containing a collection of
 * masses, each with two vectors describing location relative to the origin
 * of the quadrant (upper left) and velocity.
 * 
 * @author smithk
 */
public class Quadrant implements Massive {    
    
    private final double width;
    private final double height;
    
    final public static double G = 6.67408e-11; // m kg s
    final public static double LY = 9.460e15;  // m 
    
    private class Mass implements Massive {
        private double mass;
        private Vector position;
        private Vector velocity;
        
        public double getMass() {
            return mass;
        }
        
        public Vector getPosition() {
            return position;
        }
        
        public Mass (double mass, Vector position, Vector velocity) {
            this.mass = mass; // kg
            this.position = position;  // m
            this.velocity = velocity;  // km/s
        }
        
        public Mass () {
            this (Math.random() * 1.9e30 * 100 + 5.9e24,  // (mass earth, mass sun * 100) kg
                  new Vector (Math.random() * width, Math.random() * height), // m
                  new Vector ((Math.random() * 2 - 1) * 220, (Math.random() * 2 - 1) * 220)); // km/s
        }
        
        public void update (Vector acceleration) {
            velocity.add (acceleration);
            position.add (velocity);
            // position.add (new Vector(2,2));
        }
        
        public String toString() {
            return position.toString() + " " + velocity.toString();
        }
        
        public void render (PApplet pa, double pixelSize) {
            pa.ellipse ((float)(position.i/pixelSize), (float)(position.j/pixelSize), 5, 5);
            // System.out.println(position.i/pixelSize + "," + position.j/pixelSize);
        }

        @Override
        public Vector getVelocity() {
            return velocity;
        }
    }

    private List<Mass> masses;
    
    /**
     * 
     * @param numberMasses
     * @param width interpreted as m
     * @param height interpreted as m
     */
    public Quadrant (int numberMasses, double width, double height) {
        this.width = width;
        this.height = height;
        
        masses = new ArrayList(numberMasses * 2);
        for (int i = 0; i < numberMasses; i++)
            masses.add (new Mass ());
    }
    
    public void render (PApplet pa, double pixelSize) {
        for (Mass mass : masses)
            mass.render (pa, pixelSize);
    }
    
    public double getMass() {
        double total = 0;
        total = masses.stream().map((mass) -> mass.getMass()).reduce(total, (accumulator, _item) -> accumulator + _item);
        return total;
    }
    
    public void update (Vector externalAccel) {
        for (int one = masses.size(); one-- > 0; ) {
            Mass mOne = masses.get(one);
            for (int two = one; two-- > 0; ) {
                Mass mTwo = masses.get(two);
                Vector radius = mOne.getPosition().subtract(mTwo.getPosition());
                double gravitas = (mOne.getMass() * mTwo.getMass() / radius.mag / radius.mag) * G;
                Vector accel = radius.normScale (gravitas/1_000_000_000);
                mTwo.update (accel.neg());
                mOne.update (accel);
            }
            if (externalAccel != null)
                mOne.update (externalAccel);
        }
        // System.out.println("Quadrant update");
    }
    
    @Override
    public Vector getVelocity() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Vector getPosition() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
