package com.mvgv70.mtcvolume;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ReceiverBackView extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent)
  {
    Log.d(Settings.LOG_ID, "ReceiverBackView.onReceive");
    // ������� ������� ������ ��� ������ �� ������ ������ ������� ����
    context.sendBroadcast(new Intent(context,ReceiverBrightness.class));
  }

}
