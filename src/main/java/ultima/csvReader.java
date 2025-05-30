package ultima;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

// to add: javadoc comments

public class csvReader{

    public File f;
    public ArrayList<String> titles = new ArrayList<>();

    public csvReader(String filePath){
        f = new File(filePath);
    }

    public int getLen(){
        return titles.size();
    }

    public void testf(){

        System.out.println("Working dir: " + System.getProperty("user.dir"));
        System.out.println("CSV file path: " + f.getAbsolutePath());
        System.out.println("Exists? " + f.exists() + "  IsFile? " + f.isFile());

    }

    public void getCols(){

        try (Scanner scan = new Scanner(this.f)){
            String regex = ", ";
            String columns = scan.nextLine();
            String[] split_col = columns.split(regex);

            for (String s : split_col){
                titles.add(s.replace("\"", ""));
            }
        }
        catch (FileNotFoundException e){
            return;
        }

    }

    public void fileText(ArrayList<Double>[] ret){

        try (Scanner scan = new Scanner(f)){
            String regex = ", ";
            scan.nextLine();
            while (scan.hasNextLine()){
                String cur = scan.nextLine();
                String[] row = cur.split(regex);
                int ind = 0;
                for (String s : row){
                    if (s.length() == 0){
                        continue;
                    }
                    double val = Double.parseDouble(s.replace("\"", ""));
                    ret[ind++].add(val);
                }
            }

        }
        catch (FileNotFoundException e){
            return;
        }

    }

}