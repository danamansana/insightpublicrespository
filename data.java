package com.company;

/**
 * Created by danielappel on 3/31/17.
 */
public class data {
    public String name;
    public String address;
    public int usage;
    public data(String n, String a, int u){
        name= n;
        address= a;
        usage= u;
    }
    public void updateaddress(String a){
        address = a;
    }
    public void updateusage(){
        usage=usage+1;
    }
    public void state(){
        //System.out.print(name+",");
        System.out.print(address+",");
        System.out.println(usage);
    }
    public String publish(){
        String  u = Integer.toString(usage);
        return name+","+u;
    }
}
