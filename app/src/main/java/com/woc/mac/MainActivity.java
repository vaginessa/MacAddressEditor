package com.woc.mac;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.woc.mac.util.Utils;


public class MainActivity extends Activity {

    private static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getApplicationContext();
        FragmentManager fragmentManager=getFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(android.R.id.content,new SettingFragment());
        fragmentTransaction.commit();

    }

    public static class SettingFragment extends PreferenceFragment {
        private  SharedPreferences sharedPreferences;
        private  String patternMac="^[A-Fa-f0-9]{2}(:[A-Fa-f0-9]{2}){5}$";
        private Toast toast;
        @Override
        public View onCreateView(final LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {

            sharedPreferences=MainActivity.context.getSharedPreferences("data", Context.MODE_WORLD_READABLE);

            sharedPreferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener()
            {

                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if(!sharedPreferences.getString("editMAC", Utils.getMac()).matches(patternMac))
                {
                    showToast("警告：输入的MAC地址不合法！");
                }
                }
            });
           getPreferenceManager().setSharedPreferencesMode(MODE_WORLD_READABLE);
           getPreferenceManager().setSharedPreferencesName("data");

            addPreferencesFromResource(R.xml.settings);

            return super.onCreateView(inflater, container, savedInstanceState);
        }

        public void showToast(CharSequence text)
        {
            if(toast==null)
                toast=Toast.makeText(getContext(),text,Toast.LENGTH_LONG);
            else
                toast.setText(text);
            toast.show();
        }
    }
}
