package com.mvgv70.mtcvolume;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ReceiverBrightness extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent)
  {
    Log.d(Settings.LOG_ID, "ReceiverBrightness.onReceive");
    Settings settings = Settings.get(context);
    // ���� �������� � ����������
    if (!settings.getBrightnessEnable()) return;
    if (intent.hasExtra("brightness"))
    {
      // ��������� ������� �� �������
      int brightness = intent.getIntExtra("brightness",0);
      Log.d(Settings.LOG_ID,"ReceiverBrightness "+brightness);
      if (brightness > 0)
      {
        // ��������� �������
        settings.setBrightness(brightness);
        settings.showBrightnessToast(context.getString(R.string.toast_brightness_change));
      }
    }
    else
    {
      // ���������� ������� �� ������ ���������� ��� ������ �������
      if (settings.setTimeBrightness())
        settings.showBrightnessToast(context.getString(R.string.toast_brightness_change));
    }
  }
  
}
