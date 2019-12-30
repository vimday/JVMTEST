package com.lwf.exercise.trycatch;


public class TryReturn2 {
    public static void main(String[] args) {
        System.out.println(test());
    }

    private static int test() {
        int a=0;
        try {
            a=5;
            return a;
        }finally {
            a=10;
        }
    }
}
