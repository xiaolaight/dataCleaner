package ultima;

import java.util.ArrayList;

/**
 * A utility class for removing outliers by sorting column data and
 * providing indexes of outliers. Uses merge sort to prepare the data.
 *
 * @author Andi Guo
 */
public class remOut {

    /**
     * The 2D array of column value lists to process.
     * val[i] holds the data for column i.
     */
    public ArrayList<Double> val[];

    /**
     * Number of columns in the dataset.
     */
    public int col = 0;

    /**
     * Number of rows (data points) per column.
     */
    public int row = 0;

    /**
     * A list of row indices identified as outliers.
     */
    public ArrayList<Integer> outs = new ArrayList<>();

    /**
     * Constructs the outlier remover with the given dimensions and input data.
     * Performs a shallow copy of the input array.
     *
     * @param c   the number of columns
     * @param r   the number of rows per column
     * @param inp the input data as an array of lists
     */
    @SuppressWarnings("unchecked")
    public remOut(int c, int r, ArrayList<Double>[] inp) {
        this.col = c;
        this.row = r;
        this.val = new ArrayList[col];
        System.arraycopy(inp, 0, val, 0, col);
    }

    /**
     * Recursively performs merge sort on a segment of a specific column's data.
     *
     * @param l the starting index (inclusive) of the segment
     * @param r the ending index (exclusive) of the segment
     * @param v the column index to sort
     * @return a new sorted list of values from index l to r-1
     */
    public ArrayList<Double> mergeSort(int l, int r, int v) {
        ArrayList<Double> ret = new ArrayList<>();
        if (l == r - 1) {
            ret.add(val[v].get(l));
            return ret;
        }
        int mid = (l + r) / 2;
        ArrayList<Double> lef = mergeSort(l, mid, v);
        ArrayList<Double> rig = mergeSort(mid, r, v);
        int ptl = 0, ptr = 0;
        while (ptl < lef.size() && ptr < rig.size()) {
            if (lef.get(ptl) < rig.get(ptr)) {
                ret.add(lef.get(ptl++));
            } else {
                ret.add(rig.get(ptr++));
            }
        }
        while (ptl < lef.size()) {
            ret.add(lef.get(ptl++));
        }
        while (ptr < rig.size()) {
            ret.add(rig.get(ptr++));
        }
        return ret;
    }

    /**
     * Initializes sorting for all columns by applying mergeSort
     * across their full range.
     */
    public void init() {
        for (int i = 0; i < col; i++) {
            val[i] = mergeSort(0, row, i);
        }
    }

}
