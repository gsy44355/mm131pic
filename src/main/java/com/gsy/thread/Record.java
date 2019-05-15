package com.gsy.thread;

import java.util.LinkedList;
import java.util.Map;

/**
 * Created By Gsy on 2019/5/10
 */
public class Record {
    LinkedList<Map> linkedList = new LinkedList<>();

    public LinkedList<Map> getLinkedList() {
        return linkedList;
    }

    public void setLinkedList(LinkedList<Map> linkedList) {
        this.linkedList = linkedList;
    }

    public synchronized Map getUrl(){
        if(!linkedList.isEmpty()){
            Map re =  linkedList.getFirst();
            linkedList.remove(re);
            return re;
        }else {
            System.out.println("获取不到");
        }
        System.out.println("获取不到网页了");
        return null;
    }

}
