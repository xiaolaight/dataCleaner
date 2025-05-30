package ultima;

import java.util.ArrayList;

public class winsorMethod extends remOut{

    public int perc;

    public winsorMethod(int c, int r, ArrayList<Double>[] arr, int p){

        super(c, r, arr);
        perc = p;

    }

    public Double percentile(int cur, double per){

        int lo = 0, hi = super.row-1, ans = 0;
        while (lo <= hi){
            int mid = (lo + hi)/2;
            if (((double) mid / super.row) <= per/100.0){
                ans = mid;
                lo = mid+1;
            }
            else{
                hi = mid-1;
            }
        }
        return val[cur].get(ans);

    }

    public void iqr(int cur){

        double lo = percentile(cur, perc);
        double hi = percentile(cur, 100.0-perc);
        for (int i=0; i<super.row; i++){
            if (val[cur].get(i) >= lo){
                break;
            }
            super.outs.add(i);
        }
        for (int i=super.row-1; i>=0; i--){
            if (val[cur].get(i) <= hi){
                break;
            }
            super.outs.add(i);
        }

    }



}