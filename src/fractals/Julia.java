
package fractals;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

/**
 * @author smithk
 */
public class Julia extends PApplet {
    private PImage julia;
    private double xScale;
    private double yScale;
    private double xShift;
    private double yShift;
    private Complex c;
    
    private static Complex f(Complex z, Complex c) {
        return Complex.add (Complex.multiply (z, z), c);
    }
    
    private static double prorate (double x, double a, double b, double c, double d) {
        return (x - a)/(b - a) * (d - c) + c;
    }

    private long buildJulia() {
        julia = createImage (width, height, HSB);
        long start = millis();
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++) {
                Complex z = new Complex (prorate (x, 0, width, -xScale, xScale) + xShift, 
                                         prorate (y, 0, height, -yScale, yScale) + yShift);
                int iterations = 0;
                while (iterations < 256) {
                    iterations ++;
                    z = f(z, c);
                    if (z.magnitude() > 3)
                        break;
                }
                if (iterations < 256) {
                    int p = color ((128 + iterations * 15) % 256, 255, 255);
                    stroke (p);
                    point (x, y);
                    julia.set (x, y, p);
                }
                else
                    julia.set (x, y, color(0, 255, 0));
                
                // System.out.format ("%s: %d\n", z.toString(), iterations);
            }
        return millis();
    }
        
    public void settings() {
        size(800, 400);
    }
    
    public void setup() {
        colorMode (HSB, 255);
        xScale = 1.5;
        yScale = xScale * height/width;
        c = new Complex (-1, 0);
        long mills = -(millis() - buildJulia ());
        System.out.format ("done: %.3f secs\n", mills/1000.0);
    }

    public void draw() {        
        image (julia, 0, 0);
        
        if (marquing) {
            float dW = mouseX - startMarque.x;
            float dH = mouseY - startMarque.y;
            if (abs(dW) > abs(dH))
                dH = height * dW / width;
            else
                dW = width * dH / height;
            
            stroke (255);
            strokeWeight (2);
            noFill();
            rect (startMarque.x, startMarque.y, dW, dH);
        }
    }
    
    PVector startMarque;
    boolean marquing;
    
    public void mousePressed() {
        startMarque = new PVector (mouseX, mouseY);
        marquing = true;
    }
    
    public void mouseReleased() {
        marquing = false;
    }
    
    public void keyTyped() {
        if (key == ' ' && marquing) {
            float dW = mouseX - startMarque.x;
            float dH = mouseY - startMarque.y;
            if (abs(dW) > abs(dH))
                dH = height * dW / width;
            else
                dW = width * dH / height;
   
            xScale *= (abs(dW) / width);
            yScale *= (abs(dH) / height);
            
            // xShift = -xScale * 2 * mouseX / width;
            // yShift = -yScale * 2 * mouseY / height;
            
            marquing = false;
            buildJulia ();
        }
        else if (key == 'r') {
            xScale = 1.5;
            yScale = xScale * height/width;
            xShift = 0;
            yShift = 0;
            buildJulia ();
        }
    }
    
    public static void main(String[] args) {
        PApplet.main(new String[]{Julia.class.getName()});
    }
}
