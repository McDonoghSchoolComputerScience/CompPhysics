/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

import vector.Vector;

/**
 *
 * @author smithk
 */
public class HiFilter<T> extends BasePush<T> {
    private Push<T> downStream;
    
    public HiFilter (Push<T> downStream) {
        this.downStream = downStream;
    }
    @Override
    public boolean push(T pushed) {
        if (pushed instanceof Vector && ((Vector)pushed).mag < 100) {
            downStream.push (pushed);
            return true;
        }
        return false;
    }
}
