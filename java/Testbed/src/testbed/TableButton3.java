package testbed;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.*;
import javax.swing.table.*;

import org.jutils.io.LogUtils;

import com.jgoodies.looks.Options;

public class TableButton3 extends JFrame
{
    public TableButton3()
    {
        String[] columnNames = { "Date", "String", "Integer", "Decimal", "" };
        Object[][] data = {
            { new Date(), "A", new Integer( 1 ), new Double( 5.1 ), "Delete0" },
            { new Date(), "B", new Integer( 2 ), new Double( 6.2 ), "Delete1" },
            { new Date(), "C", new Integer( 3 ), new Double( 7.3 ), "Delete2" },
            { new Date(), "D", new Integer( 4 ), new Double( 8.4 ), "Delete3" } };

        DefaultTableModel model = new DefaultTableModel( data, columnNames );
        JTable table = new JTable( model )
        {
            // Returning the Class of each column will allow different
            // renderers to be used based on Class
            public Class<?> getColumnClass( int column )
            {
                return getValueAt( 0, column ).getClass();
            }
        };
        table.setRowHeight( 30 );

        JScrollPane scrollPane = new JScrollPane( table );
        getContentPane().add( scrollPane );

        // Create button column
        new ButtonColumn( table, 4 );
    }

    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( new Runnable()
        {
            public void run()
            {
                try
                {
                    UIManager.setLookAndFeel( "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel" );
                    Options.setSelectOnFocusGainEnabled( true );
                }
                catch( Exception ex )
                {
                    ex.printStackTrace();
                }
                TableButton3 frame = new TableButton3();
                frame.setDefaultCloseOperation( EXIT_ON_CLOSE );
                frame.pack();
                frame.setVisible( true );
            }
        } );
    }

    private static class ButtonColumn extends AbstractCellEditor implements
        TableCellRenderer, TableCellEditor, ActionListener
    {
        JTable table;

        JButton renderButton;

        JButton editButton;

        String text;

        public ButtonColumn( JTable table, int column )
        {
            super();
            this.table = table;
            renderButton = new JButton();

            editButton = new JButton();
            editButton.setFocusPainted( false );
            editButton.addActionListener( this );

            TableColumnModel columnModel = table.getColumnModel();
            columnModel.getColumn( column ).setCellRenderer( this );
            columnModel.getColumn( column ).setCellEditor( this );
        }

        public Component getTableCellRendererComponent( JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row,
            int column )
        {
            if( hasFocus )
            {
                renderButton.setForeground( table.getForeground() );
                renderButton.setBackground( UIManager.getColor( "Button.background" ) );
            }
            else if( isSelected )
            {
                renderButton.setForeground( table.getSelectionForeground() );
                renderButton.setBackground( table.getSelectionBackground() );
            }
            else
            {
                renderButton.setForeground( table.getForeground() );
                renderButton.setBackground( UIManager.getColor( "Button.background" ) );
            }

            renderButton.setText( ( value == null ) ? "" : value.toString() );
            return renderButton;
        }

        public Component getTableCellEditorComponent( JTable table,
            Object value, boolean isSelected, int row, int column )
        {
            text = ( value == null ) ? "" : value.toString();
            editButton.setText( text );
            return editButton;
        }

        public Object getCellEditorValue()
        {
            return text;
        }

        public void actionPerformed( ActionEvent e )
        {
            fireEditingStopped();
            LogUtils.printDebug( e.getActionCommand() + " : " +
                table.getSelectedRow() );
        }
    }
}
