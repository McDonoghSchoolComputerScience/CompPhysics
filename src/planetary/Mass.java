/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planetary;

import vector.Vector;

/**
 *
 * @author smithk
 */
public class Mass {

    private final String nomen;
    private final double mass;
    private final Vector position;
    private final Vector velocity;
    
    private final double radius;
    private final double rotation;    // radians per second
    
    private float angleOfRotation;
    
    public String getNomen() {
        return nomen;
    }

    public double getMass() {
        return mass;
    }

    public Vector getPosition() {
        return position;
    }
    
    float getAngleOfRotation() {
        return angleOfRotation;
    }

    double getRadius() {
        return radius;
    }
    
    
    /**
     * 
     * @param nomen name
     * @param radius meters
     * @param rotation period of rotation in [earth] days
     * @param mass mass in kilograms
     * @param position mean distance from sun in meters
     * @param velocity orbital velocity in meters/second
     */
    public Mass(String nomen, double radius, double rotation, double mass, double position, double velocity) {
        this.nomen = nomen;
        this.mass = mass;
        this.radius = radius;
        this.position = new Vector (position, 0, 0);
        this.velocity = new Vector (0, 0, velocity);
        
        this.rotation =  (2 * Math.PI) / (rotation * 86400);    // raidans/second
    }

    public void update(double dT, Vector f) {
        velocity.add(Vector.scale(f, dT));
    }

    public void update(double dT) {
        position.add(Vector.scale(velocity, dT));
        angleOfRotation = (float)((angleOfRotation + dT * rotation) % (2 * Math.PI));
    }
}
