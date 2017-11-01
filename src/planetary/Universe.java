/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planetary;

import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PImage;
import processing.event.MouseEvent;
import vector.Vector;

/**
 *
 * @author smithk
 */
public class Universe extends PApplet {
    public final static double G = 6.6740831e-11; // m^3 / (kg s^2)    boolean axis = false;
    public static double MpP = 1.3e9 / 4; // Meters / Pixel
    
    private long dT = 3600 * 8;   
    private long elapsed = 0;
 
    private ArrayList<Mass> bodies;
    private ArrayList<MassRenderer> renderers;
    private Mass sol;
    private Mass terra;
    private Mass launched;
    private MassRenderer solRenderer;

    private PImage field;
    private Vector cameraLoc;

    
    private void addBody (Mass body, MassRenderer renderer) {
        bodies.add (body);
        renderers.add (renderer);
    }
    
    private static Vector Fg (Mass a, Mass b) {
        Vector distance = Vector.subtract (a.getPosition(), b.getPosition());        
        return distance.normScale ((a.getMass() * b.getMass() * G) / distance.mag / distance.mag);
    }
    
    private static String V2S (Vector v) {
        return String.format ("<%.2f, %.2f, %.2f>", v.i, v.j, v.k);
    }
    
    private void update (double dT) {
        for (int aNdx = 0; aNdx < bodies.size(); aNdx++) {
            Mass a = bodies.get(aNdx);
            for (int bNdx = aNdx + 1; bNdx < bodies.size(); bNdx++) {
                Mass b = bodies.get(bNdx);
                Vector fg = Fg (b, a);
                a.update (dT, Vector.scale (fg, 1/a.getMass()));
                b.update (dT, Vector.scale (fg, -1/b.getMass()));
            }
            a.update(dT);
        }
    }
    
    private void populate(PApplet p) {
        MassRenderer.setPApplet(p);
        
        bodies = new ArrayList();
        renderers = new ArrayList();
        
        elapsed = 0; 
        
        // Mass (name, radius(m), rotation(days), mass(kg), distance(m), orbital velocity(m/s)
        sol = new Mass ("Sol", 7e8, 25.38,  1.989e30, 0, 0);
        // MassRenderer (Mass, track color(r,g,b), texture file(.jpg)
        solRenderer = new MassRenderer (sol, color(255,200,0), "universe/sol_flat.jpg");
        addBody (sol, solRenderer);
        
        Mass m;
        addBody ((m = new Mass ("Mercury", 4.9e6, 0, 3.28e23,  5.8e10,   -47.4e3)),
                      new MassRenderer (m, color(100,100,0),   null));

        addBody ((m = new Mass ("Venus",    6e6,  0,  4.85e24, 1.082e11, -35e3)),
                      new MassRenderer (m, color(0,255,0),  "universe/mars.jpg"));
        
        addBody ((m = new Mass ("Terra",  6.4e6, 1, 5.972e24, 1.496e11, -30e3)),
                      new MassRenderer (m, color(0,255,255), "universe/terra_flat.jpg"));
        terra = m;
        
        // addBody (new Mass ("Luna", color(128,128,0), 7.3456e22, new Vector(149.6e9+384.4e6,0), new Vector(0,31e3)));
        addBody ((m = new Mass ("Luna", 1.65e6, 24, 7.3456e22, 1.496e11+384.4e6, -31.e3)),
                      new MassRenderer (m, color(128), "universe/mars.jpg"));
        
        addBody ((m = new Mass ("Mars", 3.4e6, 0, 6.29e23,  2.279e11, -24.1e3)),
                      new MassRenderer (m, color(255,80,0), "universe/mars.jpg"));
        
        addBody ((m = new Mass ("Jupiter",70e6, 0, 1.90e27,  7.786e11, -13.1e3)),
                      new MassRenderer (m, color(200), "universe/jupiter_flat.jpg"));
        
        addBody ((m = new Mass ("Saturn", 58e6, 0,  5.68e26,  1.429e12,  -9.6e3)),
                      new MassRenderer (m, color(200,200,0),  null));        
    }
    
    private void annotate() {
        int days = (int)(elapsed / 86400);
        int hours = (int)(elapsed % 86400 / 3600);
        textSize (20);
        fill (255);
        text (String.format ("%d + %02d", days, hours), 20, height-50);
        text (V2S(cameraLoc), 20, height-75);
        text (String.format("%.2g m/px", MpP), 20, height-100);
    }
    
    @Override
    public void settings() {
        size (1600, 1000, P3D);
    }

    @Override
    public void setup() {
        field = loadImage ("universe/starFieldD.jpg");
        field.resize(width, height);
        
        cameraLoc = new Vector (0, -130, 750);
        updating = true;
        
        populate(this);
    }

    @Override
    public void draw() {                
        if (updating)
            elapsed += dT;
        
        if (black)
            background (0);
        else
            background(field);
        pushMatrix(); 
        {
            translate (width/2, height/2, 0);
            if (rideLaunched && launched != null) {
                Vector lp = Vector.scale (launched.getPosition(), 1/MpP);
                System.out.println(lp);
                camera ((float)(lp.i), (float)lp.j, (float)lp.k, 0, 0, 0, 0, 1, 0);
            }
            else {
                camera ((float)cameraLoc.i, (float)cameraLoc.j, (float)cameraLoc.k, 0, 0, 0, 0, 1, 0);
                strokeWeight(2);
                stroke(255,0,255);
                line ((float)cameraLoc.i, (float)cameraLoc.j, (float)cameraLoc.k, 0, 0, 0);
                noStroke();
            }
            if (axis) {
                stroke (255);
                line (-width/2, 0, 0, width/2, 0, 0);
                line (0, -height/2, 0, 0, height/2, 0);
                noStroke();
            }

            ambientLight (255, 255, 255);
            // render Sol, always at position 0
            solRenderer.render(cameraLoc, trail);
            noLights();
        
            pointLight (255, 255, 255, 0, 0, 0);
            for (int i = renderers.size() - 1; i > 0; i--)
                renderers.get(i).render (cameraLoc, trail);

        } popMatrix();
        
        ambientLight (255, 255, 255);
        annotate();     
        
        if (updating)
            update (dT);
    }
    
    private boolean trail = true;
    private boolean black = false;
    private boolean help = false;
    private boolean axis = false;
    private boolean updating = true;
    private boolean shifted = false; 
    private boolean rideLaunched = false;

    @Override
    public void keyPressed() {
        switch (key) {
            case CODED:
                if (!shifted) {
                    switch (keyCode) {
                        case UP:    MpP /= 2; break;
                        case DOWN:  MpP *= 2; break;
                        case RIGHT: dT *= 2;  break;
                        case LEFT:  dT /= 2;  break;
                        case SHIFT: shifted = true; break;
                    }
                }
                else {
                    if (keyCode == UP)
                        cameraLoc.j += 50;
                    else
                    if (keyCode == DOWN)
                        cameraLoc.j -= 50;
                }
                break;
                
            case '?':  help = true; break;
            case 'x':  axis = !axis; break;
            case 't':  trail = !trail; break;
            case 'r':  setup(); break;
            case 'b':  black = !black; break;
            case 'L':  rideLaunched ^= true; break;
            case ' ':
                updating ^= true;
                frameRate (updating ? 5 : 60);
                break;
        }
    }
    
    @Override
    public void keyReleased() {
        if (key == '?')
            help = false;
        else
        if (key == CODED && keyCode == SHIFT)
            shifted = false;
    }
    
    @Override
    public void mouseWheel (MouseEvent me) {
        int count = me.getCount();
        cameraLoc.k -= count * 10;
    }
    
    public void mouseClicked() {
        if (launched != null)
            return;
        
        launched = new Mass ("Curiosity", 6.4e6, 0, 5.972e3, 1.496e11, -40e3);
        addBody (launched, new MassRenderer (launched, color(255,255,255), null));
    }
    
    public static void main (String[] args) {
        PApplet.main(new String[]{Universe.class.getName()});
    }
}
