package com.superjoy.someone.model;

import com.superjoy.someone.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.LinkedList;

/**
 * @Author Ping
 * @create 2021/3/31 15:31
 */
@Data
@AllArgsConstructor
public class ProfileViews {
    String ownerId;
    Integer count;
    LinkedList<Viewer> viewers;

    public int add(Viewer viewer) {
        viewers.add(viewer);
        if(viewers.size()> Constants.MAX_VIEWERS){
            viewers.removeFirst();
        }
        return ++count;
    }
}
