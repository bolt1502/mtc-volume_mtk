package com.mvgv70.mtcvolume;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class ReceiverSpeed extends BroadcastReceiver {
  private static double last_speed = 0;
	  
  @Override
  public void onReceive(Context context, Intent intent)
  {
    if (!intent.hasExtra(LocationManager.KEY_LOCATION_CHANGED)) return;
    Location location = (Location)intent.getExtras().get(LocationManager.KEY_LOCATION_CHANGED);
    Settings settings = Settings.get(context);
    // ���������� ��������� ��������
    if (!settings.getSpeedEnable()) return;
    // ��� �������� � �������
    if (!location.hasSpeed()) return;
    // ������� ����� mute
    if (settings.getMute()) return;
    // ������� ���������
    int volume = settings.getVolume(context);
    // ������� ���������
    if (volume == 0) return;
    // ������� ��������
    double speed = location.getSpeed();
    // ������� m/s � km/h
    speed *= 3.6; 
    if ((int)speed == (int)last_speed) return;
    List<Integer> speed_steps = settings.getSpeedValues();
    // �� ������� ������ ������ ���������
    int volChange = settings.getSpeedChangeValue();
    // ���-�� ����������� �� ��������
    int changeIncr = 0;
    // ����� ���������
    int volumeNew = 0;
    // ����������� ����� ��������/���������
    String volumeDir = "";
    // ������������� ��� ����������� ��������		
    if (speed > last_speed) 
      volumeDir = "+";
    else
      volumeDir = "-";
    // �� ������� ������ ����� ��������� ���������
    for (Integer spd_step : speed_steps)
    {
      if (last_speed >= spd_step) 
        changeIncr -= volChange;
      if (speed >= spd_step) 
        changeIncr += volChange;
    }
    // ����� ������� ���������
    volumeNew = volume + changeIncr;
    if (volumeNew <= 0) volumeNew = 1;
    // ��������� ��������� �� ��������
    if (volumeNew != volume)
    {
      Log.d(Settings.LOG_ID,"speed="+(int)speed+", last_speed="+(int)last_speed+", changeIncr="+changeIncr+", volChange="+volChange+", volume="+volume+", volumeNew="+volumeNew);
      // ������ ���������
      settings.setVolume(volumeNew);
      settings.showSpeedToast(context.getString(R.string.toast_volume_speed_set)+" "+volumeDir+" ("+volumeNew+")");
    }
    last_speed = speed;
  }

}
