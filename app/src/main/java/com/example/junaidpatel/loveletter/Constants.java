package com.example.junaidpatel.loveletter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by junaid.patel on 2018-01-31.
 */

public class Constants {
    public HashMap<Integer, Integer> drawableHashMap;
    ArrayList deck = new ArrayList(Arrays.asList(1,1,1,1,1,2,2,3,3,4,4,5,5,6,7,8));
//    ArrayList deck = new ArrayList(Arrays.asList(4,4,4,4,4));

    public Constants() {
        drawableHashMap = new HashMap<Integer, Integer>();
        drawableHashMap.put(1, R.drawable.guard);
        drawableHashMap.put(2, R.drawable.priest);
        drawableHashMap.put(3, R.drawable.baron);
        drawableHashMap.put(4, R.drawable.handmaid);
        drawableHashMap.put(5, R.drawable.prince);
        drawableHashMap.put(6, R.drawable.king);
        drawableHashMap.put(7, R.drawable.countess);
        drawableHashMap.put(8, R.drawable.princess);
    }
}
