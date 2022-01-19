package com.jm.online_store.controller.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

public class ExampleWild {
    public static void main(String[] args) {
        /*LinkedList list =
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        Iterator<Integer> iterator = list.iterator();
        iterator.next();
        iterator.remove(1);
        cityIterator.next();
        // генерирует ConcurrentModificationException
        cityIterator.remove(1);*/
        //ok









        List<Number> nums = new ArrayList<>();
        nums.add(1);
        nums.add(2);
        nums.add(3);
        List<? super Integer> ints = nums;
        ints.forEach(System.out::println);
        ints.get(2); //3


//        List<Number> nums = new ArrayList<>();
//        nums.add(1);
//        nums.add(2);
//        nums.add(3);
//        List<? super Integer> ints = nums;
//        System.out.println(ints.get(2));


//        List<Integer> ints = new ArrayList<>();
//        ints.add(1);
//        ints.add(2);
//        ints.add(3);
//        List<? extends Number> nums = ints;
//        nums.add(null);
//        nums.forEach(System.out::println);



//        Integer integer = 23;
//        Number number = integer;
//
//        Number number = 23;
//        Integer integer = number;


//        List<Integer> list = new ArrayList<>();
//        list.add(1);
//        list.add(2);
//        list.add(3);
//        List<Number> listNumber = list;
    }
}
