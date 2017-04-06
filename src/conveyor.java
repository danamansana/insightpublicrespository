package com.company;
import java.util.ArrayList;
/**
 * Created by danielappel on 4/4/17.
 */

import java.util.Calendar;
public class conveyor {
    ArrayList<Hour> conv;
    int currentposition;
    int currentnumber;
    Integer size;
    basicbin tops;
    public conveyor(){
        conv = new ArrayList<>();
        currentposition= 0;
        currentnumber = 0;
        size = 0;
        tops = new basicbin();
    }
    public conveyor(basicbin b){
        conv = new ArrayList<>();
        currentposition= 0;
        currentnumber= 0;
        size =0;
        tops= b;
    }
    public Boolean during(){
        if(currentposition>conv.size()-1||conv.isEmpty()){return false;}
        Hour start = conv.get(currentposition);
        Hour finish = conv.get(conv.size()-1);
        Calendar helper = start.increment(Calendar.HOUR, 1);
        return helper.before(finish.time);
    }

    public void add(Hour h){
        if(!conv.isEmpty() && conv.get(conv.size()-1).name.equals(h.name)){
            currentnumber= +1;
            conv.get(conv.size()-1).updatepings();
        }
        else{
        currentnumber = currentnumber+1;
        h.usage = currentnumber;
        conv.add(h);
        size = size+1;
        adjust();
        }
    }

    public void incpos(){
        currentposition = currentposition+1;
    }

    public void adjust(){
        while(during()){
            conv.get(conv.size()-1).usage = conv.get(conv.size()-1).usage-conv.get(currentposition).pings;
            currentnumber = currentnumber- conv.get(currentposition).pings;
            tops.ins(currentposition, conv);
            incpos();
        }
        if(currentposition>1000){
            ArrayList<Hour> cleanconv = new ArrayList<>();
            int  i = currentposition;
            while(i<conv.size()){
                cleanconv.add(conv.get(i));
                i = i+1;
            }
            conv = cleanconv;
            currentposition = 0;
        }

    }

    public void clearconv(){
        while (currentposition < conv.size()){
            tops.ins(currentposition,conv);
            incpos();
        }
    }



}
