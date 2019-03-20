package com.zjzf.shoescircle.lib.utils;

import java.util.Random;

/**
 * Created by 陈志远 on 2017/5/5.
 */

public class RandomUtil {

    private static final Random random = new Random();

    public static int random(int min, int max) {
        random.setSeed(System.nanoTime());
        return random.nextInt(max - min + 1) + min;
    }
}
