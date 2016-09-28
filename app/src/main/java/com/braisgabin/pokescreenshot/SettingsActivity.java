package com.braisgabin.pokescreenshot;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.braisgabin.directorypicker.DirectoryPicker;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;

import javax.inject.Inject;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class SettingsActivity extends AppCompatActivity {
  private static final String FINISH_ON_UPDATE_TRAINER_LVL = "FINISH_ON_UPDATE_TRAINER_LVL";

  public static final String TRAINER_LVL = "trainer_lvl";
  public static final String SCREENSHOT_DIR = "screenshot_dir";

  public static Intent getCallingIntent(Context context, boolean finishOnUpdateTrainerLvl) {
    final Intent intent = new Intent(context, SettingsActivity.class);

    intent.putExtra(FINISH_ON_UPDATE_TRAINER_LVL, finishOnUpdateTrainerLvl);

    return intent;
  }

  public static String screenshotDirDefault(Context context) {
    // https://github.com/android/platform_frameworks_base/blob/master/packages/SystemUI/src/com/android/systemui/screenshot/GlobalScreenshot.java#L98
    final File root = Environment.getExternalStorageDirectory();
    final File picturesDir = new File(root, Environment.DIRECTORY_PICTURES);
    final File defaultDir = new File(picturesDir, "Screenshots");
    File file;

    if (ContextCompat.checkSelfPermission(context, READ_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
      file = defaultDir;
    } else if (defaultDir.exists()) {
      file = defaultDir;
    } else {
      final FileFilter fileFilter = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
          return pathname.isDirectory();
        }
      };
      final Pattern namePattern = Pattern.compile("Screen.*", Pattern.CASE_INSENSITIVE);
      file = Utils.find(picturesDir, fileFilter, namePattern);
      if (file == null) {
        file = Utils.find(root, fileFilter, namePattern);
      }
      file = file == null ? defaultDir : file;
    }
    return file.getAbsolutePath().substring(root.getAbsolutePath().length());
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);
    if (savedInstanceState == null) {
      final Intent intent = getIntent();
      getFragmentManager()
          .beginTransaction()
          .add(R.id.f_for_fragment, SettingsFragment.newInstance(intent.getBooleanExtra(FINISH_ON_UPDATE_TRAINER_LVL, false)))
          .commit();
    }
  }


  public static class SettingsFragment extends PreferenceFragment implements DirectoryPicker.OnDirectoryClickedListener {
    public static SettingsFragment newInstance(boolean finishOnUpdateTrainerLvl) {
      Bundle args = new Bundle();
      args.putBoolean(FINISH_ON_UPDATE_TRAINER_LVL, finishOnUpdateTrainerLvl);

      SettingsFragment fragment = new SettingsFragment();
      fragment.setArguments(args);
      return fragment;
    }

    @Inject
    SharedPreferences sharedPreferences;

    private boolean finishOnUpdateTrainerLvl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      App.component(getActivity()).inject(this);

      addPreferencesFromResource(R.xml.settings);

      bindPreferenceSummaryToValue(findPreference(TRAINER_LVL));
      bindPreferenceSummaryToValue(findPreference(SCREENSHOT_DIR));

      findPreference(SCREENSHOT_DIR).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
          DirectoryPicker.newInstance(Environment.getExternalStorageDirectory(), sharedPreferences.getString(SCREENSHOT_DIR, screenshotDirDefault(getActivity())))
              .show(getChildFragmentManager(), SCREENSHOT_DIR);
          return true;
        }
      });

      this.finishOnUpdateTrainerLvl = getArguments().getBoolean(FINISH_ON_UPDATE_TRAINER_LVL, false);
      if (finishOnUpdateTrainerLvl) {
        ((MyListPreference) findPreference(TRAINER_LVL)).show(null);
      }
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #bindPreferenceSummaryToValueListener
     */
    private void bindPreferenceSummaryToValue(Preference preference) {
      // Set the listener to watch for value changes.
      preference.setOnPreferenceChangeListener(bindPreferenceSummaryToValueListener);

      // Trigger the listener immediately with the preference's
      // current value.
      bindPreferenceSummaryToValueListener.onPreferenceChange(preference,
          PreferenceManager
              .getDefaultSharedPreferences(preference.getContext())
              .getString(preference.getKey(), ""));
    }

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private Preference.OnPreferenceChangeListener bindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
      @Override
      public boolean onPreferenceChange(Preference preference, Object value) {
        String stringValue = value.toString();

        if (preference instanceof ListPreference) {
          // For list preferences, look up the correct display value in
          // the preference's 'entries' list.
          ListPreference listPreference = (ListPreference) preference;
          int index = listPreference.findIndexOfValue(stringValue);

          if (finishOnUpdateTrainerLvl && index >= 0 && preference.getKey().equals(TRAINER_LVL)) {
            getActivity().finish();
          }

          // Set the summary to reflect the new value.
          preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);

        } else if (preference.getKey().equals(SCREENSHOT_DIR)) {
          preference.setSummary(TextUtils.isEmpty(stringValue) ? screenshotDirDefault(getActivity()) : stringValue);
        } else {
          // For all other preferences, set the summary to the value's
          // simple string representation.
          preference.setSummary(stringValue);
        }
        return true;
      }
    };

    @Override
    public void onDirectoryClicked(DirectoryPicker picker, File file) {
      final String root = Environment.getExternalStorageDirectory().getAbsolutePath();
      String screenshotDir = file.getAbsolutePath().substring(root.length());
      screenshotDir = screenshotDir.isEmpty() ? "/" : screenshotDir;
      final Preference preference = findPreference(SCREENSHOT_DIR);
      preference.getEditor()
          .putString(SCREENSHOT_DIR, screenshotDir)
          .apply();
      bindPreferenceSummaryToValueListener.onPreferenceChange(preference, screenshotDir);
    }
  }
}
