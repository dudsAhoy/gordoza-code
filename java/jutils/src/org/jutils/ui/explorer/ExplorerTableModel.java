package org.jutils.ui.explorer;

import java.io.File;
import java.util.List;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ExplorerTableModel extends AbstractTableModel
{
    /**  */
    public static final String [] HEADERS = new String[] { "Name", "Location",
        "Size (kB)", "Type", "Modified" };

    /**  */
    private Vector<ExplorerItem> contents = new Vector<ExplorerItem>( 1024 );

    /***************************************************************************
     * 
     **************************************************************************/
    public ExplorerTableModel()
    {
        super();
    }

    /***************************************************************************
     * @param row int
     * @param col int
     * @return boolean
     **************************************************************************/
    public boolean isCellEditable( int row, int col )
    {
        return false;
    }

    /***************************************************************************
     * @return int
     **************************************************************************/
    public int getColumnCount()
    {
        return HEADERS.length;
    }

    /***************************************************************************
     * @return int
     **************************************************************************/
    public int getRowCount()
    {
        return contents.size();
    }

    /***************************************************************************
     * @param col int
     * @return String
     **************************************************************************/
    public String getColumnName( int col )
    {
        String name = null;

        if( col > -1 && col < HEADERS.length )
        {
            name = HEADERS[col];
        }

        return name;
    }

    /***************************************************************************
     * @param rowIndex
     * @return
     **************************************************************************/
    public ExplorerItem getExplorerItem( int rowIndex )
    {
        ExplorerItem item = null;

        if( rowIndex > -1 && rowIndex < contents.size() )
        {
            item = ( ExplorerItem )contents.get( rowIndex );
        }

        return item;
    }

    /***************************************************************************
     * @param rowIndex int
     * @param columnIndex int
     * @return Object
     **************************************************************************/
    public Object getValueAt( int rowIndex, int columnIndex )
    {
        ExplorerItem item = ( ExplorerItem )contents.get( rowIndex );
        Object value = null;

        switch( columnIndex )
        {
            case 0:
            {
                value = item;
                break;
            }
            case 1:
            {
                value = "  " + item.getParentPath() + "  ";
                // value = "path";
                break;
            }
            case 2:
            {
                long len = item.getSizeInKb();
                value = len < 0 ? "" : "  " + len + "  ";
                // value = "path";
                break;
            }
            case 3:
            {
                value = "  " + item.getType() + "  ";
                // value = "path";
                break;
            }
            case 4:
            {
                value = "  " + item.getLastModified() + "  ";
                // value = "path";
                break;
            }
            default:
            {
                break;
            }
        }

        return value;
    }

    /***************************************************************************
     * @param files List
     **************************************************************************/
    public void addFiles( List<? extends ExplorerItem> files )
    {
        int start = contents.size();
        int end = 0;
        contents.addAll( files );
        end = contents.size();
        if( end > start )
        {
            fireTableRowsInserted( start, end - 1 );
        }
    }

    /***************************************************************************
     * @param file File
     **************************************************************************/
    public void addFile( File file )
    {
        insertFile( contents.size(), file );
    }

    /***************************************************************************
     * @param row integer
     * @param file File
     **************************************************************************/
    public void insertFile( int row, File file )
    {
        DefaultExplorerItem item = new DefaultExplorerItem( file );
        insertFile( row, item );
    }

    /***************************************************************************
     * @param file File
     **************************************************************************/
    public void addFile( ExplorerItem item )
    {
        insertFile( contents.size(), item );
    }

    /***************************************************************************
     * @param row integer
     * @param file File
     **************************************************************************/
    public void insertFile( int row, ExplorerItem item )
    {
        contents.insertElementAt( item, row );
        fireTableRowsInserted( row, row );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void clearModel()
    {
        int size = contents.size();
        contents.clear();
        if( size > 0 )
        {
            this.fireTableRowsDeleted( 0, size - 1 );
        }
    }
}
