package ultima;

import java.util.ArrayList;

/**
 * Implements z-score based outlier detection by extending remOut to use sorted data.
 * Calculates mean and standard deviation for each column, then marks any data point
 * with |Z| >= 3 as an outlier.
 *
 * @author Andi Guo
 */
public class zScore extends remOut {

    /**
     * Stores the sum of values for each column, used to compute the mean.
     */
    @SuppressWarnings("FieldMayBeFinal")
    private ArrayList<Double> sumVals = new ArrayList<>();

    /**
     * Constructs a z-score processor for the given dataset.
     *
     * @param c   the number of columns
     * @param r   the number of rows per column
     * @param inp the input data as an array of lists for each column
     */
    public zScore(int c, int r, ArrayList<Double>[] inp) {
        super(c, r, inp);
    }

    /**
     * Initializes the sorted data (via super.init()) and computes the sum of values
     * for each column to prepare for mean calculation.
     * This method must be called before any other statistical methods.
     */
    @Override
    public void init() {
        super.init();
        for (int i = 0; i < super.col; i++) {
            double csm = 0.0;
            for (double j : super.val[i]) {
                csm += j;
            }
            sumVals.add(csm);
        }
    }

    /**
     * Computes the mean of a specific column.
     *
     * @param cur the column index
     * @return the arithmetic mean of values in the column
     */
    public Double mean(int cur) {
        return sumVals.get(cur) / super.row;
    }

    /**
     * Computes the population standard deviation of a specific column.
     * Uses the formula sqrt(\u2211(x - mean)^2 / N).
     *
     * @param cur the column index
     * @return the standard deviation of the column's values
     */
    public Double std(int cur) {
        double cmean = mean(cur);
        double sumSq = 0.0;
        for (int i = 0; i < super.row; i++) {
            double diff = super.val[cur].get(i) - cmean;
            sumSq += diff * diff;
        }
        double variance = sumSq / super.row;
        return Math.sqrt(variance);
    }

    /**
     * Marks outliers in the specified column by computing the Z-score for each data point
     * and adding its index to the inherited outs list if |Z| >= 3.
     *
     * @param cur the column index on which to perform z-score outlier detection
     */
    public void zMethod(int cur) {
        double cmean = mean(cur);
        double cstd = std(cur);
        for (int i = 0; i < super.row; i++) {
            double Z = (super.val[cur].get(i) - cmean) / cstd;
            if (Math.abs(Z) >= 3.0) {
                super.outs.add(i);
            }
        }
    }

}
