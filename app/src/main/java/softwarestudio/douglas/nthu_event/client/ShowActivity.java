package softwarestudio.douglas.nthu_event.client;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import softwarestudio.douglas.nthu_event.client.adapter.CommentAdapter;
import softwarestudio.douglas.nthu_event.client.model.Event;
import softwarestudio.douglas.nthu_event.client.model.Comment;
import softwarestudio.douglas.nthu_event.client.service.rest.RestManager;


public class ShowActivity extends Activity {

    private static final String TAG = ShowActivity.class.getSimpleName();
    private TextView eventName;
    private TextView eventTime;
    private TextView eventPlace;
    private TextView eventHost;
    private TextView eventContent;
    private TextView numOfPeople;
    private String eventId;
    private Button joinBtn;

    private ListView commentLv;
    private ArrayList<Comment> cmtList = new ArrayList<Comment>();
    private CommentAdapter cmtAdapter;

    private Map<Long,Boolean> userJoinMap;

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
        eventHost = (TextView) findViewById(R.id.event_host);
        eventContent = (TextView) findViewById(R.id.event_content);
        numOfPeople = (TextView) findViewById(R.id.event_peopleNum);

        joinBtn = (Button) findViewById(R.id.btn_join);
        Button commentBtn = (Button) findViewById(R.id.btn_comment);
        //Button bookmarkBtn = (Button) findViewById(R.id.btn_bookmark);

        userJoinMap = new HashMap<Long,Boolean>();

        commentBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                commentEvent();
            }
        });
       /* bookmarkBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                bookmarkEvent();
            }
        });*/
        joinBtn.setOnClickListener(joinListener);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        eventId = bundle.getString("eventId");

        progressDialog = new ProgressDialog(ShowActivity.this);
        progressDialog.setMessage(getString(R.string.info_wait));
        progressDialog.setCancelable(false);
        progressDialog.show();
        loadUserJoin();
        getEvent();

        commentLv = (ListView) findViewById(R.id.list_3comment);
        cmtAdapter =  new CommentAdapter(this, cmtList);
        commentLv.setAdapter(cmtAdapter);
        getComment();
    }
    private Button.OnClickListener joinListener
            = new Button.OnClickListener(){
        @Override
        public void onClick(View view){
            if(joinBtn.getText().toString().equals("參加")){
                joinBtn.setText("退出");
                //Integer.toString
                String curNum = numOfPeople.getText().toString();
                String newNum = Integer.toString(Integer.parseInt(curNum)+1);
                numOfPeople.setText(newNum);
                progressDialog.show();
                joinEvent();
            }
            else{
                joinBtn.setText("參加");
                String curNum = numOfPeople.getText().toString();
                String newNum = Integer.toString(Integer.parseInt(curNum)-1);
                numOfPeople.setText(newNum);
                progressDialog.show();
                exitEvent();
            }
        }
    };
    private void getComment(){
        Map<String, String> params = new HashMap<String, String>();
        StringBuilder url = new StringBuilder(getString(R.string.rest_server_url));
        url.append("comment/");
        url.append(eventId);
        restMgr.listUniversal(Comment.class, url.toString(), params, new RestManager.ListResourceListener<Comment>() {
            @Override
            public void onResponse(int code, Map<String, String> headers,
                                   List<Comment> resources) {

                cmtList.clear();
                int n = resources.size();
                if(n<=3)
                    cmtList.addAll(resources);
                else{
                    for(int i=n-3; i<=n-1; i++)/*最下面的三則留言*/
                        cmtList.add(resources.get(i));
                }

                progressDialog.dismiss();
                //Toast.makeText(ShowActivity.this, "成功list所有留言",Toast.LENGTH_SHORT).show();

                setListViewHeightBasedOnChildren(commentLv);
            }
            @Override
            public void onRedirect(int code, Map<String, String> headers, String url) {
                onError(null, null, code, headers);
            }

            @Override
            public void onError(String message, Throwable cause, int code,
                                Map<String, String> headers) {
                Log.d(this.getClass().getSimpleName(), "" + code + ": " + message);
                Toast.makeText(ShowActivity.this, "留言讀取失敗", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }, null);
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
                eventHost.setText(resource.getPosterName());
                eventContent.setText(resource.getDescription());
                numOfPeople.setText(Integer.toString(resource.getJoinNum()));
                Log.d(TAG, "event got:" + resource.getTitle());
                //Toast.makeText(ShowActivity.this, "順利讀取活動",
                       // Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
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
                progressDialog.dismiss();
            }

            @Override
            public void onRedirect(int code, Map<String, String> headers, String url) {

            }

            @Override
            public void onError(String message, Throwable cause, int code, Map<String, String> headers) {
                Toast.makeText(ShowActivity.this, "參加活動失敗",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        },null);

    }
    private void exitEvent() {
        restMgr.deleteUniversal(Event.class,getString(R.string.rest_server_url)+"users/join-event/"+eventId,new RestManager.DeleteResourceListener() {
            @Override
            public void onResponse(int code, Map<String, String> headers) {
                progressDialog.dismiss();
            }

            @Override
            public void onRedirect(int code, Map<String, String> headers, String url) {
            }

            @Override
            public void onError(String message, Throwable cause, int code, Map<String, String> headers) {
                Toast.makeText(ShowActivity.this, "退出活動失敗",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        },null);
    }
    private void commentEvent(){
        Intent intent = new Intent(this, CommentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("eventName", eventName.getText().toString());
        bundle.putString("eventId", eventId);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            getComment();
        }
    }
    private void bookmarkEvent(){

    }
    private String convertDate(long millis){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/M/d h:mm (a)");
        return formatter.format( new Date(millis) );
    }
    private boolean checkUserJoin(){/*若user有參加該event 回傳true*/
        return userJoinMap.containsKey(Long.parseLong(eventId));
    }
    private void loadUserJoin(){
        Map<String, String> params = new HashMap<String, String>();
        restMgr.listUniversal(Event.class, getString(R.string.rest_server_url)+"users/join-event",params, new RestManager.ListResourceListener<Event>() {
            @Override
            public void onResponse(int code, Map<String, String> headers,
                                   List<Event> resources) {
                //Toast.makeText(ShowActivity.this, "成功list",Toast.LENGTH_SHORT).show();
                for(Event e : resources)
                    userJoinMap.put(e.getId(),true);
                if(checkUserJoin()){
                    joinBtn.setText("退出");
                }else{
                    joinBtn.setText("參加");
                }
                //getEvent();
            }

            @Override
            public void onRedirect(int code, Map<String, String> headers, String url) {
                onError(null, null, code, headers);
            }

            @Override
            public void onError(String message, Throwable cause, int code,
                                Map<String, String> headers) {
                Log.d(this.getClass().getSimpleName(), "" + code + ": " + message);
                Toast.makeText(ShowActivity.this, "無法讀取user參加的活動",Toast.LENGTH_SHORT).show();
            }
        }, null);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        int n = listAdapter.getCount();
        for (int i = 0; i < (n<=3 ? n:3) ; i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}