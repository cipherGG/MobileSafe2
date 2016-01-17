package com.gg.app.mobilesafe2.utils;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;


import com.gg.app.mobilesafe2.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * onCancelListener
 */
public class DialogUtils {

    /**
     * 普通对话框
     *
     * @param context
     * @param title
     * @param message
     * @param view
     * @param positive
     * @param negative
     * @param callBack2
     * @return
     */
    public static AlertDialog showAlertDialog(Context context, String title, String message, View view, String positive, String negative, final CallBack2 callBack2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setView(view);
        builder.setPositiveButton(positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callBack2.positive();
            }
        });
        builder.setNegativeButton(negative, null);
        AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }

    public interface CallBack2 {
        void positive();
    }

    /**
     * 进度对话框
     *
     * @param context
     * @param title
     * @param message
     * @param horizontal
     * @return
     */
    public static ProgressDialog showProgressDialog(Context context, String title, String message, boolean horizontal) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        if (horizontal)
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.show();
        return dialog;
    }

    /**
     * 时间对话框
     *
     * @param activity
     */
    public static void showDatePickerDialog(AppCompatActivity activity) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(activity.getSupportFragmentManager(), "datePicker");

    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        public int year;
        int month;
        int day;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user

//            Snackbar.make(TVProgramActivity.recyclerView,
//                    year + "年" + (month + 1) + "月" + day + "日",
//                    Snackbar.LENGTH_SHORT).show();
            Calendar c = Calendar.getInstance();
            c.set(year, month, day);
            Date date = c.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //set TVProgramActivity.date
//            TVProgramActivity.date = sdf.format(date);
//            TVProgramActivity.updateItems();
        }
    }

    /**
     * 样式对话框
     *
     * @param activity
     */
    public static void showThemeDialog(AppCompatActivity activity) {
        ThemeDialog dialog = new ThemeDialog();
        dialog.show(activity.getSupportFragmentManager(), "theme");
    }

    public static class TimePickerFragment extends android.app.DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            Toast.makeText(getActivity(), hourOfDay + " " + minute, Toast.LENGTH_SHORT).show();
        }
    }

    public static class ThemeDialog extends DialogFragment implements View.OnClickListener {
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container
                , Bundle savedInstanceState) {
            //hide the dialog title
            this.getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

            final View layout = inflater.inflate(R.layout.dialog_theme, container, false);
            layout.findViewById(R.id.blue_theme).setOnClickListener(this);
            layout.findViewById(R.id.indigo_theme).setOnClickListener(this);
            layout.findViewById(R.id.green_theme).setOnClickListener(this);
            layout.findViewById(R.id.red_theme).setOnClickListener(this);
            layout.findViewById(R.id.blue_grey_theme).setOnClickListener(this);
            layout.findViewById(R.id.black_theme).setOnClickListener(this);
            layout.findViewById(R.id.purple_theme).setOnClickListener(this);
            layout.findViewById(R.id.orange_theme).setOnClickListener(this);
            layout.findViewById(R.id.pink_theme).setOnClickListener(this);
            return layout;
        }

        //call when dialog show
        @Override
        public void onStart() {
            super.onStart();
            Dialog dialog = getDialog();
            if (dialog != null) {
                //get the size fo parent
                int width = ViewGroup.LayoutParams.MATCH_PARENT;
                int height = ViewGroup.LayoutParams.MATCH_PARENT;
                //get dialog window and set window the size
                Window dialogWindow = dialog.getWindow();
                dialogWindow.setLayout(width, height);
            }
        }

        @Override
        public void onClick(View v) {

            String theme;
            int i = v.getId();
            switch (i) {
                case R.id.blue_theme:
                    theme = "Blue";
                    break;
                case R.id.indigo_theme:
                    theme = "Indigo";
                    break;
                case R.id.green_theme:
                    theme = "Green";
                    break;
                case R.id.red_theme:
                    theme = "Red";
                    break;
                case R.id.blue_grey_theme:
                    theme = "BlueGrey";
                    break;
                case R.id.black_theme:
                    theme = "Black";
                    break;
                case R.id.orange_theme:
                    theme = "Orange";
                    break;
                case R.id.purple_theme:
                    theme = "Purple";
                    break;
                case R.id.pink_theme:
                    theme = "Pink";
                    break;
                default:
                    theme = "Green";
                    break;
            }
            PreferenceUtils.putString(getContext(), "setting_theme", theme);
            //this method can be lay in this line or next line ,this line have specify animation
            getActivity().finish();
            //start activity and reset the theme in onCreate method
            //startActivity(new Intent(getContext(), HomeActivity.class));
        }
    }


}
