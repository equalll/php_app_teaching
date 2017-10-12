package com.lib.base.observer;

/**
 * Created by jarylan on 2017/3/21.
 * 观察者抽象类
 */

public interface Observer {

    /**
     * 登录状态
     */
    void loginStateChange(int state);

    /**
     * 环信登录异常
     */
    void chatLoginException(int state);

}
