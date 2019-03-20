package com.zjzf.shoescircleandroid.model.event;

import com.zjzf.shoescircle.lib.utils.ToolUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 陈志远 on 2018/10/17.
 */
public class RefreshEvent {
    List<String> classNameList;

    public RefreshEvent(Class... targetClass) {
        if (targetClass != null) {
            classNameList = new ArrayList<>(targetClass.length);
            for (Class aClass : targetClass) {
                classNameList.add(aClass.getSimpleName());
            }
        }
    }

    public boolean contains(Class clazz) {
        if (clazz == null || ToolUtil.isListEmpty(classNameList)) return false;
        return classNameList.contains(clazz.getSimpleName());
    }
}
