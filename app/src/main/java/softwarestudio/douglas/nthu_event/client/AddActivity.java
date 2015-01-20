package softwarestudio.douglas.nthu_event.client;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.Toast;

import org.w3c.dom.Text;

import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import softwarestudio.douglas.nthu_event.client.model.Event;
import softwarestudio.douglas.nthu_event.client.service.rest.RestManager;


public class AddActivity extends Activity {
    private EditText eventNameEdt;
    private EditText eventDateEdt;
    private EditText eventPlaceEdt;
    private EditText eventTimeEdt;
    private EditText eventContentEdt;
    private Button submitBtn;
    private RestManager rstmgr;
    private String[] tag=new String[2];

    private int hour, minute;
    private int year, month, dateOfMonth;

    private ProgressDialog progressDialog;
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

        final Spinner spinner1 = (Spinner) findViewById(R.id.eventCat1);
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
        //hour = UNSET_TIME; minute = UNSET_TIME;
        eventDateEdt.setOnClickListener(pickDateListener);

        /*設定時區*/
        Calendar mcurrentTime = new GregorianCalendar( TimeZone.getTimeZone(getString(R.string.time_zone_id)) );
        hour = mcurrentTime.get(Calendar.HOUR_OF_DAY) ;
        minute = mcurrentTime.get(Calendar.MINUTE);
        eventTimeEdt.setOnClickListener(pickTimeListener);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show progress
                if(checkUnFill()){
                    Toast.makeText(AddActivity.this, "請填寫所有欄位！",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    progressDialog = new ProgressDialog(AddActivity.this);
                    progressDialog.setMessage(getString(R.string.info_wait));
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    Event e=new Event();
                    e.setTitle(eventNameEdt.getText().toString());
                    e.setDescription(eventContentEdt.getText().toString());
                    e.setTime(getTime());
                    e.setLocation(eventPlaceEdt.getText().toString());
                    e.setTag1(tag[0]);
                    e.setTag2(tag[1]);
                    postEvent(e);
                }

            }
        });
    }
    private boolean checkUnFill(){
        if(eventNameEdt.getText().toString().trim().equals(""))
            return true;
        if(eventDateEdt.getText().toString().trim().equals(""))
            return true;
        if(eventTimeEdt.getText().toString().trim().equals(""))
            return true;
        if(eventPlaceEdt.getText().toString().trim().equals(""))
            return true;
        if(eventContentEdt.getText().toString().trim().equals(""))
            return true;

        return false;
    }

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
                                     year = datePicker.getYear();
                                     month = datePicker.getMonth();
                                     dateOfMonth = datePicker.getDayOfMonth();

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
    /**時間選擇，確認、取消按鈕還要再加*/
    private View.OnClickListener pickTimeListener
            = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(AddActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(selectedHour);
                    sb.append(":");
                    sb.append(formatDigit(selectedMinute));
                    eventTimeEdt.setText(sb.toString());
                    hour = selectedHour;
                    minute = selectedMinute;
                }
            }, hour, minute, false);//Yes 24 hour time
            mTimePicker.setTitle("選擇活動時間");
            mTimePicker.show();
        }
    };

    private OnItemSelectedListener spnListener1 =
            new OnItemSelectedListener(){
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int pos, long id) {
                    tag[0] = parent.getItemAtPosition(pos).toString();
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
                    tag[1] = parent.getItemAtPosition(pos).toString();
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
        rstmgr.postResource(Event.class,e,new RestManager.PostResourceListener() {
            @Override
            public void onResponse(int code, Map<String, String> headers) {
                Toast.makeText(AddActivity.this, "活動新增成功！",
                        Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                finish();
            }

            @Override
            public void onRedirect(int code, Map<String, String> headers, String url) {

            }

            @Override
            public void onError(String message, Throwable cause, int code, Map<String, String> headers) {
                if(code==401){
                    Toast.makeText(AddActivity.this, "新增失敗，請先登入帳號",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(AddActivity.this, "錯誤！"+code,
                            Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        },null);
    }
    private long getTime() {
        Calendar calendar=Calendar.getInstance();
        calendar.set(year,month,dateOfMonth,hour,minute);
        return calendar.getTimeInMillis();
    }
}
