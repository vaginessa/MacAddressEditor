package com.woc.mac;

import android.app.AndroidAppHelper;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.woc.mac.MainActivity;
import com.woc.mac.ThisApplication;
import com.woc.mac.util.Utils;

import java.net.NetworkInterface;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
/**
 * Created by Administrator on 2017/2/13.
 */
public class XposedFilter implements IXposedHookLoadPackage,IXposedHookZygoteInit {

    private XSharedPreferences sharedPreferences;
   private final String MY_PACKAGE_NAME= MainActivity.class.getPackage().getName();
   private  String mac=null;
    boolean isGet=false;
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {

       if(sharedPreferences.hasFileChanged()) {
           sharedPreferences.reload();
            mac = sharedPreferences.getString("editMAC", null);
       }

        if(mac==null||mac.equals("00:00:00:00:00:00"))
            return;
        findAndHookMethod("android.net.wifi.WifiInfo",loadPackageParam.classLoader,"getMacAddress",new XC_MethodReplacement()
        {
            @Override
            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {


                //XposedBridge.log("------------------>(sharedPreferences1==null)"+(sharedPreferences==null));
                XposedBridge.log("------------------>mac1:"+mac);
                return mac;
            }
        });

        findAndHookMethod("java.net.NetworkInterface",loadPackageParam.classLoader,"getHardwareAddress",new XC_MethodReplacement()
        {
            @Override
            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                //XposedBridge.log("------------------>(sharedPreferences2==null)"+(sharedPreferences==null));
                XposedBridge.log("------------------>mac2:"+mac);
                return Utils.macToByte(mac);
            }
        });
    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        sharedPreferences= new XSharedPreferences(MY_PACKAGE_NAME,"data");
        sharedPreferences.makeWorldReadable();
        XposedBridge.log("------------------>MY_PACKAGE_NAME:"+MY_PACKAGE_NAME);
    }
}
