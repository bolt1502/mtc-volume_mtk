package com.mvgv70.mtcvolume;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ReceiverAppVolume extends BroadcastReceiver {
  // ����������������� ������� microntek ���/����
  private static final String CANBUS_ACTION = "com.microntek.canbusdisplay";
  private static final String APP_NAME = "type";
  
  private static String current_appname = "";
	
  @Override
  public void onReceive(Context context, Intent intent)
  {
    Settings settings = Settings.get(context); 
    // ������� � ����������
    if (!settings.getAppsEnable()) return;
    String action = intent.getAction();
    if (action.equals(CANBUS_ACTION)) 
    {
      String event = intent.getStringExtra(APP_NAME);
      if (event.endsWith("-on")) 
      {
        String appname = event.substring(0,event.length()-3);
        Log.d(Settings.LOG_ID,"event="+event+", appname="+appname+", current_appname="+current_appname);
        if (!current_appname.isEmpty())
        {
          // ���������
          int volume = settings.getVolume();
          // ��������� ��������� ��� �������� ����������
          int currentAdj = settings.getAppVolumeAdj(current_appname);
          // ��������� ��������� ��� ������������ ����������
          int newAdj = settings.getAppVolumeAdj(appname);
          Log.d(Settings.LOG_ID,"volume="+volume+", currentAdj="+currentAdj+", newAdj="+newAdj);
          if (newAdj != currentAdj)
          {
            // ������ ���������
            int volumeNew = volume-currentAdj+newAdj;
            if (volumeNew <= 0) volumeNew = 1;
            Log.d(Settings.LOG_ID,"volume="+volume+", currentAdj="+currentAdj+", newAdj="+newAdj+", volumeNew="+volumeNew);
            settings.setVolume(volumeNew);
            settings.showAppToast(context.getString(R.string.toast_volume_app_corr)+" "+(newAdj-currentAdj));
          }
        }
        current_appname = appname;
      }
    }
  }

}
