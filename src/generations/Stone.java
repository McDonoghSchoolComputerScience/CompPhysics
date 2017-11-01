/**
 *
 * @author smithk
 */
package generations;

import java.util.ArrayList;
import java.util.Random;
import processing.core.PApplet;
import vector.Vector;

/**
 *
 * @author smithk
 */
public class Stone {
    private final Random rand;
    private final ArrayList<Vector> vertices;
    private float omega;
    
    private Vector centerMass;
    private double offset;
    
    ArrayList<Vector> history;
    
    // the vertex that is the point of rotation (tumble) and the amount of angle
    // remaining to tumble relative to the point of rotation before shifting to
    // the next vertex. 

    private int pivotNdx;
    private float pivotAngleRemnant;
    
    private final float inertia;
    private final float mass;
    
    public Stone (int sides, float sideLen, float warp, float omega, long rSeed) {

        // begin by creating a polygon of specified number of sides, adding each
        // vertex to the vertices structure. The first vertex will be that of the
        // left edge of the base.
        
        rand = new Random(rSeed);
        vertices = new ArrayList(sides);
        history = new ArrayList();
                
        float angle = (float)Math.PI * 2 / sides;
        boolean oops;
        do {
            oops = false;
            
            vertices.clear();
            double rotation = 0;
        
            Vector coord = new Vector();    // actual coordinate (vs vector offsets)
        
            // begin at the origin (always <0, 0>)
            //
            vertices.add (new Vector());
            for (int side = 0; side < sides-1; side++) {
                // tweak the length of the side as a function of sideLen and warp
                // A warp of 0 generates a regular polygon.
                //
                double warpedLen = sideLen * (rand.nextDouble() * warp + 1 - warp/2);
                Vector to = new Vector (Math.cos(rotation) * warpedLen, Math.sin(rotation) * warpedLen);
                
                // obtain the "real" coordinate and add to vertices
                coord.add (to);
                vertices.add (new Vector(coord.i, coord.j));
                
                // finally, move on to the next vertex
                rotation += angle;
            }
        
            Vector v1 = vertices.get(sides-2);
            Vector v2 = vertices.get(sides-1);

            double k = v2.j * v1.i / v1.j;
            oops = (v2.j < 0 || k < v2.i);
            
        } while (oops);
        
        setCenterMass();

        mass = 1.0f;
        this.omega = omega;
        
        // calculate average radius
        float radius = 0;
        for (Vector v : vertices)
            radius += Vector.subtract (centerMass, v).mag;
        radius /= vertices.size();
        inertia = radius * radius * mass;

        pivotNdx = 1;
        pivotAngleRemnant = getPivotAngle (pivotNdx);
    }
    
    public float dOmegaDt (float dT) {
        Vector r = getArms();
        float tau = (float)(PolyGons.G * r.i);
        return tau / inertia * dT;
    }
    
    public final void setCenterMass() {
        centerMass = new Vector();
        for (Vector v : vertices)
            centerMass.add (v);
        centerMass.scale (1.0/vertices.size());
        
        if (history.size() > 600) {
            history.remove (0);
        }
        history.add (new Vector (centerMass.i, centerMass.j, omega));
    }
    
    public Vector getArms() {
        return Vector.subtract (centerMass, vertices.get(pivotNdx));
    }
    
    public float getOmega() {
        return omega;
    }
    
    public void nudge(float dOmega) {
        omega += dOmega;
    }
    
    public double getOffset() {
        return offset;
    }
    
    @Override
    public String toString() {
        String coords = centerMass.toString();
        int vNdx = 0;
        for (Vector v : vertices) 
            coords += String.format ("\n<%.2f, %.2f> %.2f", v.i, v.j, getPivotAngle(vNdx++));
        return coords;
    }
    
    private float getPivotAngle (int vNdx) {
        Vector pivot 
                = Vector.subtract (vertices.get (vNdx),
                                   vertices.get ((vNdx + 1) % vertices.size()));
        float angle = (float)(Math.PI - Math.acos (pivot.i/pivot.mag));
        return angle;
    }
    
    public void render (PApplet p) {
        if (centerMass.i > p.width) {
            for (Vector v : vertices)
                v.i -= p.width;
            centerMass.i -= p.width;
        }
        
        Vector from = vertices.get (0);
        for (int vNdx = 0; vNdx < vertices.size(); vNdx++) {
            Vector to = vertices.get ((vNdx + 1) % vertices.size());
            // flip j ("y") to present "normal coord system"
            p.line ((float)from.i, -(float)from.j, (float)to.i, -(float)to.j); 
            if (vNdx == pivotNdx) {
                p.stroke(255, 0, 0);
                p.ellipse ((float)from.i, -(float)from.j, 5, 5);
                p.line ((float)from.i, -(float)from.j, (float)centerMass.i, -(float)centerMass.j);
                p.stroke(255, 255, 0);
            }
            from = to;
        }
        p.stroke (255, 0, 0);
        p.ellipse ((float)centerMass.i, -(float)centerMass.j, 5, 5);

        p.stroke (100, 200, 100);
        for (Vector v : history) {
            p.point ((float)v.i, -(float)v.j);
        }
    }
    
    public float drag() {
        int prevNdx = (pivotNdx + vertices.size() - 1) % vertices.size();
        float deltaX = (float)(centerMass.i - vertices.get(prevNdx).i);
        
        return deltaX * deltaX / 15_000f * omega;
    }
    
    public void update (float dT) {
        omega = Math.max (dOmegaDt (dT) + omega, 0);
        float dR = omega * dT;
        
        while (dR > 0) {
            if (dR < pivotAngleRemnant) {
                rotate (dR);
                break;
            }
            else {
                float partial = pivotAngleRemnant;
                rotate (partial);
                dR -= partial;
                pivotNdx = (pivotNdx + 1) % vertices.size();
                pivotAngleRemnant = getPivotAngle (pivotNdx);
                
                omega -= drag();
            }
        }
    }
    
    public void rotate (float dR) {
        // rotate all vertices and the center of mass by -dR relative to the
        // pivot vertex.
        
        double initialPos = centerMass.i;

        Vector p = vertices.get(pivotNdx);
        
        for (int vNdx = 0; vNdx < vertices.size(); vNdx++) {
            if (vNdx == pivotNdx)
                continue;
            Vector v = vertices.get(vNdx);
            double angle = Math.atan2 (v.j - p.j, v.i - p.i) - dR;
            v.subtract (p);
            vertices.set (vNdx, (new Vector (Math.cos(angle) * v.mag, Math.sin(angle) * v.mag)).add(p));
        }
        setCenterMass();
        
        pivotAngleRemnant -= dR;
        
        offset += centerMass.i - initialPos;
    }
}

