/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nbody;

/**
 *
 * @author smithk
 */
public class Simulation {
    public static void main(String[] args) throws InterruptedException {
        Quadrant theFinalFrontier = new Quadrant(100, Quadrant.LY, Quadrant.LY);
        Visualizer theView = new Visualizer (theFinalFrontier, 500, 500, Quadrant.LY/500);
        while (true) {
            if (theView.isRedrawn())
                theFinalFrontier.update(null);
            theView.update();
        }
    }
}
