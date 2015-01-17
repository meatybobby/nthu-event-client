package softwarestudio.douglas.nthu_event.client;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class ShowActivity extends Activity {
    private TextView eventName;
    private TextView eventTime;
    private TextView eventPlace;
    private TextView eventContent;
    private TextView numOfPeople;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
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


    }

    private void joinEvent(){

    }
    private void commentEvent(){

    }
    private void bookmarkEvent(){

    }
}