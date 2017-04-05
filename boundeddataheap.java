package com.company;

/**
 * Created by danielappel on 4/3/17.
 */
public class boundeddataheap extends dataheap {
    int size;
    int max;
    public boundeddataheap(int n){
        super();
        max = n;
    }
    public void boundedinsert(data d){
        insert(d);
        size = size+1;
        if(size>max){
            removelast();
            size= size-1;
        }
    }

    public void removelast(){
        heap.remove(subtractone(nextleaf));
        nextleaf = subtractone(nextleaf);
    }
}
