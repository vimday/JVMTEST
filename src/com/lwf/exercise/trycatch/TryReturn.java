package com.lwf.exercise.trycatch;

public class TryReturn {
    public static void main(String[] args) {
        System.out.println(test());
    }

    private static boolean test() {
        try {
            return true;
        }finally {
            return false;
        }
    }
}
