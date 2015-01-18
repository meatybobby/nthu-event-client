package softwarestudio.douglas.nthu_event.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import softwarestudio.douglas.nthu_event.client.model.Event;
import softwarestudio.douglas.nthu_event.client.service.rest.RestManager;


public class ShowActivity extends Activity {
    private TextView eventName;
    private TextView eventTime;
    private TextView eventPlace;
    private TextView eventContent;
    private TextView numOfPeople;
    private String eventId;

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

        Button joinBtn = (Button) findViewById(R.id.btn_join);
        Button commentBtn = (Button) findViewById(R.id.btn_comment);
        Button bookmarkBtn = (Button) findViewById(R.id.btn_bookmark);

        joinBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                joinEvent();
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
        getEvent();
    }

    private void getEvent(){
        Map<String, String> header = new HashMap<>();

        restMgr.getResource(Event.class, eventId, new RestManager.GetResourceListener<Event>() {
            @Override
            public void onResponse(int code, Map<String, String> headers, Event resource) {
                eventName.setText(resource.getTitle());
                eventPlace.setText(resource.getLocation());
                eventContent.setText(resource.getDescription());
                numOfPeople.setText(resource.getJoinNum());
            }

            @Override
            public void onRedirect(int code, Map<String, String> headers, String url) {

            }

            @Override
            public void onError(String message, Throwable cause, int code, Map<String, String> headers) {

                Toast.makeText(ShowActivity.this, "無法讀取活動",
                        Toast.LENGTH_SHORT).show();
            }
        }, null);
    }

    private void joinEvent(){
        Event event=new Event();
        event.setId(Long.parseLong(eventId));
        restMgr.postUniversal(Event.class,"http://nthu-event-2014.appspot.com/users/join-event",event,new RestManager.PostResourceListener() {
            @Override
            public void onResponse(int code, Map<String, String> headers) {

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
}