package org.jutils.concurrent;

import java.util.concurrent.LinkedBlockingQueue;

/*******************************************************************************
 * Used to process data that is collected by a separate thread. When this task
 * has data AND has a chance to process said data, it will call the sub-class's
 * {@link #processData(Object)} function.
 ******************************************************************************/
public class Consumer<T> implements IStoppable
{
    /** {@code true} if input should be accepted, {@code false} otherwise. */
    private volatile boolean acceptInput;
    /** List of data to be consumed. */
    private final LinkedBlockingQueue<T> data;
    /**  */
    private final IConsumer<T> consumer;

    /***************************************************************************
     * Creates a new Abstract Consumer
     **************************************************************************/
    public Consumer( IConsumer<T> consumer )
    {
        this.consumer = consumer;
        this.acceptInput = true;
        this.data = new LinkedBlockingQueue<T>();
    }

    /***************************************************************************
     * Called by the start() function. DO NOT CALL DIRECTLY. This function is to
     * be called by {@link Thread#start()}.
     **************************************************************************/
    @Override
    public final void run( IStopper stopper )
    {
        T obj = null;

        while( stopper.continueProcessing() )
        {
            if( acceptInput )
            {
                try
                {
                    obj = data.take();
                }
                catch( InterruptedException e )
                {
                    obj = data.poll();
                }
            }
            else
            {
                obj = data.poll();
            }

            if( obj != null )
            {
                consumer.consume( obj, stopper );
                obj = null;
            }
            else
            {
                break;
            }
        }

        stopAcceptingInput();
    }

    /***************************************************************************
     * @return {@code true} if the task is accepting input, {@code false}
     * otherwise.
     **************************************************************************/
    public boolean isAcceptingInput()
    {
        return acceptInput;
    }

    /***************************************************************************
     * Flags the task to stop accepting input, but the user must ensure that the
     * Thread running this task is interrupted.
     **************************************************************************/
    public void stopAcceptingInput()
    {
        acceptInput = false;
    }

    /***************************************************************************
     * Adds data to this Thread to be processed when the thread is able.
     * @param obj The data to be processed.
     * @throws NullPointerException if the specified element is null.
     **************************************************************************/
    public final void addData( T obj ) throws NullPointerException
    {
        if( acceptInput )
        {
            try
            {
                data.put( obj );
            }
            catch( InterruptedException e )
            {
            }
        }
    }

    /***************************************************************************
     * @return the number of elements that have not been processed.
     **************************************************************************/
    public int getNumItemsLeft()
    {
        return data.size();
    }
}