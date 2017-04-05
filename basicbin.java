package com.company;

/**
 * Created by danielappel on 4/4/17.
 */
import java.util.ArrayList;
import java.util.Calendar;
public class basicbin {
    Hour[] bin;
    basicbin(){
        bin = new Hour[10];
        int n = 0;
    }

    public dataheap top(int k){
        int  i =0;
        dataheap value = new dataheap();
        while (i<k && bin[i] != null){
            value.insert(bin[i]);
            i = i+1;
        }
        return value;
    }

    public void ins(int i, ArrayList<Hour> list){
        boolean stillactive = true;
        int count = count(i, list);
        int index = 0;
        while( stillactive && index<bin.length ){
            if(bin[index] == null){
                bin[index] = list.get(i);
                stillactive = false;
                index = index+1;
            }
            else if((count> (bin[index]).usage)) {
                bin[index] = list.get(i);
            stillactive =false;}
            index= index+1;
        }
        }
    public int count(int i, ArrayList<Hour> list){
        Hour h = list.get(i);
        Calendar c = h.increment(Calendar.HOUR, 1);
        int index = i;
        while(index<list.size() && list.get(index).time.before(c)){
            h.usage = h.usage+1;
            index = index+1;
        }
        return h.usage;
    }
}
