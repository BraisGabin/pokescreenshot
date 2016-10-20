package com.braisgabin.pokescreenshot;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.f2prateek.rx.preferences.Preference;

import javax.inject.Inject;
import javax.inject.Named;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.braisgabin.pokescreenshot.SettingsActivity.TRAINER_LVL;

public class MainActivity extends AppCompatActivity {
  private static final int REQUEST_CODE_PERMISSION = 0;
  private static final int REQUEST_CODE_TRAINER_LVL = 1;

  @Inject
  @Named(TRAINER_LVL)
  Preference<String> trainerLvl;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    App.component(this).inject(this);
    checkPermissions();
  }

  private void checkPermissions() {
    if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
    } else {
      checkTrainerLevel();
    }
  }

  private void checkTrainerLevel() {
    if (!trainerLvl.isSet()) {
      startActivityForResult(SettingsActivity.getCallingIntent(this, true), REQUEST_CODE_TRAINER_LVL);
      Toast.makeText(getApplicationContext(), getString(R.string.select_trainter_level), Toast.LENGTH_LONG).show();
    } else {
      runService();
    }
  }

  private void runService() {
    startService(ScreenshotService.getCallingIntent(this));
    finish();
  }

  @Override
  public void finish() {
    findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
    super.finish();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
      case REQUEST_CODE_TRAINER_LVL:
        checkTrainerLevel();
        break;
      default:
        super.onActivityResult(requestCode, resultCode, data);
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    switch (requestCode) {
      case REQUEST_CODE_PERMISSION:
        if (grantResults[0] == PERMISSION_GRANTED) {
          checkTrainerLevel();
        } else {
          Toast.makeText(getApplicationContext(), R.string.permission_requited, Toast.LENGTH_LONG).show();
          finish();
        }
        break;
      default:
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
  }
}
