package com.mvgv70.mtcvolume;

import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.util.Log;

public class ReceiverBluetooth extends BroadcastReceiver
{
  // ����������������� ������� microntek bluetooth
  private static final String BLUETOOTH_ACTION = "com.microntek.bt.report";
  private static final String BLUETOOTH_STATE = "connect_state";
  private static final int BLUETOOTH_CALL_END = 1;
  private static final int BLUETOOTH_CALL_OUT = 2;
  private static final int BLUETOOTH_CALL_IN = 3;
  //����������������� ������� �������� ������
  private static final String MUSIC_PLAYPAUSE_NOTIFY = "com.microntek.playpausemusic";
  private static final String MUSIC_PLAYPAUSE_STATE = "playstate";
  private static final String MUSIC_PLAY_ACTION = "com.microntek.playmusic";
  // PLAY/PAUSE constants
  private static final int MUSIC_NONE = 0;
  private static final int MUSIC_PLAY = 1;
  // private static final int MUSIC_PAUSE = 2;
  private static int MusicPlayState = MUSIC_NONE;
  private static boolean mPlaying = false;
  
  @Override
  public void onReceive(Context context, Intent intent)
  {
	Settings settings = Settings.get(context);
	// �������� ������� �������� � ����������
    if (!settings.getCallsEnable()) return;
    String action = intent.getAction();
    if (action.equals(BLUETOOTH_ACTION)) 
    {
      if (intent.hasExtra(BLUETOOTH_STATE))
      {
        int state = intent.getIntExtra(BLUETOOTH_STATE,-1);
        Log.d(Settings.LOG_ID,"state="+state);
        // TODO: �������� ����� ������
        if ((state == BLUETOOTH_CALL_IN) || (state == BLUETOOTH_CALL_OUT)) 
        {
          // �������� ��� ��������� ������
          mPlaying = isPlaying();
          if (mPlaying) 
          {
            // �������� �� �����, ���� ����� �������
            switchPlayPause(context);
            Settings.get(context).showCallsToast(context.getString(R.string.toast_player_paused));
          }
        }
        else if (state == BLUETOOTH_CALL_END)
        {
          // ��������� ������
          if (mPlaying && settings.getPlayAfterCallsEnable())
        	// ������� �����, ���� �� ��� ������� � ������ ������
            switchPlayPause(context);
        }
      }
      if (intent.hasExtra("connected_mac"))
      {
        Log.d(Settings.LOG_ID,"connected_mac="+intent.getStringExtra("connected_mac"));
      }
    }
    else if (action.equals(MUSIC_PLAYPAUSE_NOTIFY)) 
    {
      int playstate = intent.getIntExtra(MUSIC_PLAYPAUSE_STATE,0);
      Log.d(Settings.LOG_ID, "ReceiverMicrontek.playstate = "+playstate);
      // �������� ������� ��������� ������
      setPlayState(playstate);
    }
  } 
  
  public void setPlayState(int newState)
  {
    MusicPlayState = newState;
  }
  
  public boolean isPlaying()
  {
    return (MusicPlayState == MUSIC_PLAY);
  }
  
  public static boolean isPlayerRun()
  {
    return (MusicPlayState != MUSIC_NONE);
  }
  
  public void switchPlayPause(Context cntx)
  {
    Log.d(Settings.LOG_ID, "ReceiverMicrontek.switchPlayPause");
    Intent intent = new Intent(MUSIC_PLAY_ACTION);
    cntx.sendBroadcast(intent);
  } 

}