package com.zjzf.shoescircleandroid.utils;


import com.zjzf.shoescircleandroid.R;

/**
 * Created by 陈志远 on 2018/8/5.
 */
public class LevelUtil {

    public static int getLevelIconResource(int level) {
        switch (level) {
            case 0:
                //青铜.
                return R.drawable.ic_level_copper;
            case 1:
                //白银
                return R.drawable.ic_level_silver;
            case 2:
                //黄金
                return R.drawable.ic_level_gold;
            case 3:
                //铂金
                return R.drawable.ic_level_platinum;
            case 4:
                //钻石
                return R.drawable.ic_level_diamond;
            default:
                return R.drawable.ic_level_copper;

        }

    }
}
