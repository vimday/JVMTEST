package com.lwf.exercise.jvm;

import com.lwf.jvm.agent.SizeOfAgent;

public class TestSizeOfAnObject {
    public static void main(String[] args) {
        System.out.println(SizeOfAgent.sizeOf(new Object()));
        System.out.println(SizeOfAgent.sizeOf(new int[]{}));
        System.out.println(SizeOfAgent.sizeOf(new P()));
    }
    //-XX:+UseCompressedClassPointers -XX:+UseCompressedOops
    //Oops:ordinary object pointers
//

    private static class P{
                        //8 _markword
                        //4 _class pointer
        int id;//4
        String name;//4
        int age;//4
        byte b1;//1
        byte b2;//1

        Object o;//4
        byte b3;//1


    }

}
