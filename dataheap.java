package com.company;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by danielappel on 3/31/17.
 */
// This class implements a binary heap of data with the data having greatest usage at the top. Nodes of the heap are
    //indexed by binary strings beginning with 1, in such a way that appending a 0 always moves down and to the left and appending a 1 always
    // moves down and to the right. The root has address 1.
    // dataheap is written to allow data to be updated while in the heap, unlike java's built in priorityqueue
public class dataheap {
    public int height; // This is the height of the tree
    public String nextleaf; //This is the next address into which data will be added
    public HashMap<String, data> heap; //Stores the values
    public HashMap<String, data> names; // stores names of things that have already occurred
    public dataheap(){
        height = 1;
        nextleaf = "1";
        heap= new HashMap();
        names = new HashMap();
    }

    public dataheap top(int k ){
        dataheap value = new dataheap();
        int  i = 0;
        while(!nextleaf.equals("1") && i<k){
            data d = top();
            value.insert(d);
            i = i+1;
        }
        return value;
    }

    public void insert(data d){
        d.updateaddress(nextleaf);
        heap.put(nextleaf,d);
        sortposition(nextleaf);
        nextleaf = addone(nextleaf);
        /*if (nextleaf.equals(repeat("1", height))){
            nextleaf = "1"+ repeat("0", height);
            height= height+1;
        }
        else{
            nextleaf = addone(nextleaf);
        }*/

    }

    //top returns the top of the heap, assuming the heap isn't empty.

    public data top(){
        data value = heap.get("1");
        //System.out.println(value.usage+"is the first value");
        heap.remove("1");
        String leaf = subtractone(nextleaf);
        //System.out.println(leaf+"will go to the top");
        if (heap.containsKey(leaf)){
        data newtop = heap.get(leaf);
        heap.remove(leaf);
        newtop.updateaddress("1");
        heap.put("1",newtop);
        sortposition("1");}
        nextleaf = leaf;
        //System.out.println("now the state is");
        //state();
        return value;
    }

    public void updateposition(String position){
        heap.get(position).updateusage();
        sortposition(position);
    }

    public void sortposition(String position){
        String p = position;
        while(isless(moveindirection(0,p), p)){
            switcher(moveindirection(0,p),p);
            p = moveindirection(0,p);
            //state();
        }
        while(isless(p,moveindirection(1,p))||isless(p,moveindirection(2,p))){
            if (isless(moveindirection(1,p),moveindirection(2,p))){
                switcher(p, moveindirection(2,p));
                p = moveindirection(2,p);
                //state();
            }
            else{
                switcher(p, moveindirection(1,p));
                p = moveindirection(1,p);
                //state();
            }
        }
    }

    // isless returns true exactly when place1 and place2 are both occupied and place 2 has greater usage

    public boolean isless(String place1, String place2){
        if(!heap.containsKey(place2)||!heap.containsKey(place1)){
            return false;}
        else{
            return heap.get(place1).usage < heap.get(place2).usage;
        }
    }

    public void switcher(String place1, String place2){
        data d1 = heap.get(place1);
        data d2 = heap.get(place2);
        heap.remove(place1);
        heap.remove(place2);
        heap.put(place1, d2);
        heap.put(place2,d1);
        heap.get(place1).updateaddress(place1);
        heap.get(place2).updateaddress(place2);
    }

    //given 0,1, or 2 moveindirection finds the position above, down to the left, or down to the right of current position

    public String moveindirection(int direction, String currentposition){
        if (direction == 0 && currentposition.equals("1")){ return "0";}
        if (direction == 1){return currentposition + "0";}
        if (direction == 2){return currentposition + "1";}
        else {return currentposition.substring(0, currentposition.length()-1);}
    }

    public String repeat(String s, int n){
        if (n==1){return s;}
        else{ return repeat(s, n-1);}
    }

    // addone finds the next address after s, assuming it is on the same level of the tree. We can therefore assume s contains at least
    // a 1 and a 0 and has length 2 or greater

    public String addone(String s){
        int n = s.length()-1;
        //System.out.println(s);
        String t= s.substring(0,n);
        //System.out.println(t + "is the prefix of "+s);
        String u = s.substring(n,n+1);
        //System.out.println(u + "is the last of " + s);
        if(u.equals("0")){
                return t+"1";
            }
            else if (s.equals("1")){
                return "10";}
            else{
                return addone(t)+"0";
            }
    }

    // subtractone finds the address before s

    public String subtractone(String s){
        int n = s.length()-1;
        String t = s.substring(0,n);
        String u = s.substring(n,n+1);
        if(u.equals("1")){
            return t+"0";
        }
        else{
            String v = subtractone(t)+"1";
            if(v.substring(0,1).equals("1")){return v;}
            else{
                return v.substring(1,v.length());}
        }
    }

    public void state(){
        //System.out.println(height);
        System.out.println(nextleaf);
        for(data value: heap.values()){value.state();}
    }

    public static void main(String[] args){


        /**dataheap D  = new dataheap();
        String s = "1";
        int i = 1;
        Random ran = new Random();*/
        /**while (i != 0){
            System.out.println(s);
            s = D.subtractone(s);
            i = i-1;
        }*/

        /**while (i<10){
            int n = ran.nextInt(10);
            data d = new data(s,s,n);
            d.state();
            //System.out.println(n);
            D.insert(d);
            s= D.addone(s);
            i = i+1;
        }
        D.state();
        i = 0;
        while(i<5){
            D.updateposition("100");
            System.out.println(i);
            D.state();
            i = i+1;
        }*/


        /**while (i<10){
            //System.out.println(i);
            //System.out.println(s);
            //s = D.addone(s);
            data d = new data(s,s,i);
            //d.state();
            //s = D.addone(s);
            D.insert(d);
            //D.state();
            s= D.addone(s);
            i = i+1;
        }
        D.state();*/

        /**while(!D.nextleaf.equals("1")){
            D.top();
        }*/
        //D.sortposition("100");


        /*while (i<20){
            System.out.println(s);
            s = D.addone(s);
            i = i+1;
        }*/
        //D.state();
        //int n = 10;
        //int i = 0;
        //String s = "1";
        //System.out.println(s.substring(0,1));
        //System.out.print(D.addone(s));
        /*while(i<n){
            data d = new data(Integer.toString(i), Integer.toString(i), i);
            d.state();
            D.insert(d);
            D.state();
            i=i+1;
        }*/
        //data first = new data("first", "first", 5);
        //first.state();
        //D.insert(first);
        //D.state();
    }
}
