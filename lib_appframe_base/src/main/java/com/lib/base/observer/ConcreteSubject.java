package com.lib.base.observer;

/**
 * Created by jarylan on 2017/3/21.
 * 具体被观察者类 ， 状态变化则通知观察者们
 */

public class ConcreteSubject extends Subject {

    private static ConcreteSubject concreteSubject;

    private ConcreteSubject(){

    }

    public static ConcreteSubject getInstance(){
        if(concreteSubject == null){
            synchronized (ConcreteSubject.class) {
                if (concreteSubject == null) {
                    concreteSubject = new ConcreteSubject();
                }
            }
        }
        return concreteSubject;
    }

    public void loginChange(int newState){
        //状态发生改变，通知各个观察者
        this.AccountLoginStateNotify(newState);
    }

    public void chatLoginException(int state){
        //环信登录异常
        this.chatLoginExceptionNotify(state);
    }
}
