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
import org.jfree.chart.axis.CompassFormat;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.TickUnits;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.*;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;


public class WindDirectionHistoryPanel extends JPanel {

    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TimeSeries windDirection;

    /**
     * Creates a new application.
     * 
     * @param maxAge  the maximum age (in seconds).
     */
    @SuppressWarnings("deprecation")
	public WindDirectionHistoryPanel(int maxAge) {

        super(new BorderLayout());

        /* setup our series */
        windDirection = new TimeSeries("Wind Direction", Second.class);
        windDirection.setMaximumItemAge(maxAge);
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(windDirection);

        
        /* create our x-axis and y-axis */
        DateAxis domain = new DateAxis("Time");
        NumberAxis range = new NumberAxis("Direction");
        
        
        domain.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        range.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        domain.setLabelFont(new Font("SansSerif", Font.PLAIN, 14));
        range.setLabelFont(new Font("SansSerif", Font.PLAIN, 14));
    
        
       
        /* setup the labels for our y-axis */
        TickUnits units = new TickUnits();
        units.add(new NumberTickUnit(180.0, new CompassFormat()));
        units.add(new NumberTickUnit(90.0, new CompassFormat()));
        units.add(new NumberTickUnit(45.0, new CompassFormat()));
        units.add(new NumberTickUnit(22.5, new CompassFormat()));
        range.setStandardTickUnits(units);
        
        
        XYItemRenderer renderer = new XYLineAndShapeRenderer(true,false);
        


        renderer.setSeriesPaint(0, Color.blue); /* direction */
        renderer.setStroke(new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
        XYPlot plot = new XYPlot(dataset, domain, range, renderer);
        
        
        /* 360 degrees showing on the y-axis at all times */
        range.setLowerMargin(0.0);
        //range.setUpperMargin(180.0);
        
        /* only as much as we need */
        domain.setLowerMargin(0.0);
        domain.setUpperMargin(0.0);
        domain.setAutoRange(true);
        domain.setTickLabelsVisible(true);

        
        /* set our colors */
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        

       // range.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
      
        JFreeChart chart = new JFreeChart("", new Font("SansSerif", Font.BOLD, 12), plot, false);
        chart.setBackgroundPaint(Color.white);
        
        ChartPanel chartPanel = new ChartPanel(chart, true);
        chartPanel.setPreferredSize(new Dimension(400, 330));
        add(chartPanel);

    }
    
    public void addWindDirection(int dir) {
    	Second s=new Second();
    	this.windDirection.addOrUpdate(s, dir);
    }
    
    public void addWindDirection(Second s, int dir) {
    	this.windDirection.addOrUpdate(s, dir);
    }
    
}
