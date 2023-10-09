package com.example.rmcdad308.UI;



import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rmcdad308.Database.Repository;
import com.example.rmcdad308.R;
import com.example.rmcdad308.entities.Excursion;
import com.example.rmcdad308.entities.Vacation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExcursionDetails extends AppCompatActivity {

    String name;
    int excursionId;
    String excursionDate;
    int vacationId;
    EditText editName;
    Button editDate;
    Repository repository;

    DatePickerDialog.OnDateSetListener startDate;

    final Calendar myCalendarStart = Calendar.getInstance();
    Vacation vacation;

    String vStartDate;

    String vEndDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_details);

        repository = new Repository(getApplication());
        name = getIntent().getStringExtra("name");
        editName = findViewById(R.id.excursionname);

        editName.setText(name);
        excursionId = getIntent().getIntExtra("id", -1);
        vacationId = getIntent().getIntExtra("vacationId", -1);
        editDate = findViewById(R.id.excursiondate);
        excursionDate = getIntent().getStringExtra("date");
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editDate.setText(excursionDate);

        ArrayList<Vacation> vacationArrayList = new ArrayList<>();
        vacationArrayList.addAll(repository.getAllVacations());
        ArrayList<Integer> vacationIdList = new ArrayList<>();
        for (Vacation vacation : vacationArrayList) {
            vacationIdList.add(vacation.getVacationId());
        }


        startDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                myCalendarStart.set(Calendar.YEAR, year);
                myCalendarStart.set(Calendar.MONTH, monthOfYear);
                myCalendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabelStart();

            }
        };


        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date;

                String info = editDate.getText().toString();
                if (info.equals("")) info = "09/01/23";
                try {
                    myCalendarStart.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(ExcursionDetails.this, startDate, myCalendarStart
                        .get(Calendar.YEAR), myCalendarStart.get(Calendar.MONTH),
                        myCalendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabelStart() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editDate.setText(sdf.format(myCalendarStart.getTime()));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_excursiondetails, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        if (item.getItemId() == R.id.excursionsave) {
            Vacation ascVacation = repository.getVacationById(vacationId);
            String eDateText = editDate.getText().toString();


            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
            Date edate;

            try {
                edate = sdf.parse(eDateText);


                if (ascVacation != null) {
                    Date vacSD = sdf.parse(ascVacation.getStartDate());
                    Date vacED = sdf.parse(ascVacation.getEndDate());

                    if (edate.after(vacSD) && edate.before(vacED)) {

                        Excursion excursion;

                        if (excursionId == -1) {
                            if (repository.getAllExcursions().isEmpty())
                                excursionId = 1;
                            else
                                excursionId = repository.getAllExcursions().get(repository.getAllExcursions().size() - 1).getExcursionId() + 1;

                            excursion = new Excursion(excursionId, editName.getText().toString(), editDate.getText().toString(), vacationId);
                            repository.insert(excursion);
                            Toast.makeText(this, "Your Activity has been added.", Toast.LENGTH_LONG).show();
                        } else {
                            excursion = new Excursion(excursionId, editName.getText().toString(), editDate.getText().toString(), vacationId);
                            repository.update(excursion);
                            Toast.makeText(this, "Your Activity has been updated.", Toast.LENGTH_LONG).show();
                        }
                        finish();
                        return true;
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }


            Toast.makeText(ExcursionDetails.this, "Your activity must fall between your journey start and end date", Toast.LENGTH_LONG).show();
        }


        //return false;



        if(item.getItemId()== R.id.excursiondelete){
        for (Excursion exc : repository.getAllExcursions()) {
            if (exc.getExcursionId() == excursionId){
                repository.delete(exc);
                Toast.makeText(this,  "Your activity was deleted.", Toast.LENGTH_LONG).show();

            }
        }
        finish();
        return true;
    }
    if(item.getItemId()== R.id.notify) {
        String eDateFromScreen = editDate.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
        try{
            Date exDate = sdf.parse(eDateFromScreen);
            scheduleNotification(this, "Your " + name + " is today: " + excursionDate, exDate.getTime(), 3);
            Toast.makeText(this,  "Your notification has been set.", Toast.LENGTH_LONG).show();
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

}