package com.mvgv70.mtcvolume;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
// import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.Timer;

import com.luckycatlabs.sunrisesunset.SunriseSunsetCalculator;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class ReceiverCoord extends BroadcastReceiver {
	
  private static Timer timerSunrise = null;
  private static Timer timerSunset = null;
  private static TimerBrightness taskSunrise = null;
  private static TimerBrightness taskSunset = null;
  
  // �������� ��������
  public static void createTimers()
  {
    Log.d(Settings.LOG_ID,"createTimers()");
    timerSunrise = new Timer();
    timerSunset = new Timer();
    taskSunrise = new TimerBrightness();
    taskSunset = new TimerBrightness();
  }
  
  // ��������� ��������
  public static void cancelTimers()
  {
    Log.d(Settings.LOG_ID,"cancelTimers()");
    // sunrise
    taskSunrise.cancel();
    taskSunrise = null;
    timerSunrise.cancel();
    timerSunrise = null;
    // sunset
    taskSunset.cancel();
    taskSunset = null;
    timerSunset.cancel();
    timerSunset = null;
  }
	
  @Override
  public void onReceive(Context context, Intent intent)
  {
    Log.d(Settings.LOG_ID,"ReceiverCoord.onReceive");
    Settings settings = Settings.get(context);
    if (!intent.hasExtra(LocationManager.KEY_LOCATION_CHANGED)) return;
    Location location = (Location)intent.getExtras().get(LocationManager.KEY_LOCATION_CHANGED);
    // ��������� ������� ��������
    if (!settings.getBrightnessEnable()) return;
    // ��������� ����� �������� � ������
    com.luckycatlabs.sunrisesunset.dto.Location gpslocation = new com.luckycatlabs.sunrisesunset.dto.Location(location.getLatitude(), location.getLongitude());
    SunriseSunsetCalculator calc = new SunriseSunsetCalculator(gpslocation,TimeZone.getDefault());
    String sunrise = calc.getOfficialSunriseForDate(Calendar.getInstance());
    String sunset = calc.getOfficialSunsetForDate(Calendar.getInstance());
    Log.d(Settings.LOG_ID, "sunrise="+sunrise+", sunset="+sunset);
    // �������� ����� �������� � ������
    saveSunriseSunset(settings, sunrise, sunset);
    // ������� ������� �������
    context.sendBroadcast(new Intent(context,ReceiverBrightness.class));
    // ��������� ������� ������� � ������
    Date sunriseDate = settings.getEventDate("sunrise");
    if (sunriseDate != null)
    {
      taskSunrise.setParams("sunrise",settings);
      timerSunrise.schedule(taskSunrise, sunriseDate, AlarmManager.INTERVAL_DAY);
      Log.d(Settings.LOG_ID,"set sunrise timer at "+DateFormat.getInstance().format(sunriseDate));
    }
    Date sunsetDate = settings.getEventDate("sunset");
    if (sunsetDate != null)
    {
      taskSunset.setParams("sunset",settings);
      timerSunset.schedule(taskSunset, sunsetDate, AlarmManager.INTERVAL_DAY);
      Log.d(Settings.LOG_ID,"set sunset timer at "+DateFormat.getInstance().format(sunsetDate));
    }
  }
	  
  private void saveSunriseSunset(Settings settings, String sunrise, String sunset)
  {
    // ��������� ����� ������� � ������ ������ �� �����
    int sunriseHour = settings.parseString(sunrise.substring(0,2),-1);
    int sunriseMin = settings.parseString(sunrise.substring(3,5),-1);
    int sunsetHour = settings.parseString(sunset.substring(0,2),-1);
    int sunsetMin = settings.parseString(sunset.substring(3,5),-1);
    // ���� ��������� �������
    int correction = settings.getBrightnessCorrection();
    // + ����������� ������� ����� �����
    // - ����������� ������ ����� �����
    if (correction != 0)
    {
      Log.d(Settings.LOG_ID,"brightness correction time "+correction+" min");
      Calendar calendar = Calendar.getInstance();
      // ������
      calendar.setTimeInMillis(java.lang.System.currentTimeMillis());
      calendar.set(Calendar.HOUR_OF_DAY, sunriseHour);
      calendar.set(Calendar.MINUTE, sunriseMin);
      // ��������� �������  	    
      calendar.add(Calendar.MINUTE, -correction);
      // ����������������� ����� �������
      sunriseHour = calendar.get(Calendar.HOUR_OF_DAY);
      sunriseMin = calendar.get(Calendar.MINUTE);
      // �����
      calendar.setTimeInMillis(java.lang.System.currentTimeMillis());
      calendar.set(Calendar.HOUR_OF_DAY, sunsetHour);
      calendar.set(Calendar.MINUTE, sunsetMin);
      // ��������� ������  	    
      calendar.add(Calendar.MINUTE, correction);
      // ����������������� ����� ������
      sunsetHour = calendar.get(Calendar.HOUR_OF_DAY);
      sunsetMin = calendar.get(Calendar.MINUTE);
    }
    
    // ��������
    settings.setSunsetSunrise(sunriseHour,sunriseMin,sunsetHour,sunsetMin);
  }

}
