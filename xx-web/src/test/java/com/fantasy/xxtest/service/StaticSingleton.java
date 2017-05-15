package com.fantasy.xxtest.service;

/**
 * @author li.fang
 * @since 2017/5/9
 */
public class StaticSingleton {

    private StaticSingleton(){
    }

    private static class SingletonHolder{
        private static StaticSingleton instance = new StaticSingleton();
    }

    public static StaticSingleton getInstance(){
        return SingletonHolder.instance;
    }
}
