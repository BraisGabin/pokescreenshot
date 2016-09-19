package com.braisgabin.pokescreenshot;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

// Extracted from: http://stackoverflow.com/a/27083833/842697
public class NotificationActivity extends Activity {
  private static final String EXTRA_NOTIFICATION_ID = "EXTRA_NOTIFICATION_ID";

  public static Intent getCallingIntent(Context context, int notificationId) {
    Intent intent = new Intent(context, NotificationActivity.class);
    intent.putExtra(EXTRA_NOTIFICATION_ID, notificationId);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    return intent;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    final NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    final int notificationId = getIntent().getIntExtra(EXTRA_NOTIFICATION_ID, -1);
    manager.cancel(notificationId);
    finish(); // since finish() is called in onCreate(), onDestroy() will be called immediately
  }
}