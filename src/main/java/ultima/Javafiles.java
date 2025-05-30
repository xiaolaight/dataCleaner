package ultima;

import java.awt.Dimension;
import java.util.*;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;

/**
 * A javaa  application that reads numerical data from a CSV with entirely numerical information,
 * removes statistical outliers, and displays the results as a box-and-whisker chart.
 *
 * @author Andi Guo
 */

public class Javafiles extends ApplicationFrame {

    /**
     * A list to store column titles read from the CSV file.
     */
    public static ArrayList<String> titlesArr = new ArrayList<>();

    /**
     * Constructs the chart application frame with the given title.
     *
     * @param title the title for the application window
     */
    public Javafiles(String title){
        super(title);
        JPanel curPanel = makePanel();
        curPanel.setPreferredSize(new Dimension(270, 270));
        setContentPane(curPanel);
    }

    /**
     * Reads a CSV file of numerical data and returns its columns as an array of value lists.
     * It also populates {@code titlesArr} with the column headings.
     *
     * @return an array of {@code ArrayList<Double>}, each containing the values for one column
     */
    public static ArrayList<Double>[] makeVals(){

        String file_path = "C:\\Users\\andig\\Downloads\\airtravel.csv";
        csvReader read = new csvReader(file_path);
        read.getCols();
        int fsz = read.getLen();
        ArrayList<Double> vals[] = new ArrayList[fsz];
        for (int i=0; i<fsz; i++){
            vals[i] = new ArrayList<>();
        }

        read.fileText(vals);

        for (int i=0; i<fsz; i++){
            titlesArr.add(read.titles.get(i));
        }

        return vals;


    }

    /**
     * Applies Winsorization and z-score methods to remove outliers from the data.
     *
     * @param vals the original dataset as an array of value lists
     * @return a new array of lists containing only the non-outlier values
     */
    public static ArrayList<Double>[] makeOutlier(ArrayList<Double>[] vals){

        int rc = vals[0].size();
        int fsz = vals.length;

        winsorMethod prune = new winsorMethod(fsz, rc, vals, 1);
        prune.init();
        for (int i=0; i<fsz; i++){
            prune.iqr(i);
        }

        ArrayList<Double> copyVal[] = new ArrayList[fsz];
        int ind = 0;
        for (ArrayList<Double> vec : prune.val) {
            copyVal[ind++] = new ArrayList<>(vec);
        }
        zScore prune2 = new zScore(fsz, rc, copyVal);
        prune2.init();
        for (int i=0; i<fsz; i++){
            prune2.zMethod(i);
        }

        Set<Integer> allout = new HashSet<>();

        for (int i : prune2.outs){
            allout.add(i);
        }
        for (int i : prune.outs){
            allout.add(i);
        }

        ArrayList<Double> retVal[] = new ArrayList[fsz];

        for (int i=0; i<fsz; i++){
            retVal[i] = new ArrayList<>();
        }

        for (int i=0; i<rc; i++){
            if (allout.contains(i)){
                continue;
            }
            for (int j=0; j<fsz; j++){
                retVal[j].add(vals[j].get(i));
            }
        }

        return retVal;

    }

    /**
     * Converts filtered data into a BoxAndWhiskerCategoryDataset for plotting.
     *
     * @param vals the outlier-free data arrays
     * @return a dataset suitable for creating a box-and-whisker chart
     */
    public static BoxAndWhiskerCategoryDataset makeDataset(ArrayList<Double>[] vals){

        DefaultBoxAndWhiskerCategoryDataset ds = new DefaultBoxAndWhiskerCategoryDataset();

        int fsz = vals.length;

        for (int i=0; i<fsz; i++){
            ArrayList<Double> dataPoints = new ArrayList<>(vals[i]);
            ds.add(dataPoints, titlesArr.get(i), "Datasets");
        }

        return ds;

    }

    /**
     * Builds a JFreeChart box-and-whisker chart from the given dataset.
     *
     * @param ds the dataset containing category statistics
     * @param t  the chart title
     * @return a configured {@code JFreeChart} instance
     */
    public static JFreeChart makeChart(BoxAndWhiskerCategoryDataset ds, String t){

        JFreeChart chart = ChartFactory.createBoxAndWhiskerChart(
                t, "Category", "Value", ds,
                true);
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setDomainGridlinesVisible(true);
        plot.setRangePannable(true);

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        BoxAndWhiskerRenderer rend = new BoxAndWhiskerRenderer();
        rend.setItemMargin(0.5);
        rend.setMaximumBarWidth(0.1);
        plot.setRenderer(rend);

        return chart;

    }

    /**
     * Creates a JPanel containing the box-and-whisker chart after reading,
     * filtering, and dataset construction.
     *
     * @return a {@code ChartPanel} ready for display
     */
    public static JPanel makePanel(){

        JFreeChart graph = makeChart(makeDataset(makeOutlier(makeVals())), "Dataset (Outliers Removed)");
        ChartPanel panel = new ChartPanel(graph);
        panel.setMouseWheelEnabled(true);
        panel.setPreferredSize(new Dimension(270, 270));
        return panel;

    }

    /**
     * The main entry point that launches the application window.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {

        Javafiles graph = new Javafiles("Outliers Removed");
        graph.pack();
        RefineryUtilities.centerFrameOnScreen(graph);
        graph.setVisible(true);

    }

}
