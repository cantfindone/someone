package com.superjoy.someone.utils;

import cn.hutool.core.util.ObjectUtil;
import com.superjoy.someone.model.UserInfo;

/**
 * @author Ping
 */
public class UserContext {

    private static ThreadLocal<UserInfo> threadLocal = new ThreadLocal<>();

    public static UserInfo current() {
        return threadLocal.get();
    }

    public static String currentUserId() {
        UserInfo user = threadLocal.get();
        if (ObjectUtil.isNotNull(user)) {
            return user.getUserId();
        }
        return null;
    }

    public static void setUser(UserInfo user) {
        threadLocal.set(user);
    }
}
