package com.ecuacion.finalproject.data;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.ecuacion.finalproject.DateTimePicker.notification;
import com.ecuacion.finalproject.R;
import com.ecuacion.finalproject.databinding.ActivityAddDataBinding;
import com.ecuacion.finalproject.ui.home.MainScreen;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class add_task extends AppCompatActivity {

    EditText title, message, updateName, updateMessage;
    MaterialButton btnTime, btnDate;
    MaterialButton btnSave, btnBack, btnUpdate;
    TextView viewDate, viewTime;


    //for calendar
    private static final String channelID = "channel1";
    private static final int notificationID = 1;
    private static final String titleExtra = "titleExtra";
    private static final String messageExtra = "messageExtra";

    private ActivityAddDataBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);


        //for calendar
        binding = ActivityAddDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        createNotificationChannel();

        title = findViewById(R.id.add_title);
        message = findViewById(R.id.add_message);

        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);

//        btnUpdate = findViewById(R.id.updateDatePicker);
//        btnUpdate.setOnClickListener(view -> {
//            if (isDataValid()) {
//                // scheduleNotification();
//                insertData();
//                startActivity(new Intent(getApplicationContext(), MainScreen.class));
//                finish();
//            } else {
//                Toast.makeText(this, "Please input both fields", Toast.LENGTH_SHORT).show();
//            }
//
//        });

//        btnDate = findViewById(R.id.btnDate);
//
//        viewDate = findViewById(R.id.viewDate);
//        btnTime = findViewById(R.id.btnTime);


//        btnDate.setOnClickListener(view -> {
//            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
//                    .setTitleText("Select a Date")
//                    .build();
//            datePicker.addOnPositiveButtonClickListener(selection -> {
//                String date = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date(selection));
//                viewDate.setText(MessageFormat.format("Selected Date", date));
//                // Do something with the selected date
//            });
//            datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
//        });


//        btnTime.setOnClickListener(view -> {
//            MaterialTimePicker.Builder timePickerBuilder = new MaterialTimePicker.Builder()
//                    .setTimeFormat(TimeFormat.CLOCK_12H)
//                    .setTitleText("Select a Time");
//
//            MaterialTimePicker timePicker = timePickerBuilder.build();
//
//            timePicker.addOnPositiveButtonClickListener(dialog -> {
//                int hour = timePicker.getHour();
//                int minute = timePicker.getMinute();
//                String selectedTime = String.format(Locale.getDefault(), "%02d:%02d %s",
//                        (hour % 12 == 0) ? 12 : hour % 12, minute, (hour < 12) ? "AM" : "PM");
//
//                viewTime.setText("Selected Time: " + selectedTime);
//
//                // Do something with the selected time
//            });
//
//            timePicker.show(getSupportFragmentManager(), "TIME_PICKER");
//        });


        btnSave.setOnClickListener(view -> {
            if (isDataValid()) {
                // scheduleNotification();
                insertData();
                startActivity(new Intent(getApplicationContext(), MainScreen.class));
                finish();
            } else {
                Toast.makeText(this, "Please input both fields", Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), MainScreen.class));
            finish();
        });


    }

    //condition if Textfield is Empty
    private boolean isDataValid() {
        String title = binding.addTitle.getText().toString();
        String desciption = binding.addMessage.getText().toString();
        // Check if both fields have values
        return !TextUtils.isEmpty(title) && !TextUtils.isEmpty(desciption);
    }


    @SuppressLint("ScheduleExactAlarm")
    private void insertData() {
        String add_title = title.getText().toString().trim();
        String add_message = message.getText().toString().trim();

        //for calendar
        String notif_title = binding.addTitle.getText().toString();
        String notif_message = binding.addMessage.getText().toString();

        if (TextUtils.isEmpty(notif_title) || TextUtils.isEmpty(notif_message)) {
            title.setError("Field is required");
            message.setError("Field is required");
            return;
        }

        Intent intent = new Intent(getApplicationContext(), notification.class);
        intent.putExtra(titleExtra, notif_title);
        intent.putExtra(messageExtra, notif_message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(),
                notificationID,
                intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        long time = getTime();
        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                time,
                pendingIntent
        );
        showAlert(time, notif_title, notif_message);

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1; // Month is zero-based
        int year = calendar.get(Calendar.YEAR);

        // All input fields are valid, proceed with data insertion
        Map<String, Object> map = new HashMap<>();
        map.put("title", add_title);
        map.put("message", add_message);

        map.put("hour", hour);
        map.put("minute", minute);
        map.put("day", day);
        map.put("month", month);
        map.put("year", year);


        FirebaseDatabase.getInstance().getReference().child("Task").push()
                .setValue(map)
                .addOnSuccessListener(aVoid -> {
                    title.setText("");
                    message.setText("");

                    Toast.makeText(getApplicationContext(), "Successfully Created Task", Toast.LENGTH_LONG).show();
                })
                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Could not add task", Toast.LENGTH_LONG).show());
    }


    //for calendar
    @SuppressLint("ScheduleExactAlarm")
    void scheduleNotification() {
        String title = binding.addMessage.getText().toString();
        String message = binding.addTitle.getText().toString();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(message)) {
            // Handle the case where either the title or message is empty
            // You can show an error message or take appropriate action here.
            return; // Don't proceed with scheduling the notification.
        }

        Intent intent = new Intent(getApplicationContext(), Notification.class);
        intent.putExtra(titleExtra, title);
        intent.putExtra(messageExtra, message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(),
                notificationID,
                intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );


        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        long time = getTime();
        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                time,
                pendingIntent
        );
        showAlert(time, title, message);
    }

    private void showAlert(long time, String title, String message) {
        Date date = new Date(time);
        DateFormat dateFormat = android.text.format.DateFormat.getLongDateFormat(getApplicationContext());
        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(getApplicationContext());

        new AlertDialog.Builder(this)
                .setTitle("Notification Scheduled")
                .setMessage("Title: " + title +
                        "\nMessage: " + message +
                        "\nAt: " + dateFormat.format(date) + " " + timeFormat.format(date))
                .setPositiveButton("Okay", null)
                .show();
    }

    private long getTime() {
        int minute = binding.timePicker.getMinute();
        int hour = binding.timePicker.getHour();
        int day = binding.datePicker.getDayOfMonth();
        int month = binding.datePicker.getMonth();
        int year = binding.datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);
        return calendar.getTimeInMillis();
    }

//    private void createNotificationChannel() {
//        String name = "Notif Channel";
//        String desc = "A Description of the Channel";
//        int importance = NotificationManager.IMPORTANCE_DEFAULT;
//        NotificationChannel channel = new NotificationChannel(channelID, name, importance);
//        channel.setDescription(desc);
//        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        notificationManager.createNotificationChannel(channel);
//    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainScreen.class));
        finish();
    }


}