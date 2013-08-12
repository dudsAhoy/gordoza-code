package org.jutils.ui.model;

/*******************************************************************************
 * 
 ******************************************************************************/
public interface IDataView<T> extends IComponentView
{
    /***************************************************************************
     * @return
     **************************************************************************/
    public T getData();

    /***************************************************************************
     * @param data
     **************************************************************************/
    public void setData( T data );
}
