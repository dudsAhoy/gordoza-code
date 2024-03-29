package org.jutils.chart.ui.objects;

import java.awt.*;

import org.jutils.SwingUtils;
import org.jutils.chart.ChartUtils;
import org.jutils.chart.data.*;
import org.jutils.chart.data.ChartContext.IDimensionCoords;
import org.jutils.chart.model.*;
import org.jutils.chart.ui.IChartWidget;
import org.jutils.chart.ui.Layer2d;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SeriesWidget implements IChartWidget
{
    /**  */
    public final Chart chart;
    /**  */
    public final Series series;

    /**  */
    public final IMarker marker;
    /**  */
    public final IMarker selectedMarker;
    /**  */
    public final CircleBorderMarker highlight;
    /**  */
    public final ILine line;

    /**  */
    public final ChartContext context;
    /**  */
    public boolean trackPoint;

    /***************************************************************************
     * @param data
     **************************************************************************/
    public SeriesWidget( Chart chart, Series series, ChartContext context )
    {
        this.chart = chart;
        this.series = series;
        this.context = context;

        this.marker = new CircleMarker();
        this.selectedMarker = new CircleMarker();
        this.highlight = new CircleBorderMarker();
        this.line = new SimpleLine();

        marker.setColor( series.marker.color );
        highlight.setColor( series.highlight.color );
        line.setColor( series.line.color );
        line.setSize( series.line.weight );
        selectedMarker.setColor( SwingUtils.inverseColor( series.marker.color ) );

        trackPoint = true;

        highlight.setSize( 10 );
    }

    /***************************************************************************
     *
     **************************************************************************/
    @Override
    public void draw( Graphics2D graphics, Point location, Dimension size )
    {
        if( !series.visible )
        {
            return;
        }

        marker.setColor( series.marker.color );
        marker.setSize( series.marker.weight );
        highlight.setColor( series.highlight.color );
        line.setColor( series.line.color );
        line.setSize( series.line.weight );
        selectedMarker.setColor( SwingUtils.inverseColor( series.marker.color ) );

        Point p = new Point();
        Point last = new Point( -100, -100 );
        IDataPoint dp;
        Bounds b = context.getBounds();

        // LogUtils.printDebug( "series: " + series.name + " weight: " +
        // series.marker.weight );

        Span spanx = series.isPrimaryDomain ? b.primaryDomainSpan
            : b.secondaryDomainSpan;
        // Span spany = series.isPrimaryRange ? context.primaryRangeSpan
        // : context.secondaryRangeSpan;

        IDimensionCoords domain = series.isPrimaryDomain ? context.domain.primary
            : context.domain.secondary;
        IDimensionCoords range = series.isPrimaryRange ? context.range.primary
            : context.range.secondary;

        if( spanx == null )
        {
            return;
        }

        int start = ChartUtils.findNearest( series.data, spanx.min ) - 2;
        int end = ChartUtils.findNearest( series.data, spanx.max ) + 2;

        start = Math.max( start, 0 );
        end = Math.min( end, series.data.getCount() );

        // LogUtils.printDebug( "series: start: " + start + ", end: " + end );

        Layer2d markerLayer = new Layer2d();
        Layer2d selectedLayer = new Layer2d();
        int d = series.marker.weight + 2;
        int r = d / 2 + 1;

        if( series.marker.visible )
        {
            Graphics2D g2d;
            Point mp = new Point( r, r );

            g2d = markerLayer.setSize( d, d );
            marker.setLocation( mp );
            marker.draw( g2d, mp, null );

            g2d = selectedLayer.setSize( d, d );
            selectedMarker.setLocation( mp );
            selectedMarker.draw( g2d, mp, null );
        }

        for( int i = start; i < end; i++ )
        {
            dp = series.data.get( i );

            if( dp.isHidden() )
            {
                continue;
            }

            p.x = domain.fromCoord( dp.getX() );
            p.y = range.fromCoord( dp.getY() );

            if( p.x != last.x || p.y != last.y )
            {
                if( series.line.visible && last.x != -100 )
                {
                    line.setPoints( last, p );

                    line.draw( graphics, p, size );
                }

                if( series.marker.visible )
                {
                    Layer2d l2d = dp.isSelected() ? selectedLayer : markerLayer;

                    // m.draw( graphics, p, size );
                    l2d.paint( graphics, p.x - r, p.y - r );
                }

                last.x = p.x;
                last.y = p.y;
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Dimension calculateSize( Dimension canvasSize )
    {
        return null;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void clearSelected()
    {
        for( IDataPoint xy : series.data )
        {
            xy.setSelected( false );
        }
    }

    /***************************************************************************
     * @param domain
     * @param range
     **************************************************************************/
    public void setSelected( Span domain, Span range )
    {
        for( IDataPoint xy : series.data )
        {
            if( domain.min <= xy.getX() && xy.getX() <= domain.max &&
                range.min <= xy.getY() && xy.getY() <= range.max )
            {
                xy.setSelected( true );
            }
        }
    }
}
