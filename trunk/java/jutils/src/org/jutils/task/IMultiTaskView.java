package org.jutils.task;

/*******************************************************************************
 * 
 ******************************************************************************/
public interface IMultiTaskView
{
    /***************************************************************************
     * @param title
     * @param message
     * @return
     **************************************************************************/
    public ITaskView addTask( String message );

    /***************************************************************************
     * @param view
     **************************************************************************/
    public void removeTask( ITaskView view );

    /***************************************************************************
     * @param title
     **************************************************************************/
    public void setTitle( String title );

    /***************************************************************************
     * @param percent
     **************************************************************************/
    public void setPercent( int percent );

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean canContinue();

    /***************************************************************************
     * @param error
     **************************************************************************/
    public void signalError( TaskError error );
}
