package com.company;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by danielappel on 4/2/17.
 */
public class parser {
    public String name;
    public String time;
    public String command;
    public String response;
    public String originalline;
    public boolean matched;
    public parser(String input){
        Pattern p = Pattern.compile("(\\S*)\\s.*\\s\\[(.*)]\\s+\"(.*)\"\\s+(401)?.*");
        Matcher m = p.matcher(input);
        m.find();
        name = m.group(1);
        time = m.group(2);
        command  = m.group(3);
        response = m.group(4);
        if(response == null){response = "noproblem";}
        originalline = input;
        matched = m.matches();
        Pattern c = Pattern.compile("(.*?/)?(.*)");
        Matcher com = c.matcher(command);
        com.find();
        command = "/"+com.group(2);
    }
    public void state(){
        System.out.println(matched);
        System.out.println(name);
        System.out.println(time);
        System.out.println(command);
        System.out.println(response);

    }

    public data timedatum(){
        data d  = new data(time, time, 1);
        return d;
    }

    public data commanddatum(){
        data d  = new data(command, command, 1);
        return d;
    }

    public data userdatum(){
        data d = new data(name, name, 1);
        return d;
    }



    public static void main(String[] args){
        parser testparser = new parser("name - - [time] \"GET /command\"  blahblahblah");
        testparser.state();


    }
}
