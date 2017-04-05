package com.company;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by danielappel on 4/4/17.
 */
public class hourheap {
    public hourheap(){
        nextleaf = "1";
        heap= new HashMap();
    }

    public int height; // This is the height of the tree
    public String nextleaf; //This is the next address into which data will be added
    public HashMap<String, Hour> heap; //Stores the values

    public void insert(Hour h){
        h.updateaddress(nextleaf);
        heap.put(nextleaf,h);
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

    public Hour top(){
        Hour value = heap.get("1");
        //System.out.println(value.usage+"is the first value");
        heap.remove("1");
        String leaf = subtractone(nextleaf);
        //System.out.println(leaf+"will go to the top");
        if (heap.containsKey(leaf)){
            Hour newtop = heap.get(leaf);
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
        //sortposition(position);
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
            return heap.get(place1).time.before(heap.get(place2).time);
        }
    }

    public void switcher(String place1, String place2){
        Hour h1 = heap.get(place1);
        Hour h2 = heap.get(place2);
        heap.remove(place1);
        heap.remove(place2);
        heap.put(place1, h2);
        heap.put(place2,h1);
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

    public void inserthour(Hour h){
        inserthourhelper(h, "1");
        if(heap.isEmpty()){
            insert(h);
        }
        else if(!heap.get("1").name.equals(h.name)){
            insert(h);
        }
    }

    public void inserthourhelper(Hour h, String head){
        if(heap.containsKey(head)){
            Hour tocompare = heap.get(head);
            Calendar tocompareend = tocompare.increment(Calendar.HOUR, 1);
            if(tocompareend.after(h.time)){
                updateposition(head);
                inserthourhelper(h, head+"0");
                inserthourhelper(h, head+"1");
            }
        }
        }

    public dataheap sortbyusage(){
        dataheap value  = new dataheap();
        while(!nextleaf.equals("1")) {
            value.insert(top());}
            return value;
    }


    }


