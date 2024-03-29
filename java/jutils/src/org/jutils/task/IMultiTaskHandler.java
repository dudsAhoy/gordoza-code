package org.jutils.task;

/*******************************************************************************
 * Defines the functions that check for user control and reports status of a
 * {@link TaskPool}.
 ******************************************************************************/
public interface IMultiTaskHandler
{
    /***************************************************************************
     * Gets the next task in the queue. This function is guaranteed to be called
     * in a thread-safe manner.
     * @return the next task in the queue.
     **************************************************************************/
    public ITask nextTask();

    /***************************************************************************
     * Returns {@code true} if processing can continue; {@code false} otherwise.
     **************************************************************************/
    public boolean canContinue();

    /***************************************************************************
     * Creates an {@link ITaskView} and initializes the view's message field to
     * the provided task's name.
     * @return a view for displaying the provided task.
     **************************************************************************/
    public ITaskView createView( String taskName );

    /***************************************************************************
     * @param taskError
     **************************************************************************/
    public void signalError( TaskError error );

    /***************************************************************************
     * @param view
     **************************************************************************/
    public void removeView( ITaskView view );

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getTaskCount();

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void signalPercent( int percent );

    /***************************************************************************
     * @param message
     **************************************************************************/
    public void signalMessage( String message );
}
