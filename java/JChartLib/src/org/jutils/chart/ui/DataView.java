package org.jutils.chart.ui;

import java.awt.*;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jutils.chart.model.Series;
import org.jutils.ui.TitleView;
import org.jutils.ui.model.IView;

public class DataView implements IView<JPanel>
{
    /**  */
    private final JPanel view;
    /**  */
    private final JTabbedPane seriesTabs;

    public DataView()
    {
        this.seriesTabs = new JTabbedPane();
        this.view = createView();
    }

    private JPanel createView()
    {
        JPanel view = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;
        TitleView titleView = new TitleView();

        titleView.setTitle( "Data" );
        titleView.setComponent( seriesTabs );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 4,
                4, 4, 4 ), 0, 0 );
        view.add( titleView.getView(), constraints );

        return view;
    }

    public void addSeries( Series series )
    {
        DataSeriesView view = new DataSeriesView();

        view.setData( series );

        seriesTabs.addTab( series.name, view.getView() );
    }

    public void clearSeries()
    {
        seriesTabs.removeAll();
    }

    @Override
    public JPanel getView()
    {
        return view;
    }
}
