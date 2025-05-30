package ultima;

import java.util.ArrayList;

public class remOut {

    public ArrayList<Double> val[];
    public int col = 0;
    public int row = 0;
    public  ArrayList<Integer> outs = new ArrayList<>();

    public remOut(int c, int r, ArrayList<Double>[] inp){

        col = c;
        row = r;
        val = new ArrayList[col];
        System.arraycopy(inp, 0, val, 0, col);

    }

    public ArrayList<Double> mergeSort(int l, int r, int v){

        ArrayList<Double> ret = new ArrayList<>();
        if (l == r-1){
            ret.add(val[v].get(l));
            return ret;
        }
        int mid = (l+r)/2;
        ArrayList<Double> lef = mergeSort(l, mid, v);
        ArrayList<Double> rig = mergeSort(mid, r, v);
        int ptl = 0, ptr = 0;
        while (ptl < (mid-l) && ptr < (r - mid)){
            if (lef.get(ptl) < rig.get(ptr)){
                ret.add(lef.get(ptl++));
            }
            else{
                ret.add(rig.get(ptr++));
            }
        }
        while (ptl < (mid-l)){
            ret.add(lef.get(ptl++));
        }
        while(ptr < (r-mid)){
            ret.add(rig.get(ptr++));
        }
        return ret;

    }

    public void init(){

        for (int i=0; i<col; i++){
            val[i] = mergeSort(0, row, i);
        }

    }

    public ArrayList<Integer> returnOuts(){

        return outs;

    }

}
