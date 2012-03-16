package org.jutils.ui;

import java.awt.*;

import javax.swing.JPanel;
import javax.swing.UIManager;

public class GradientPanel extends JPanel
{
    public GradientPanel()
    {
        this( null, null );
    }

    public GradientPanel( LayoutManager lm )
    {
        this( lm, null );
    }

    public GradientPanel( LayoutManager lm, Color background )
    {
        super( lm );
        setBackground( background );
    }

    public void setBackground( Color bg )
    {
        if( bg != null )
        {
            super.setBackground( bg );
        }
        else
        {
            super.setBackground( getDefaultBackground() );
        }
    }

    /**
     * Determines and answers the header's background color. Tries to lookup a
     * special color from the L&amp;F. In case it is absent, it uses the
     * standard internal frame background.
     * @return the color of the header's background
     */
    protected Color getDefaultBackground()
    {
        Color c = UIManager.getColor( "ProgressBar.foreground" );
        return c != null ? c
            : UIManager.getColor( "InternalFrame.activeTitleBackground" );
    }

    public void paintComponent( Graphics g )
    {
        super.paintComponent( g );
        if( !isOpaque() )
        {
            return;
        }
        Color control = UIManager.getColor( "control" );
        int width = getWidth();
        int height = getHeight();

        Graphics2D g2 = ( Graphics2D )g;
        Paint storedPaint = g2.getPaint();
        g2.setPaint( new GradientPaint( 0, 0, getBackground(), width, 0,
            control ) );
        g2.fillRect( 0, 0, width, height );
        g2.setPaint( storedPaint );
    }
}
