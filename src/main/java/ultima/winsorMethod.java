package ultima;

import java.util.ArrayList;

/**
 * Implements the Winsorization method for outlier removal by capping values
 * below and above specified percentiles.
 * Extends remOut to leverage merge-sorted data of each column.
 *
 * @author Andi Guo
 */
public class winsorMethod extends remOut {

    /**
     * The percentile threshold (e.g., 1 for 1%) to use for capping.
     */
    public int perc;

    /**
     * Constructs a Winsorization processor for the given dataset.
     *
     * @param c   the number of columns
     * @param r   the number of rows per column
     * @param arr the input data as an array of lists for each column
     * @param p   the percentile (0-100) to define lower and upper caps
     */
    public winsorMethod(int c, int r, ArrayList<Double>[] arr, int p) {
        super(c, r, arr);
        this.perc = p;
    }

    /**
     * Finds the value at a given percentile for a specific column using binary search.
     * The data in val[cur] must be sorted before calling.
     *
     * @param cur the column index to search
     * @param per the desired percentile (0-100)
     * @return the data value at the specified percentile
     */
    public Double percentile(int cur, double per) {
        int lo = 0, hi = super.row - 1, ans = 0;
        while (lo <= hi) {
            int mid = (lo + hi) / 2;
            if (((double) mid / super.row) <= per / 100.0) {
                ans = mid;
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }
        return val[cur].get(ans);
    }

    /**
     * Identifies and records outlier indices based on the lower and upper percentile caps.
     * Values below the lower cap or above the upper cap are marked as outliers.
     *
     * @param cur the column index on which to perform Winsorization
     */
    public void iqr(int cur) {
        double lo = percentile(cur, perc);
        double hi = percentile(cur, 100.0 - perc);

        // Mark values below lower cap
        for (int i = 0; i < super.row; i++) {
            if (val[cur].get(i) >= lo) {
                break;
            }
            super.outs.add(i);
        }

        // Mark values above upper cap
        for (int i = super.row - 1; i >= 0; i--) {
            if (val[cur].get(i) <= hi) {
                break;
            }
            super.outs.add(i);
        }
    }

}
