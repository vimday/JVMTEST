package com.lwf.jvm.agent;

import java.lang.reflect.Type;

public class Obj4SizeofTest extends Papa implements Type
{
    int aint = 3;
    public int bint = 4;
    boolean b1 = true;
    boolean b2 = true;
    boolean b3 = true;
    boolean b4 = true;
    boolean b5 = true;
    boolean b6 = true;
    boolean b7 = true;
    boolean b8 = true;
    String str1;
    Object obj = new Papa();
    public static final byte[] bytes = {97};
    String str2 = new String(bytes);
    Integer i = 1;
    int[] is = {1,2,3};
    Object[][] objs = {{new Object(),new Object()},{new Object(),new Object()}};

    private static class Inner
    {}

    private class Inner1
    {}
}

