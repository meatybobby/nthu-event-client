package softwarestudio.douglas.nthu_event.client;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

import softwarestudio.douglas.nthu_event.client.service.rest.RestManager;


public class AddActivity extends Activity {
    private EditText eventNameEdt;
    private EditText eventDateEdt;
    private EditText eventPlaceEdt;
    private EditText eventContentEdt;
    private Button submitBtn;
    private RestManager rstmgr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        eventNameEdt = (EditText) findViewById(R.id.name_txt);
        eventDateEdt = (EditText) findViewById(R.id.date_txt);
        eventPlaceEdt = (EditText) findViewById(R.id.location_txt);
        eventContentEdt = (EditText) findViewById(R.id.content_txt);
        submitBtn = (Button) findViewById(R.id.submitEventBtn);
        rstmgr=RestManager.getInstance(this);

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
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postEvent();
            }
        });
    }


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
    private void postEvent() {

    }
}
