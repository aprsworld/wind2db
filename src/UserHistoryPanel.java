/* --------------------
 * MemoryUsageDemo.java
 * --------------------
 * (C) Copyright 2002-2006, by Object Refinery Limited.
 */

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;


public class UserHistoryPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private TimeSeries yAxis[];
    

    /**
     * Creates a new application.
     * 
     * @param maxAge  the maximum age (in seconds).
     * @param nChannels total channels to plot
     */
    @SuppressWarnings("deprecation")
	public UserHistoryPanel(int maxAge, int nPlotting, String channelLabels[]) {
        super(new BorderLayout());
        
        yAxis = new TimeSeries[nPlotting];

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        for ( int i=0 ; i<nPlotting ; i++ ) {
        	yAxis[i]=new TimeSeries(channelLabels[i], Second.class);
        	yAxis[i].setMaximumItemAge(maxAge);
        	dataset.addSeries(yAxis[i]);
        }
        
        // create two series that automatically discard data more than 30 
        // seconds old...
        
        
        DateAxis domain = new DateAxis("Date");
        NumberAxis range = new NumberAxis("");
        domain.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        range.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        domain.setLabelFont(new Font("SansSerif", Font.PLAIN, 14));
        range.setLabelFont(new Font("SansSerif", Font.PLAIN, 14));

        XYItemRenderer renderer = new XYLineAndShapeRenderer(true, false);
        
        //renderer.setSeriesPaint(0, Color.blue); /* wind speed */
        //renderer.setSeriesPaint(1, Color.red);  /* wind gust */
        
        renderer.setStroke(new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
        XYPlot plot = new XYPlot(dataset, domain, range, renderer);
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        //plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        domain.setAutoRange(true);
        domain.setLowerMargin(0.0);
        domain.setUpperMargin(0.0);
        domain.setTickLabelsVisible(true);

        range.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        //String title=new String("Over last " + maxAge + " seconds");
       
        
        JFreeChart chart = new JFreeChart("", new Font("SansSerif", Font.BOLD, 12), plot, true);
        chart.setBackgroundPaint(Color.white);
        
        //setMaximumDrawHeight(double height);
        ChartPanel chartPanel = new ChartPanel(chart, true);
        //chartPanel.setBorder(BorderFactory.createCompoundBorder(
        //		BorderFactory.createEmptyBorder(4, 4, 4, 4),
        //        BorderFactory.createLineBorder(Color.black)));
        //chartPanel.setMaximumDrawHeight(140);
//        chartPanel.setSize(300,100);
        chartPanel.setPreferredSize(new Dimension(400, 200));
        add(chartPanel);

    }
    
    public Second addPoint(int ch, double value) {
    	Second s=new Second();
    	yAxis[ch].addOrUpdate(s,value);
    	return s;
    }
    
    public void addPoint(Second s, int ch, double value) {
    	yAxis[ch].addOrUpdate(s,value);
    }
    
}
