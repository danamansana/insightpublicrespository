package com.company;

/**
 * Created by danielappel on 4/3/17.
 */
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Hour extends data {
    Calendar time;
    int pings;
    public Hour(parser p){
        super(p.time, p.time, 1);
        time = cal(p.time);
        pings = 1;

    }

    public void updatepings(){
        pings  =+1;
        usage =+1;
    }


    public Calendar increment(int field, int amount){
        Calendar inc = (Calendar)time.clone();
        inc.add(field, amount);
        return inc;
    }

    public Calendar cal(String s){
        Calendar c = Calendar.getInstance();
        Pattern p = Pattern.compile("(.*?)/(.*?)/(.*?):(.*?):(.*?):(.*?)\\s(.*?)?");
        Matcher m = p.matcher(s);
        m.find();
        int day = Integer.parseInt(m.group(1));
        int month = monthnumber(m.group(2));
        int year = Integer.parseInt(m.group(3));
        int hour = Integer.parseInt(m.group(4));
        int min =  Integer.parseInt(m.group(5));
        int sec = Integer.parseInt(m.group(6));
        c.set(year,month,day,hour,min,sec);
        return c;
    }

    public Integer monthnumber(String m){
        if(m.substring(0,2).toLowerCase().equals("ja")){return 1;}
        if(m.substring(0,1).toLowerCase().equals("f")){return 2;}
        if(m.substring(0,3).toLowerCase().equals("mar")){return 3;}
        if(m.substring(0,2).toLowerCase().equals("ap")){return 4;}
        if(m.substring(0,3).toLowerCase().equals("may")){return 5;}
        if(m.substring(0,3).toLowerCase().equals("jun")){return 6;}
        if(m.substring(0,3).toLowerCase().equals("jul")){return 7;}
        if(m.substring(0,2).toLowerCase().equals("au")){return 8;}
        if(m.substring(0,1).toLowerCase().equals("s")){return 9;}
        if(m.substring(0,1).toLowerCase().equals("o")){return 10;}
        if(m.substring(0,1).toLowerCase().equals("n")){return 11;}
        return 12;
    }

}
