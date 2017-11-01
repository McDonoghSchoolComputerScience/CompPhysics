package graph;

import processing.core.PApplet;

/**
 * Push provides the core of a Decorator pattern. Push objects are intended as
 * pipelines through which elements are pushed, some of those reaching the end
 * of the pipeline, others disappearing based on specific filters.
 * 
 * @author smithk
 * @param <T> the type of the pushed entity, typically a Vector or Point
 */
public interface Push<T> {
    boolean push (T pushed);
}
