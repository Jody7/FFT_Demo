import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;
import org.jfree.ui.ApplicationFrame;

import java.awt.*;


/**
 * Created by Jody on 9/28/2016.
 */
public class Graph extends ApplicationFrame {
    public Graph(final String title, DefaultCategoryDataset data) {
        super(title);
        JFreeChart chart = ChartFactory.createLineChart(
        title, "Normalized Freq", "Magnitude", data, PlotOrientation.VERTICAL, true, true, false
        );
        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new Dimension(500,700));
        setContentPane(panel);
    }
}
