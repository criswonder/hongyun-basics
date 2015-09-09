package com.xixi.android;

import android.app.Application;
import android.content.Context;

/**
 * 此类必须在程序刚刚启动的时候注册，这样在程序的
 * 运行中就可以通过此类来获取全局的Application了
 * 最好的实现是自定义一个Application的子类，然后在此子类
 * 的初始化中注册。
 * @author AndyMao
 *
 */
public class MucangConfig {

    private static Application application;


    public static void setApplication(Application application) {
        MucangConfig.application = application;
    }


    public static String getPackageName() {
        Context context = getContext();
        if (context != null) {
            return context.getPackageName();
        }
        return null;
    }

    public static Context getContext() {
        return application;
    }


}
