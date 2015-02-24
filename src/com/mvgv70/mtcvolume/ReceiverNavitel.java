package com.mvgv70.mtcvolume;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ReceiverNavitel extends BroadcastReceiver
{
  // ����������������� ������� microntek ����
  private static final String ACC_ACTION = "com.microntek.acc";
  private static final String ACC_STATE = "accstate";
  private static final String ACC_OFF = "accoff";
  // ������������ ������� �������
  private static final String NAVITEL_RESTART_ACTION = "com.device.poweroff";
  
  @Override
  public void onReceive(Context context, Intent intent)
  {
    Settings settings = Settings.get(context);
    // ���������� ������� ������� � ����������
    if (!settings.getNavitelEnable()) return;
    //
    String action = intent.getAction();
    Log.d(Settings.LOG_ID,"ReceiverNavitel "+action);
    if (action.equals(ACC_ACTION))
    {
      String accState = intent.getStringExtra(ACC_STATE);
      if (accState.equals(ACC_OFF))
      {
        Log.d(Settings.LOG_ID,"send "+NAVITEL_RESTART_ACTION+" to Navitel");
        // ���������� ���������, ������ ��������� �������
        Intent ni = new Intent(NAVITEL_RESTART_ACTION);
        context.sendBroadcast(ni);
      }
    }
  }
	  
}
