/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nbody;

import processing.core.PApplet;
import processing.core.PSurface;

/**
 *
 * @author smithk
 */
public class Visualizer {
    private PApplet graphic;
    private Quadrant quad;
    
    private boolean redrawn;
    
    public boolean isRedrawn() {
        boolean wasRedrawn = redrawn;
        redrawn = false;
        return wasRedrawn;
    }
    
    private class Graphic extends PApplet {
        private int wide;  // px
        private int high;  // px
        private double pixelSize; // m/px
        
        public Graphic (int wide, int high, double pixelSize) {
            this.wide = wide;
            this.high = high;
            
            this.pixelSize = pixelSize;
        }
        
        public void settings() {
            size (wide, high);
        }
        
        public void setup() {
            background (0);
            // frameRate(100);
            noLoop();
        }
        
        public void draw() {
            // background (0);
            quad.render (this, pixelSize);
            // System.out.println("Visualizaer draw");
            redrawn = true;
        }
    }
    
    static void removeExitEvent(PSurface surf) {
        final java.awt.Window win = ((processing.awt.PSurfaceAWT.SmoothCanvas) surf.getNative()).getFrame();
        for (java.awt.event.WindowListener evt : win.getWindowListeners())
            win.removeWindowListener(evt);
    }
    
    Visualizer(Quadrant quad, int wide, int high, double pixelSize) {
        this.quad = quad;
        graphic = new Visualizer.Graphic(wide, high, pixelSize);
        PApplet.runSketch (new String[] {
            "--location=100,100",
            ""}, graphic);
        removeExitEvent (graphic.getSurface());
    }  
    
    public void update() {
        graphic.redraw();
    }
}
