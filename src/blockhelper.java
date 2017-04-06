package com.company;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by danielappel on 4/3/17.
 */
public class blockhelper extends data {
    public Calendar countfrom;
    public Calendar blockedtill;
    public Integer pings;
    public String hourname;
    public String hourbeginname;
    public blockhelper(parser p){
        super(p.originalline, p.name, 1);
        countfrom = cal(p.time);
        blockedtill = Calendar.getInstance();
        blockedtill.set(1000,1,1, 1,1,1);
        pings = 1;
        hourname = p.time;
        hourbeginname = p.time;

    }

    public void rename(){
        name = hourbeginname;
    }

    public void addping(){
        pings = pings+1;
    }

    public Calendar increment(Calendar c, int field, int amount){
        Calendar inc = Calendar.getInstance();
        inc.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
                c.get(Calendar.HOUR), c.get(Calendar.MINUTE), c.get(Calendar.SECOND));
        inc.add(field, amount);
        return inc;

    }

   /** public String caltostring(Calendar c){
        Integer y = c.get(Calendar.YEAR);
        String year = Integer.toString(y);
        Integer m = c.get(Calendar.MONTH);
        String month = Integer.toString(m);
        Integer d = c.get(Calendar.DATE);
        String day = Integer.toString(d);
        Integer h = c.get(Calendar.HOUR);
        String hour = Integer.toString(h);
        m = c.get(Calendar.MINUTE);
        String minute = Integer.toString(m);
        Integer s = c.get(Calendar.SECOND);
        String second = Integer.toString(s);
        return day+"/"+month+"/"+year+":"+hour+":"+minute+":"+second;
    }*/

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

    public static void main(String[] args){
        parser p = new parser("gboreham.interlog.com - - [01/Jul/1995:00:59:36 -0400] \"GET /images/NASA-logosmall.gif HTTP/1.0\" 200 786\n");
        blockhelper b = new blockhelper(p);
        Calendar c = b.cal("01/Jul/1995:17:09:38 -0400");
        Calendar d  = b.cal("01/Jul/1995:17:09:39 -0400");
        System.out.println(c.get(Calendar.HOUR));
        c = b.increment(c, Calendar.HOUR, 1);
        System.out.print(c.get(Calendar.HOUR));
        System.out.print(d.before(c));

    }
}
