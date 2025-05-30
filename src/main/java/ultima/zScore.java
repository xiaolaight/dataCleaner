package ultima;

import java.util.ArrayList;

public class zScore extends remOut{

    ArrayList<Double> sumVals = new ArrayList<>();
    
    public zScore(int c, int r, ArrayList<Double>[] inp){
        super(c, r, inp);
    }

    @Override
    public void init(){
        super.init();
        for (int i=0; i<super.col; i++){
            double csm = 0;
            for (double  j : super.val[i]){
                csm += j;
            }
            sumVals.add(csm);
        }
    }

    public Double mean(int cur){
        return (double) sumVals.get(cur) / super.row;
    }

    public Double std(int cur){
        double cmean = mean(cur);
        double ret = 0;
        for (int i=0; i<super.row; i++){
            ret += (super.val[cur].get(i) - cmean)*(super.val[cur].get(i) - cmean);
        }
        ret /= (double) super.row;
        return Math.sqrt(ret);
    }

    public void zMethod(int cur){
        double cmean = mean(cur);
        double cstd = std(cur);
        for (int i=0; i<super.row; i++){
            double Z = (super.val[cur].get(i) - cmean) / cstd;
            if (Math.abs(Z) >= 3){
                super.outs.add(i);
            }
        }
    }

}
