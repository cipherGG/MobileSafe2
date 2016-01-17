package com.gg.app.mobilesafe2.activity;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.gg.app.mobilesafe2.R;
import com.gg.app.mobilesafe2.service.RocketService;
import com.gg.app.mobilesafe2.utils.PreferenceUtils;
import com.gg.app.mobilesafe2.utils.VersionUtils;
import com.gg.app.mobilesafe2.utils.DialogUtils;

public class SettingsActivity extends BaseActivity {

    public static final String UPDATE = "setting_update";
    public static final String THEME = "setting_theme";
    public static final String ROCKET = "setting_rocket";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initPreference();

        initFragment();
    }

    private void initPreference() {
        PreferenceUtils.putString(this, UPDATE, "当前版本：" + VersionUtils.getVersionName(this));

        PreferenceUtils.putString(this, ROCKET, "点击开启或关闭");


    }


    private void initFragment() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(android.R.id.content, new PreFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    public class PreFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.preferences);
            setHasOptionsMenu(true);

            bindPreferenceSummaryToValue(findPreference(UPDATE));
            bindPreferenceSummaryToValue(findPreference(THEME));
            //bindPreferenceSummaryToValue(findPreference(ROCKET));

        }
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #bindPreferenceSummaryToValue
     */
    private void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(changeListener);

        // Trigger the listener immediately with the preference's current value.
        changeListener.onPreferenceChange(preference,
                PreferenceUtils.getString(preference.getContext(), preference.getKey(), ""));

        //set the listener to watch for clicked
        preference.setOnPreferenceClickListener(clickListener);
    }

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private Preference.OnPreferenceChangeListener changeListener =
            new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String key = preference.getKey();

                    //newValue是从MyPreference里取出的值
                    preference.setSummary(newValue.toString());

                    return true;
                }
            };

    private Preference.OnPreferenceClickListener clickListener = new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
            String key = preference.getKey();

            switch (key) {
                case UPDATE:
                    VersionUtils.checkVersionCode(SettingsActivity.this);

                    break;
                case THEME:
                    DialogUtils.showThemeDialog(SettingsActivity.this);

                    break;
                case ROCKET:
                    //rocketAction();

                    break;
            }
            return true;
        }
    };

    private void rocketAction() {
        boolean status = PreferenceUtils.getBoolean(this, "rocket_status", false);
        if (!status) {
            SettingsActivity.this.openService(RocketService.class);
            PreferenceUtils.putBoolean(this, "rocket_status", true);
        } else {
            SettingsActivity.this.stopService(RocketService.class);
            PreferenceUtils.putBoolean(this, "rocket_status", false);
        }
    }

}
