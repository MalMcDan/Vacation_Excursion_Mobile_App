package com.example.rmcdad308.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rmcdad308.Database.Repository;
import com.example.rmcdad308.R;
import com.example.rmcdad308.entities.Excursion;
import com.example.rmcdad308.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VacationDetails extends AppCompatActivity {
    EditText editVacationTitle;
    EditText editHotelName;
    Button editStartDate;
    Button editEndDate;
    String vacationTitle;
    String hotelName;
    String vacStartDate;
    String vacEndDate;
    DatePickerDialog.OnDateSetListener vStartDate;

    DatePickerDialog.OnDateSetListener endDate;
    final Calendar myCalendarStart = Calendar.getInstance();

    final Calendar myCalendarEnd = Calendar.getInstance();
    int vacationId;

    Repository repository;

    Vacation currentVacation;

    int numExcursions;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_details);
        editVacationTitle = findViewById(R.id.vacationtitle);
        editHotelName = findViewById(R.id.hotelname);
        editStartDate = findViewById(R.id.startdate);
        editEndDate = findViewById(R.id.enddate);
        vacationId = getIntent().getIntExtra("id", -1);
        vacationTitle = getIntent().getStringExtra("vacation name");
        hotelName = getIntent().getStringExtra("hotel name");
        vacStartDate = getIntent().getStringExtra("start date");
        vacEndDate = getIntent().getStringExtra("end date");

        editVacationTitle.setText(vacationTitle);
        editHotelName.setText(hotelName);
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        //String currentDate = sdf.format(new Date());
        editStartDate.setText(vacStartDate);
        editEndDate.setText(vacEndDate);
        repository = new Repository(getApplication());
        RecyclerView recyclerView = findViewById(R.id.excursionrecyclerview);
        repository = new Repository(getApplication());
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Excursion> filteredExcursions = new ArrayList<>();
        for (Excursion e : repository.getAllExcursions()) {
            if (e.getVacationId() == vacationId) filteredExcursions.add(e);
        }
        excursionAdapter.setExcursions(filteredExcursions);



        vStartDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendarStart.set(Calendar.YEAR, year);
                myCalendarStart.set(Calendar.MONTH, monthOfYear);
                myCalendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                updateLabelStart();

            }
        };
        editStartDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Date date;

                String info = editStartDate.getText().toString();
                if (info.equals("")) info = "09/01/23";
                try {
                    myCalendarStart.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(VacationDetails.this, vStartDate, myCalendarStart
                        .get(Calendar.YEAR), myCalendarStart.get(Calendar.MONTH),
                        myCalendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        endDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {


                myCalendarEnd.set(Calendar.YEAR, year);
                myCalendarEnd.set(Calendar.MONTH, monthOfYear);
                myCalendarEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                updateLabelStartE();
            }

        };

        editEndDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){

                Date date;

                String info = editEndDate.getText().toString();
                if (info.equals("")) info = "09/26/23";
                try {
                    myCalendarEnd.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(VacationDetails.this, endDate, myCalendarEnd
                        .get(Calendar.YEAR), myCalendarEnd.get(Calendar.MONTH),
                        myCalendarEnd.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



    }



    public void updateLabelStart() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editStartDate.setText(sdf.format(myCalendarStart.getTime()));
    }


    public void updateLabelStartE() {
        String myFormat1 = "MM/dd/yy";
        SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1, Locale.US);

        editEndDate.setText(sdf1.format(myCalendarEnd.getTime()));
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_vacationdetails, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        if (item.getItemId() == R.id.vacationsave) {

            String startDateText = editStartDate.getText().toString();
            String endDateText = editEndDate.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
            try {
                Date startDate = sdf.parse(startDateText);
                Date endDate = sdf.parse(endDateText);

                if (startDate.after(endDate)) {
                    Toast.makeText(this, "End date must be after start date.", Toast.LENGTH_LONG).show();
                } else {
                    Vacation vacation;
                    if (vacationId == -1) {
                        if (repository.getAllVacations().size() == 0) vacationId = 1;
                        else
                            vacationId = repository.getAllVacations().get(repository.getAllVacations().size() - 1).getVacationId() + 1;
                        vacation = new Vacation(vacationId, editVacationTitle.getText().toString(), editHotelName.getText().toString(), editStartDate.getText().toString(), editEndDate.getText().toString());
                        repository.insert(vacation);
                        Toast.makeText(this, "Your Journey has been added.", Toast.LENGTH_LONG).show();

                    } else {
                        try {
                            vacation = new Vacation(vacationId, editVacationTitle.getText().toString(), editHotelName.getText().toString(), startDateText, endDateText);
                            repository.update(vacation);
                            Toast.makeText(this, "Your Journey has been updated.", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {

                        }

                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            finish();
            return true;
        }
        if (item.getItemId() == R.id.vacationdelete) {
            for (Vacation vac : repository.getAllVacations()) {
                if (vac.getVacationId() == vacationId) currentVacation = vac;
            }
            numExcursions = 0;
            for (Excursion excursion : repository.getAllExcursions()) {
                if (excursion.getExcursionId() == vacationId) ++numExcursions;
            }
            if (numExcursions == 0) {
                repository.delete(currentVacation);
                Toast.makeText(VacationDetails.this, currentVacation.getVacationName() + " was deleted.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(VacationDetails.this, "Can't delete a Journey with activities attached.", Toast.LENGTH_LONG).show();
            }
            return true;
        }
        if (item.getItemId() == R.id.addVacationExcursions) {
            if (vacationId == -1)
                Toast.makeText(VacationDetails.this, "Please save a Journey before adding an activity.", Toast.LENGTH_LONG).show();
            else {
                int excursionId;
                if (repository.getAllExcursions().size() == 0) excursionId = 1;
                else
                    excursionId = repository.getAllExcursions().get(repository.getAllExcursions().size() - 1).getExcursionId() + 1;
                Excursion excursion = new Excursion(excursionId, "Click me to edit or delete", "09/15/23", vacationId);
                repository.insert(excursion);
                RecyclerView recyclerView = findViewById(R.id.excursionrecyclerview);
                final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
                recyclerView.setAdapter(excursionAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                List<Excursion> filteredExcursions = new ArrayList<>();
                for (Excursion ex : repository.getAllExcursions()) {
                    if (ex.getVacationId() == vacationId) filteredExcursions.add(ex);
                }
                excursionAdapter.setExcursions(filteredExcursions);
                return true;

            }
        }
        if (item.getItemId() == R.id.notify) {
            String sdateFromScreen = editStartDate.getText().toString();
            String edateFromScreen = editEndDate.getText().toString();

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
            try {
                Date startDate = sdf.parse(sdateFromScreen);
                Date endDate = sdf.parse(edateFromScreen);

                // Schedule notification for the start date
                scheduleNotification(this, "Your " + vacationTitle + " is starting today " + vacStartDate + " and will end " + vacEndDate, startDate.getTime(), 1);

                // Schedule notification for the end date
                scheduleNotification(this, "Your " + vacationTitle + " is ending today " + vacEndDate + " and started on " + vacStartDate, endDate.getTime(), 2);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            return true;
        }
        if(item.getItemId() == R.id.shareV) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Let's go on your " + vacationTitle + " where you'll stay at " + hotelName + ". " + "The journey will start " + vacStartDate + " and end on " + vacEndDate);
            sendIntent.putExtra(Intent.EXTRA_TITLE, "My Journey");
            sendIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void scheduleNotification(Context context, String message, long triggerTimeMillis, int notificationId) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("key", message);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, intent, PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTimeMillis, pendingIntent);
    }



    @Override
    protected void onResume() {
        super.onResume();
        RecyclerView recyclerView = findViewById(R.id.excursionrecyclerview);
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Excursion> filteredExcursions = new ArrayList<>();
        for (Excursion ex : repository.getAllExcursions()) {
            if (ex.getVacationId() == vacationId) filteredExcursions.add(ex);
        }
        excursionAdapter.setExcursions(filteredExcursions);
    }



}
