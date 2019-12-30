package com.lwf.jvm.agent;


import java.lang.instrument.Instrumentation;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class SizeOfAgent {
    private static Instrumentation inst;

    /** initializes agent */
    public static void premain(String agentArgs, Instrumentation instP)
    {
        inst = instP;
    }

    /**
     * Returns object size without member sub-objects.
     * @param o object to get size of
     * @return object size
     */
    public static long sizeOf(Object o)
    {
        if(inst == null)
        {
            throw new IllegalStateException("Can not access instrumentation environment.\n" +
                    "Please check if jar file containing SizeOfAgent class is \n" +
                    "specified in the java's \"-javaagent\" command line argument.");
        }
        return inst.getObjectSize(o);
    }

    /**
     * Calculates full size of object iterating over
     * its hierarchy graph.
     * @param obj object to calculate size of
     * @return object size
     */
    public static long fullSizeOf(Object obj)
    {
        Map<Object, Object> visited = new IdentityHashMap<Object, Object>();
        Stack<Object> stack = new Stack<Object>();

        long result = internalSizeOf(obj, stack, visited);
        while (!stack.isEmpty())
        {
            result += internalSizeOf(stack.pop(), stack, visited);
        }
        visited.clear();
        return result;
    }

    private static boolean skipObject(Object obj, Map<Object, Object> visited)
    {
        if (obj instanceof String) {//这个if是bug，应当去掉--teasp
            // skip interned string
            if (obj == ((String) obj).intern()) {
                return true;
            }
        }
        return (obj == null) || visited.containsKey(obj);
    }

    @SuppressWarnings("rawtypes")
    private static long internalSizeOf(Object obj, Stack<Object> stack, Map<Object, Object> visited)
    {
        if (skipObject(obj, visited))
        {
            return 0;
        }
        visited.put(obj, null);

        long result = 0;
        // get size of object + primitive variables + member pointers
        result += SizeOfAgent.sizeOf(obj);

        // process all array elements
        Class clazz = obj.getClass();
        if (clazz.isArray())
        {
            if(clazz.getName().length() != 2)
            {// skip primitive type array
                int length =  Array.getLength(obj);
                for (int i = 0; i < length; i++)
                {
                    stack.add(Array.get(obj, i));
                }
            }
            return result;
        }

        // process all fields of the object
        while (clazz != null)
        {
            Field[] fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; i++)
            {
                if (!Modifier.isStatic(fields[i].getModifiers()))
                {
                    if (fields[i].getType().isPrimitive())
                    {
                        continue; // skip primitive fields
                    }
                    else
                    {
                        fields[i].setAccessible(true);
                        try
                        {
                            // objects to be estimated are put to stack
                            Object objectToAdd = fields[i].get(obj);
                            if (objectToAdd != null)
                            {
                                stack.add(objectToAdd);
                            }
                        }
                        catch (IllegalAccessException ex)
                        {
                            assert false;
                        }
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
        return result;
    }

    static Obj4SizeofTest obj = new Obj4SizeofTest();
    public static void main(String[] args) throws Exception
    {
        System.out.println("fullSizeOf : " + fullSizeOf(new Obj4SizeofTest()));
        System.out.println("sizeOf : " + sizeOf(new Obj4SizeofTest()));
        System.out.println("sizeOf Integer: " + sizeOf(new Integer(0)));
        System.out.println("sizeOf Object: " + sizeOf(new Object()));

        System.out.println("fullSizeOf Son: " + fullSizeOf(new Son()));

        System.out.println("fullSizeOf WierdStringSon: " + fullSizeOf(new WierdStringSon()));
        System.out.println("fullSizeOf WierdString: " + fullSizeOf(new WierdString()));

        //System.out.println("HotspotSizeof Obj4SizeofTest : " + HotspotSizeof.sizeof(new Obj4SizeofTest()));
        //System.out.println("determinObjSize Obj4SizeofTest : " + HotspotSizeof.determinObjSize(Obj4SizeofTest.class));
    }

}
