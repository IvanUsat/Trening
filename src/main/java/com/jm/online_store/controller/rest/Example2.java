package com.jm.online_store.controller.rest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

public class Example2 {
    public static void main(String[] args) throws MyException {
        ArrayList<BigDecimal> number = new ArrayList<>();
        number.add(new BigDecimal(1.5));
        number.add(new BigDecimal(2.0));
        number.add(new BigDecimal(1.1));
        number.add(new BigDecimal(3.1));
        number.add(new BigDecimal(6.0));
        number.add(new BigDecimal(1.0));
        filter(number);
    }

    public static void filter(Collection<BigDecimal> numbers) throws MyException {
        try{
            BigDecimal min = numbers.iterator().next();
            BigDecimal max = numbers.iterator().next();
            for (BigDecimal number: numbers){
                if (max.compareTo(number)<=0){
                    max = number;
                }
                if(min.compareTo(number) >=0){
                    min = number;
                }
            }
            BigDecimal tempDelete = max.divide(min);

            Collection<BigDecimal> toDeleteNumber = null;

            for(BigDecimal number: numbers){
                /*if(number.compareTo(tempDelete) >= 0){
                    newNumber.add(number);
                }*/
                if(number.compareTo(tempDelete) < 0){
                    toDeleteNumber.add(number);
                }
            }

        }catch (Exception e){
            throw new MyException(e);
        }
    }

    private static class MyException extends Exception {
        public MyException(Exception message) {
            super(message);
        }
    }
}
