package softwarestudio.douglas.nthu_event.client;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.List;

import softwarestudio.douglas.nthu_event.client.model.Event;
import softwarestudio.douglas.nthu_event.client.service.rest.RestManager;


public class AddActivity extends Activity {
    private EditText eventNameEdt;
    private EditText eventDateEdt;
    private EditText eventPlaceEdt;
    private EditText eventTimeEdt;
    private EditText eventContentEdt;
    private Button submitBtn;
    private Button chooseDateBtn;
    private RestManager rstmgr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        eventNameEdt = (EditText) findViewById(R.id.name_txt);
        eventDateEdt = (EditText) findViewById(R.id.date_txt);
        eventTimeEdt = (EditText) findViewById(R.id.time_txt);
        eventPlaceEdt = (EditText) findViewById(R.id.location_txt);
        eventContentEdt = (EditText) findViewById(R.id.content_txt);
        submitBtn = (Button) findViewById(R.id.submitEventBtn);
        rstmgr=RestManager.getInstance(this);
       // chooseDateBtn = (Button) findViewById(R.id.btn_pickDate);


        Spinner spinner1 = (Spinner) findViewById(R.id.eventCat1);
        Spinner spinner2 = (Spinner) findViewById(R.id.eventCat2);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.events_category1, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(spnListener1);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.events_category2, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(spnListener2);

        //chooseDateBtn.setOnClickListener(pickDateListener);
        eventDateEdt.setOnClickListener(pickDateListener);
        eventTimeEdt.setOnClickListener(pickTimeListener);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event e=new Event();
                postEvent(e);
            }
        });
    }

    /**時間選擇，確認、取消按鈕還要再加*/
    private View.OnClickListener pickTimeListener
            = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;

            mTimePicker = new TimePickerDialog(AddActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    eventTimeEdt.setText( selectedHour + ":" + selectedMinute);
                }
            }, hour, minute, false);//Yes 24 hour time
            mTimePicker.setTitle("選擇時間");
            mTimePicker.show();
        }
    };

    private View.OnClickListener pickDateListener
            = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            final DatePicker datePicker = new DatePicker(AddActivity.this);

            new AlertDialog.Builder(AddActivity.this)
                    .setTitle("選擇活動日期")
                    .setView(datePicker)
                    .setPositiveButton("確定" ,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    int year = datePicker.getYear();
                                    int month = datePicker.getMonth();
                                    int dateOfMonth = datePicker.getDayOfMonth();

                                    String dateStr = year + "/"
                                            + formatDigit(month + 1) + "/"
                                            + formatDigit(dateOfMonth);
                                    eventDateEdt.setText(dateStr);
                                }

                            }

                    )
                    .setNegativeButton("取消", null)
                    .show();
        }
    };
    private OnItemSelectedListener spnListener1 =
            new OnItemSelectedListener(){
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int pos, long id) {
                    String str = parent.getItemAtPosition(pos).toString();
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Another interface callback
                }

            };
    private OnItemSelectedListener spnListener2 =
            new OnItemSelectedListener(){
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int pos, long id) {
                    String str = parent.getItemAtPosition(pos).toString();
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Another interface callback
                }

            };
    private static String formatDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }
    private void postEvent(Event e) {

    }
}
