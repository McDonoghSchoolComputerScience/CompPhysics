/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

/**
 *
 * @author smithk
 */
public class BasePush<T> implements Push<T> {
    private Push<T> isDownStream;
    
    @Override
    public boolean push(final T pushed) {
        if (isDownStream != null)
            return isDownStream.push (pushed);
        return false;
    }
    
    public void downStream (final Push<T> isDownStream) {
        this.isDownStream = isDownStream;
    }
}
