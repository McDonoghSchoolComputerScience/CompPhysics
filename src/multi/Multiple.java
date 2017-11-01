/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multi;

import processing.core.PApplet;

/**
 *
 * @author smithk
 */
public class Multiple extends PApplet {

    public void keyPressed() {
        recordKey(key, true);
        System.out.println("keyPressed");
    }

    public void keyReleased() {
        recordKey(key, false);
        System.out.println("keyReleased");
    }

    void recordKey(int k, boolean on) {
        switch (k) {
            case 'r':
                redOn = on;
                break;
            case 'g':
                grnOn = on;
                break;
            case 'b':
                bluOn = on;
                break;
        }
    }

    int red;
    int grn;
    int blu;

    boolean redOn;
    boolean grnOn;
    boolean bluOn;

    public void settings() {
        size(300, 300);
    }
    
    public void setup() {
    }

    public void draw() {
        red = redOn ? min(red + 1, 255) : max(red - 1, 0);
        grn = grnOn ? min(grn + 1, 255) : max(grn - 1, 0);
        blu = bluOn ? min(blu + 1, 255) : max(blu - 1, 0);

        background(red, grn, blu);
        text(String.format("(%d, %d, %d)", red, grn, blu), 50, 125);
    }
    
    public static void main(String[] args) {
        PApplet.main(new String[]{Multiple.class.getName()});
    }
}
