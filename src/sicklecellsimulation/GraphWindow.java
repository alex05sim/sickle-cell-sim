package sicklecellsimulation;

import javax.swing.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import java.awt.*;

public class GraphWindow extends JFrame {
    private final XYSeries healthySeries, carrierSeries, sickleSeries;
    private final XYSeriesCollection dataset;

    public GraphWindow() {
        setTitle("Sickle Cell Simulation - Population Graph");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        // Create dataset
        healthySeries = new XYSeries("Healthy");
        carrierSeries = new XYSeries("Carriers");
        sickleSeries = new XYSeries("Sickle Cell");

        dataset = new XYSeriesCollection();
        dataset.addSeries(healthySeries);
        dataset.addSeries(carrierSeries);
        dataset.addSeries(sickleSeries);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Population Trends Over Time",
                "Generations",
                "Population",
                dataset
        );

        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        renderer.setSeriesPaint(0, Color.BLUE); // Healthy
        renderer.setSeriesPaint(1, new Color(128, 0, 128)); // Carrier (Purple)
        renderer.setSeriesPaint(2, Color.RED); // Sickle Cell
        plot.setRenderer(renderer);

        // Display graph
        ChartPanel chartPanel = new ChartPanel(chart);
        add(chartPanel);
        setVisible(true);
    }
    public void updateGraph(int generation, int healthy, int carriers, int sickle) {
        healthySeries.add(generation, healthy);
        carrierSeries.add(generation, carriers);
        sickleSeries.add(generation, sickle);
    }
    public void clearGraph() {
        healthySeries.clear();
        carrierSeries.clear();
        sickleSeries.clear();
    }


}

