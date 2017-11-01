/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planetary;

import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PShape;
import vector.Vector;

/**
 *
 * @author smithk
 */
public class MassRenderer {
    private static PApplet p;
    
    private final Mass mass;
    
    private final int fillColor;
    
    private int mSize;
    
    private PImage sphereTexture;
    private PShape texturedSphere;
    private PShape filledSphere;

    private ArrayList<Vector> pastPositions;
    private float lastAngle;
    private float scaled;
    
    private double cachedMpP;

    public MassRenderer (Mass mass, int fillColor, String texture) {
        this.mass = mass;
        this.fillColor = fillColor;

        cachedMpP = 0;
        pastPositions = new ArrayList();
        lastAngle = (float)mass.getAngleOfRotation();
        
        p.fill(fillColor);
        p.noStroke();
        filledSphere = p.createShape (p.SPHERE, 500);
        
        if (texture != null) {
            sphereTexture = p.loadImage (texture);
            if (sphereTexture != null) {
                texturedSphere = p.createShape (p.SPHERE, 500);
                texturedSphere.setTexture(sphereTexture);
            }
        }
        
        scaled = 500;
    }    
    
    public static void setPApplet (PApplet p) {
        MassRenderer.p = p;
    }
    
    public void resizeSphere () {
        mSize = (int)Math.pow(mass.getRadius(), 0.16);
        cachedMpP = Universe.MpP;

        float sf = mSize / scaled;
        scaled = mSize;
        
        filledSphere.scale (sf);
        if (texturedSphere != null)
            texturedSphere.scale (sf);
    }

    public void render(Vector cameraLoc, boolean trail) {
        if (cachedMpP != Universe.MpP)
            resizeSphere();
        
        Vector position = mass.getPosition();
        float x = (float) (position.i / Universe.MpP);
        float y = (float) (position.j / Universe.MpP);
        float z = (float) (position.k / Universe.MpP);
                
        int alpha = (int)p.degrees((float)(2 * Math.atan (mSize/2 / (cameraLoc.k - z))));
        
        p.pushMatrix();
        p.translate (x, y, z);
        
        if (alpha > 2 && texturedSphere != null) {
            float currentAngle = mass.getAngleOfRotation();
            float dA = currentAngle - lastAngle;
            lastAngle = currentAngle;
            texturedSphere.rotateY(dA);
            p.noStroke();
            p.shape (texturedSphere);
        }
        else
            p.shape (filledSphere);
        
        p.popMatrix();

        if (trail) {
            p.strokeWeight(1);
            p.stroke(fillColor);
            for (Vector past : pastPositions) {
                p.point((float) (past.i / Universe.MpP), (float)(past.j / Universe.MpP), (float) (past.k / Universe.MpP));
            }
            p.noStroke();
        }
        
        pastPositions.add(new Vector(position.i, position.j, position.k));
        if (pastPositions.size() > 500) {
            pastPositions.remove(0);
        }
    }
}
