package softwarestudio.douglas.nthu_event.client;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import softwarestudio.douglas.nthu_event.client.adapter.CommentAdapter;
import softwarestudio.douglas.nthu_event.client.model.Comment;
import softwarestudio.douglas.nthu_event.client.model.Event;
import softwarestudio.douglas.nthu_event.client.service.rest.RestManager;

/**
 * Created by Douglas on 2015/1/17.
 */
public class CommentActivity extends Activity {
    private RestManager restMgr;

    private TextView commentTitle;
    private EditText addCommentEdt;
    private ListView commentListView;
    private Button submitComment;
    private ArrayList<Comment> cmtList = new ArrayList<Comment>();
    private CommentAdapter cmtAdapter;
    private ProgressDialog progressDialog;

    private String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        restMgr =  RestManager.getInstance(getApplication());
        commentTitle = (TextView) findViewById(R.id.comment_eventName);
        addCommentEdt = (EditText)findViewById(R.id.cmtEdt);
        commentListView = (ListView) findViewById(R.id.cmtListView);
        submitComment = (Button) findViewById(R.id.submitBtn);

        progressDialog = new ProgressDialog(CommentActivity.this);
        progressDialog.setMessage(getString(R.string.info_wait));
        progressDialog.setCancelable(false);
        progressDialog.show();

        cmtAdapter =  new CommentAdapter(this, cmtList);
        commentListView.setAdapter(cmtAdapter);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        commentTitle.setText(bundle.getString("eventName") + " 的留言");
        eventId = bundle.getString("eventId");

        getComment();
        submitComment.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                progressDialog = new ProgressDialog(CommentActivity.this);
                progressDialog.setMessage(getString(R.string.info_wait));
                progressDialog.setCancelable(false);
                progressDialog.show();
                postComment();
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle the action bar items pressed
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                //NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_find:
                intent = new Intent(this, FindActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_add:
                intent = new Intent(this, AddActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_user:
                intent = new Intent(this, MyPageActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getComment() {
        Map<String, String> params = new HashMap<String, String>();
        StringBuilder url = new StringBuilder(getString(R.string.rest_server_url));
        url.append("comment/");
        url.append(eventId);
        restMgr.listUniversal(Comment.class, url.toString(), params, new RestManager.ListResourceListener<Comment>() {
            @Override
            public void onResponse(int code, Map<String, String> headers,
                                   List<Comment> resources) {

                cmtList.clear();
                cmtList.addAll(resources);
                setListViewHeightBasedOnChildren(commentListView);
               // Toast.makeText(CommentActivity.this, "成功list所有留言", Toast.LENGTH_SHORT).show();
                CommentActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cmtAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();

                    }
                });

            }

            @Override
            public void onRedirect(int code, Map<String, String> headers, String url) {
                onError(null, null, code, headers);
            }

            @Override
            public void onError(String message, Throwable cause, int code,
                                Map<String, String> headers) {
                Log.d(this.getClass().getSimpleName(), "" + code + ": " + message);
                Toast.makeText(CommentActivity.this, "留言讀取失敗", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }, null);
    }


    private void postComment(){
        Comment comment = new Comment();
        comment.setContent(addCommentEdt.getText().toString());
        StringBuilder url = new StringBuilder(getString(R.string.rest_server_url));
        url.append("comment/");
        url.append(eventId);

        restMgr.postUniversal(Comment.class,url.toString(),comment,new RestManager.PostResourceListener() {
            @Override
            public void onResponse(int code, Map<String, String> headers) {
                setResult(RESULT_OK);
                addCommentEdt.setText("");
                getComment();
                progressDialog.dismiss();
            }

            @Override
            public void onRedirect(int code, Map<String, String> headers, String url) {

            }

            @Override
            public void onError(String message, Throwable cause, int code, Map<String, String> headers) {
                //Toast.makeText(CommentActivity.this, "發佈留言失敗",Toast.LENGTH_SHORT).show();
                setResult(RESULT_CANCELED);
                progressDialog.dismiss();
            }
        },null);
    }
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;

        for (int i = 0; i < listAdapter.getCount() ; i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        totalHeight += 50;
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
