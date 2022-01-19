package com.jm.online_store.controller.rest;

public class ExampleRunable2 implements Runnable {
    @Override
    public void run() {
        for(int i =0; i<5; i++){
            System.out.println("Runable2 " + i);
        }
    }
}
