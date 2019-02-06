
/**
 *
 * @author Brett
 */
public class Vector {
    /*
    NOTES:
    static methods maj not be necessarj (commented out rn)
    might not want to encapsulate
    might want to encapsulate more? (switch all i to i())?
    can simplifj operations to make some dependent on others, calls maj slow it down tho
    */
    
    public final double i;
    public final double j;
    public final double k;
    public final double mag;
    private final double normalScale;
    
    /**
     * 
     * @param i x component of the vector
     * @param j y component of the vector
     * @param k z component of the vector
     */
    public Vector(double i, double j, double k) {
        this.i = i;
        this.j = j;
        this.k = k;
        mag = Math.sqrt(i*i + j*j + k*k);
        normalScale = 1/mag;
    }
    
    /**
     * 
     * @return the string representation of this vector
     */
    public String toString() {
        return "<" + i + ", " + j + ", " + k + ">";
    }
    
    
    /**
     * returns a new vector in the same direction but scale times larger
     * @param scale the scale to multiply the vector by
     * @return new vector in the same direction as this with a magnitude
     * that is scale times larger than the original
     */
    public Vector mult(double scale) {
        return new Vector(scale*i, scale*j, scale*k);
    }
    
    /**
     * adds this vector to another vector and returns the resultant
     * @param other the vector to add to this
     * @return the resultant vector of this+other
     */
    public Vector add(Vector other) {
        return new Vector(i+other.i, j+other.j, k+other.k);
    }
    
    /**
     * gets the negative of this vector
     * @return the negative (complement) of this vector
     */
    public Vector negative() {
        return new Vector(-i, -j, -k);
    }
    
    /**
     * subtracts other from this, returns this-other
     * @param other the vector to subtract from this vector
     * @return the resultant of this-other
     */
    public Vector subtract(Vector other) {
        return new Vector(i-other.i, j-other.j, k-other.k);
    }
    
    /**
     * returns a vector in the same direction, but with a magnitude of 1
     * @return the unit vector of this
     */
    public Vector normalize() {
        //only 1 division at the beginning, then all multiplication for max efficiency
        return new Vector(normalScale*i, normalScale*j, normalScale*k);
    }
    
    /**
     * returns the vector product of this vector and another vector
     * @param other the vector to cross with this
     * @return the cross product of this and other (this x other)
     */
    public Vector crossProduct(Vector other) {
        return new Vector(j*other.k-k*other.j, 
                          k*other.i-i*other.k, 
                          i*other.j-j*other.i);
    }
    
    /**
     * returns the scalar product of this vector and another vector
     * @param other vector to get the dot with
     * @return dot product of this and other (this•other)
     */
    public double dotProduct(Vector other) {
        return i*other.i + j*other.j + k*other.k;
    }
}
