package org.jutils.concurrent;

public interface IConsumer<T>
{
    /***************************************************************************
     * Function to be called when there is data to be processed. <p> The data
     * passed is assumed to be the sole property of this thread. If you suspect
     * this is not the case, simply surround the body of this function with a
     * synchronized block that synchronizes on the object passed to it: </p>
     * <hr> <blockquote> <pre> synchronized( obj ) { //Function body would
     * normally go here. . . . } </pre> </blockquote>
     * @param obj The data to be processed.
     **************************************************************************/
    public void consume( T data, ITaskStopManager stopper );
}
