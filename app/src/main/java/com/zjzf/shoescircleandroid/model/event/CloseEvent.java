package com.zjzf.shoescircleandroid.model.event;

import com.zjzf.shoescircle.lib.utils.ToolUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 陈志远 on 2018/12/15.
 */
public class CloseEvent {
    private List<String> className;

    public CloseEvent(Class... clazz) {
        className = new ArrayList<>();
        for (Class aClass : clazz) {
            if (aClass != null) {
                className.add(aClass.getSimpleName());
            }
        }
    }

    public boolean contains(Class clazz) {
        return !(ToolUtil.isListEmpty(className) || clazz == null) && className.contains(clazz.getSimpleName());
    }

}
