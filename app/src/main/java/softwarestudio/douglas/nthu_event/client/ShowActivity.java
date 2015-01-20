package softwarestudio.douglas.nthu_event.client;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import softwarestudio.douglas.nthu_event.client.model.Event;
import softwarestudio.douglas.nthu_event.client.service.rest.RestManager;


public class ShowActivity extends Activity {

    private static final String TAG = ShowActivity.class.getSimpleName();
    private TextView eventName;
    private TextView eventTime;
    private TextView eventPlace;
    private TextView eventContent;
    private TextView numOfPeople;
    private String eventId;
    private Button joinBtn;

    private Map<Long,Boolean> userJoinList;

    private ProgressDialog progressDialog;

    private RestManager restMgr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        restMgr =  RestManager.getInstance(getApplication());

        eventName = (TextView) findViewById(R.id.event_name);
        eventTime = (TextView) findViewById(R.id.event_time);
        eventPlace = (TextView) findViewById(R.id.event_place);
        eventContent = (TextView) findViewById(R.id.event_content);
        numOfPeople = (TextView) findViewById(R.id.event_peopleNum);

        joinBtn = (Button) findViewById(R.id.btn_join);
        Button commentBtn = (Button) findViewById(R.id.btn_comment);
        Button bookmarkBtn = (Button) findViewById(R.id.btn_bookmark);

        userJoinList = new HashMap<Long,Boolean>();

        joinBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                progressDialog.show();
                joinEvent();
                /*if(joinBtn.getText().toString().equals("參加")){
                    joinBtn.setText("退出");
                }else{
                    joinBtn.setText("參加");
                }*/
            }
        });
        commentBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                commentEvent();
            }
        });
        bookmarkBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                bookmarkEvent();
            }
        });

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        eventId = bundle.getString("EventId");
        progressDialog = new ProgressDialog(ShowActivity.this);
        progressDialog.setMessage(getString(R.string.info_wait));
        progressDialog.setCancelable(false);
        progressDialog.show();
        loadUserJoin();

    }

    private boolean checkUserJoin(){/*若user有參加該event 回傳true*/
        return userJoinList.containsKey(Long.parseLong(eventId));
    }
    private void getEvent(){
        Map<String, String> header = new HashMap<>();

        restMgr.getResource(Event.class, eventId, new RestManager.GetResourceListener<Event>() {
            @Override
            public void onResponse(int code, Map<String, String> headers, Event resource) {
                resource.getTime();
                eventName.setText(resource.getTitle());
                eventTime.setText(convertDate(resource.getTime()));
                eventPlace.setText(resource.getLocation());
                eventContent.setText(resource.getDescription());
                numOfPeople.setText(Integer.toString(resource.getJoinNum()));
                progressDialog.dismiss();

                if(checkUserJoin()){
                    joinBtn.setText("退出");
                }else{
                    joinBtn.setText("參加");
                }
                Log.d(TAG, "event got:" + resource.getTitle());
            }

            @Override
            public void onRedirect(int code, Map<String, String> headers, String url) {

            }

            @Override
            public void onError(String message, Throwable cause, int code, Map<String, String> headers) {

                Toast.makeText(ShowActivity.this, "無法讀取活動",
                        Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }, null);
    }


    private void joinEvent(){
        Event event=new Event();
        event.setId(Long.parseLong(eventId));
        restMgr.postUniversal(Event.class,getString(R.string.rest_server_url)+"users/join-event",event,new RestManager.PostResourceListener() {
            @Override
            public void onResponse(int code, Map<String, String> headers) {
                getEvent();
            }

            @Override
            public void onRedirect(int code, Map<String, String> headers, String url) {

            }

            @Override
            public void onError(String message, Throwable cause, int code, Map<String, String> headers) {

            }
        },null);

    }
    private void commentEvent(){

    }
    private void bookmarkEvent(){

    }
    private String convertDate(long millis){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd hh:mm (a)");
        return formatter.format( new Date(millis) );
    }

    private void loadUserJoin(){
        Map<String, String> params = new HashMap<String, String>();
        restMgr.listUniversal(Event.class, getString(R.string.rest_server_url)+"users/join-event",params, new RestManager.ListResourceListener<Event>() {
            @Override
            public void onResponse(int code, Map<String, String> headers,
                                   List<Event> resources) {
                Toast.makeText(ShowActivity.this, "成功list",Toast.LENGTH_SHORT).show();
                for(Event e : resources)
                    userJoinList.put(e.getId(),true);
                getEvent();
            }

            @Override
            public void onRedirect(int code, Map<String, String> headers, String url) {
                onError(null, null, code, headers);
            }

            @Override
            public void onError(String message, Throwable cause, int code,
                                Map<String, String> headers) {
                Log.d(this.getClass().getSimpleName(), "" + code + ": " + message);
                Toast.makeText(ShowActivity.this, "無法list",Toast.LENGTH_SHORT).show();
            }
        }, null);
    }
}