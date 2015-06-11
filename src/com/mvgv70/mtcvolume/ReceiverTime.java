package com.mvgv70.mtcvolume;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ReceiverTime extends BroadcastReceiver {
	
  @Override
  public void onReceive(Context context, Intent intent)
  {
	// TODO: ����� �� ������������ ��������� ��������� ����?
	String action = intent.getAction();
    Log.d(Settings.LOG_ID,"ReceiverTime.onReceive "+action);
    // ��������� ������� ��� �������� �����, ������������ �������
    ReceiverCoord.cancelTimers();
    ReceiverCoord.createTimers();
  }

}
