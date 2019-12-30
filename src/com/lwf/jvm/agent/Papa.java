package com.lwf.jvm.agent;

public class Papa
{
    int aint = 4;
    public static int bint;
    //    private String str = "123";
//    private String str = new String("123");
    String str = new String(new byte[]{49,50,51});
    //    private String str = new String(new char[]{49,50,51});
    int[] ints = {};
    //    private int[][] intss = {{}};
    int[][] intss = {{1},{1,2}};

    protected float getNum()
    {
        return 4.0f;
    }
}
