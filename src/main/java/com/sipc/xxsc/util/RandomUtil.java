package com.sipc.xxsc.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomUtil {
    public static int getRandomInt(int min, int max){
        if (min == max)
            return min;
        if (min > max){
            int tmp = min;
            min = max;
            max = tmp;
        }
        Random random = new Random();
        int randompart = random.nextInt() % (max - min);
        if (randompart < 0)
            randompart = -randompart;
        return randompart + min;
    }
}
