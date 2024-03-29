package testbed;

import java.awt.Container;
import java.awt.event.*;
import java.util.Locale;

import javax.swing.*;
import javax.swing.text.*;

public class AutoComplete<T> extends JComboBox<T> implements
    JComboBox.KeySelectionManager
{
    private String searchFor;

    private long lap;

    public class CBDocument extends PlainDocument
    {
        public void insertString( int offset, String str, AttributeSet a )
            throws BadLocationException
        {
            if( str == null )
                return;
            super.insertString( offset, str, a );
            if( !isPopupVisible() && str.length() != 0 )
                fireActionEvent();
        }
    }

    public AutoComplete( T[] items )
    {
        super( items );
        lap = new java.util.Date().getTime();
        setKeySelectionManager( this );
        JTextField tf;
        if( getEditor() != null )
        {
            tf = ( JTextField )getEditor().getEditorComponent();
            if( tf != null )
            {
                tf.setDocument( new CBDocument() );
                addActionListener( new ActionListener()
                {
                    @Override
                    public void actionPerformed( ActionEvent evt )
                    {
                        JTextField tf = ( JTextField )getEditor().getEditorComponent();
                        String text = tf.getText();
                        ComboBoxModel<T> aModel = getModel();
                        String current;
                        for( int i = 0; i < aModel.getSize(); i++ )
                        {
                            current = aModel.getElementAt( i ).toString();
                            if( current.toLowerCase().startsWith(
                                text.toLowerCase() ) )
                            {
                                tf.setText( current );
                                tf.setSelectionStart( text.length() );
                                tf.setSelectionEnd( current.length() );
                                break;
                            }
                        }
                    }
                } );
            }
        }
    }

    @Override
    public int selectionForKey( char aKey,
        @SuppressWarnings( "rawtypes") ComboBoxModel aModel )
    {
        long now = new java.util.Date().getTime();
        if( searchFor != null && aKey == KeyEvent.VK_BACK_SPACE &&
            searchFor.length() > 0 )
        {
            searchFor = searchFor.substring( 0, searchFor.length() - 1 );
        }
        else
        {
            // LogUtils.printDebug(lap);
            // Kam nie hier vorbei.
            if( lap + 1000 < now )
                searchFor = "" + aKey;
            else
                searchFor = searchFor + aKey;
        }
        lap = now;
        String current;
        for( int i = 0; i < aModel.getSize(); i++ )
        {
            current = aModel.getElementAt( i ).toString().toLowerCase();
            if( current.toLowerCase().startsWith( searchFor.toLowerCase() ) )
                return i;
        }
        return -1;
    }

    @Override
    public void fireActionEvent()
    {
        super.fireActionEvent();
    }

    public static void main( String arg[] )
    {
        JFrame f = new JFrame( "AutoCompleteComboBox" );
        f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        f.setSize( 200, 300 );
        Container cp = f.getContentPane();
        cp.setLayout( null );
        // String[] names= {"Beate", "Claudia", "Fjodor", "Fred", "Friedrich",
        // "Fritz", "Frodo", "Hermann", "Willi"};
        // JComboBox cBox= new AutoComplete(names);
        Locale[] locales = Locale.getAvailableLocales();//
        JComboBox<Locale> cBox = new AutoComplete<Locale>( locales );
        cBox.setBounds( 50, 50, 100, 21 );
        cBox.setEditable( true );
        cp.add( cBox );
        f.setVisible( true );
    }
}
