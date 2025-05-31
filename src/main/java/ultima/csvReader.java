package ultima;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A simple CSV reader that extracts column titles and numerical data into lists.
 * It assumes the CSV uses ", " as the delimiter and that non-numeric entries can be skipped.
 *
 * @author Andi Guo
 */
public class csvReader {

    /**
     * The File object representing the CSV file.
     */
    public File f;

    /**
     * A list of column titles extracted from the first line of the CSV.
     */
    public ArrayList<String> titles = new ArrayList<>();

    /**
     * Constructs a csvReader for the given file path.
     *
     * @param filePath the path to the CSV file to read
     */
    public csvReader(String filePath) {
        this.f = new File(filePath);
    }

    /**
     * Returns the number of columns (based on titles) read from the CSV.
     *
     * @return the count of column titles
     */
    public int getLen() {
        return titles.size();
    }

    /**
     * Prints diagnostic information about the working directory and file existence.
     * Useful for debugging file path issues.
     */
    public void testf() {
        System.out.println("Working dir: " + System.getProperty("user.dir"));
        System.out.println("CSV file path: " + f.getAbsolutePath());
        System.out.println("Exists? " + f.exists() + "  IsFile? " + f.isFile());
    }

    /**
     * Reads the first line of the CSV file to extract column headers.
     * It splits the line on ", " and strips any surrounding quotes.
     * If the file is not found, this method returns without modifying titles.
     */
    public void getCols() {
        try (Scanner scan = new Scanner(this.f)) {
            String regex = ", ";
            String columns = scan.nextLine();
            String[] split_col = columns.split(regex);
            for (String s : split_col) {
                titles.add(s.replace("\"", ""));
            }
        } catch (FileNotFoundException e) {
            // File not found, leave titles empty
        }
    }

    /**
     * Reads the CSV file line by line (skipping the header) and parses numeric values.
     * Each column's values are added to the corresponding list in ret.
     * Empty fields are skipped. Non-numeric entries cause NumberFormatException.
     *
     * @param ret an array of ArrayList<Double> where each list corresponds to one column
     */
    public void fileText(ArrayList<Double>[] ret) {
        try (Scanner scan = new Scanner(f)) {
            String regex = ", ";
            scan.nextLine();  // skip header row
            while (scan.hasNextLine()) {
                String cur = scan.nextLine();
                String[] row = cur.split(regex);
                int ind = 0;
                for (String s : row) {
                    if (s.isEmpty()) {
                        continue;
                    }
                    double val = Double.parseDouble(s.replace("\"", ""));
                    ret[ind++].add(val);
                }
            }
        } catch (FileNotFoundException e) {
            // File not found, do nothing
        }
    }
    
}
