package com.jan.learning.web.rest.util;

public class RandomUtil {

    public static void main(String[] args) {}

    //includes both min and max
    public static int getRandom(int min, int max) {
        int r = (int) (Math.random() * ((max - min) + 1)) + min;
        return r;
    }
}
