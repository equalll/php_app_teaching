package com.lib.base.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jarylan on 2017/3/21.
 * 被观察者抽象类
 */

public abstract class Subject {

    /**
     * 用来保存注册的观察者对象
     */
    private List<Observer> observers = new ArrayList<>();

    /**
     * 添加观察者对象
     * @param observer  观察者对象
     */
    public void attach(Observer observer){
        observers.add(observer);
    }

    /**
     * 删除观察者对象
     * @param observer  观察者对象
     */
    public void detach(Observer observer){
        observers.remove(observer);
    }

    /**
     * "我主良缘" 账号登录异常
     *
     * 通知所有注册的观察者对象
     */
    protected void AccountLoginStateNotify(int state){
        for(Observer observer : observers){
            observer.loginStateChange(state);
        }
    }

    /**
     * 环信登录异常
     *
     * 通知所有注册观察者对象
     */
    protected void chatLoginExceptionNotify(int state){
        for(Observer observer : observers){
            observer.chatLoginException(state);
        }
    }
}
