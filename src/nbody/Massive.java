/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nbody;

import vector.Vector;

/**
 *
 * @author smithk
 */
public interface Massive {
    double getMass();
    Vector getVelocity();
    Vector getPosition();
}
