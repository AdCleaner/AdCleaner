package cz.cuni.adcleaner.utilities;

import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

import javax.swing.*;

public class LineChartFrame extends ApplicationFrame {

	private static final long serialVersionUID = 9210418545078317152L;
	private final String xAxisLabel;
	private final String yAxisLabel;

	public LineChartFrame(final String title, final String xAxisLabel, final String yAxisLabel, XYSeries series) {
		super(title);
		this.xAxisLabel = xAxisLabel;
		this.yAxisLabel = yAxisLabel;

		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series);
		final JFreeChart chart = createChart(dataset);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		setContentPane(chartPanel);

                super.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                super.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}

	private JFreeChart createChart(final XYDataset dataset) {

		final JFreeChart chart = ChartFactory.createXYLineChart("", // chart
																	// title
				xAxisLabel, // x axis label
				yAxisLabel, // y axis label
				dataset, // data
				PlotOrientation.VERTICAL, true, // include legend
				true, // tooltips
				false // urls
				);

		chart.setBackgroundPaint(Color.white);

		final XYPlot plot = chart.getXYPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);

		final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, false);
		plot.setRenderer(renderer);

		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		return chart;
	}

    @Override
    public void windowClosing(java.awt.event.WindowEvent event)
    {
        // Only close window, do not call System.exit(0) as parent does.
        this.setVisible(false);
    }
}