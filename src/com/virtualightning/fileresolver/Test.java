package com.virtualightning.fileresolver;

import java.util.HashMap;

public class Test {
    public static void main(String[] args) {
        String a = "123";
        String b = new String("123");
        HashMap<String,Integer> map = new HashMap<>();
        System.out.println(a == b);
        System.out.println(a.hashCode());
        System.out.println(b.hashCode());
        System.out.println(a.equals(b));
        map.put(a,1);
        map.put(b,2);
        System.out.println(map);
    }
}
