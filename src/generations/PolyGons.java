/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generations;

import processing.core.PApplet;
import vector.Vector;

/**
 *
 * @author smithk
 */
public class PolyGons extends PApplet {
    private Stone poly;
    private float dT;
    private long rSeed;
    private int sides;
    private float elapsed;
    
    public static final double G = 9.8; // m/s/s
    public static float sideLen (int sides, float radius) {
        return 2 * radius * (float)Math.sin (Math.PI/sides);
    }
    
    public PolyGons() {
        rSeed = (long)(Math.random() * 1000_000);
        dT = 0.02f;
        sides = 5;
        
        elapsed = 0;
        poly = new Stone (sides, sideLen(sides, 80), 0.6f, (float)Math.PI/2, rSeed);
    }
    
    public void settings () {
        size (displayWidth, 300);
    }
    
    public void setup() {
        textSize (20);
    }
    
    public void draw() {
        background (0);
        strokeWeight(2);
        stroke (255);
        line (0, height-75, width, height-75);
        stroke (255, 255, 0);
        pushMatrix();
        translate (30, height-75);
        poly.render(this);
        poly.update(dT);
        popMatrix();
        
        Vector r = poly.getArms();
        fill (255);
        text (String.format ("Lever Arms: <%.2f, %.2f>", r.i, r.j), 30, height-50);
        text (String.format ("omega: %.3f", poly.getOmega()), 30, height-25);
        text (String.format ("dT: %.3f, %.3f -> %.2f", dT, elapsed, poly.getOffset()), 400, height-50);
        text (String.format ("Seed: %d", rSeed), 400, height-25);
        
        if (poly.getOmega() == 0)
            noLoop();

        elapsed += dT;
    }
    
    int tens;
    public void keyPressed() {
        if (key == '+')
            tens++;
        else if (key >= '0' && key <= '9') {
            sides = key - '0' + tens * 10;
            if (sides > 2) {
                rSeed = (long)(Math.random() * 1000_000);
                elapsed = 0;
                poly = new Stone (sides, sideLen(sides, 80), 0.6f, (float)Math.PI/2, rSeed);
                loop();
            }
            tens = 0;
        }            
        else if (key == ENTER) {
            elapsed = 0;
            poly = new Stone (sides, sideLen(sides, 80), 0.6f, (float)Math.PI/2, rSeed);
            loop();
        }
        else if (key == '=') {
            elapsed = 0;
            rSeed = (long)(Math.random() * 1000_000);
            poly = new Stone (sides, sideLen(sides, 80), 0.6f, (float)Math.PI/2, rSeed);
            loop();
        }
        else if (key == ' ')
            looping = !looping;
        else if (key == 'd') {
            elapsed = 0;
            rSeed = 147187+1;
            dT = 1;
            poly = new Stone (23, sideLen(23, 80), 0.11f, (float)Math.PI/2, rSeed);
            loop();
        }
        
        else if (key == CODED)
            if (keyCode == UP)
                dT *= 2;
            else if (keyCode == DOWN)
                dT *= 0.50;
    }
    
    public void mousePressed() {
        poly.nudge ((float)Math.PI/2);
    }
    
    public static void main(String[] args) {
        PApplet.main(new String[]{"generations.PolyGons"});
    }
}
