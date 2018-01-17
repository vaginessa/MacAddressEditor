package com.woc.mac.util;

import android.app.Application;
import android.content.Context;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

/**
 * Created by Administrator on 2017/2/13.
 */
public class Utils {

    public static String getMac()
    {
        String macSerial = null;
        String str = "";

        try
        {
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str;)
            {
                str = input.readLine();
                if (str != null)
                {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (IOException ex) {
            // 赋予默认值
            ex.printStackTrace();
        }
        return macSerial;
    }
    public  static Application getApplicationContext() {
         Application application = null;
//        try {
//            application = (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null, (Object[]) null);
//        }catch (Exception e) {
//            e.printStackTrace();
//        }

        try {
             application = (Application) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication").invoke(null, (Object[]) null);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return application;
    }


    public  static  byte[] macToByte(String s)
    {
        byte[] result=new byte[6];
        for (int i=0;i<result.length;i++)
        {
            result[i]=(byte)Integer.parseInt(s.substring(3*i,3*i+2),16);
        }

        for (int i=0;i<result.length;i++)
        {
            if(result[i]!=0)
            {
                return  result;
            }
        }
        return null;
    }
}
