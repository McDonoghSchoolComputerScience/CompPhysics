package fire;

import vector.Vector;

public class Balls {
    public double radius;
    public double mass;
    
    public Vector r;
    public Vector v;
    public Vector a;
    
    Vector dragForce;
    Vector weight;
    Vector F;
    
    public Balls (double radius, double mass, Vector r, Vector v, Vector a, Vector dragForce, Vector weight, Vector F) {
        this.radius = radius;
        this.mass = mass;
        
        this.r = r;
        this.v = v;
        this.a = a;
        
        this.dragForce = dragForce;
        this.weight = weight;
        this.F = F;
    }
}

