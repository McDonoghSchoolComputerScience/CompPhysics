/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PSurface;
import vector.Vector;

/**
 *
 * @author smithk
 */
public class Graph<T extends Vector> implements Push<T> {
    private PApplet graphic;
    private ArrayList<T> points;
    
    private class Graphic extends PApplet {
        public void settings() {
            size (200, 300);
        }
        
        public void setup() {
            background (255);
            ellipse (100, 100, 20, 30);
        }
        
        public void draw() {
            for (T point : points) {
                line (0, 0, (float)point.i, (float)point.j);
            }
        }
    }
    
    static void removeExitEvent(PSurface surf) {
        final java.awt.Window win = ((processing.awt.PSurfaceAWT.SmoothCanvas) surf.getNative()).getFrame();
        for (java.awt.event.WindowListener evt : win.getWindowListeners())
            win.removeWindowListener(evt);
    }
    
    Graph() {
        points = new ArrayList();
        graphic = new Graphic();
        PApplet.runSketch (new String[] {
            "--location=100,200",
            ""}, graphic);
        removeExitEvent (graphic.getSurface());
    }
    
    public boolean push (T pushed) {
        points.add (pushed);
        return true;
    }
}
