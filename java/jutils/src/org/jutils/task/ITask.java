package org.jutils.task;

/*******************************************************************************
 * 
 ******************************************************************************/
public interface ITask
{
    /***************************************************************************
     * @param handler
     **************************************************************************/
    public void run( ITaskHandler handler );

    /***************************************************************************
     * @return
     **************************************************************************/
    public String getName();
}
