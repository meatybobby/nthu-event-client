package softwarestudio.douglas.nthu_event.client;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import softwarestudio.douglas.nthu_event.client.adapter.UserListAdapter;
import softwarestudio.douglas.nthu_event.client.model.Comment;
import softwarestudio.douglas.nthu_event.client.model.Event;
import softwarestudio.douglas.nthu_event.client.model.User;
import softwarestudio.douglas.nthu_event.client.service.rest.RestManager;

/**
 * Created by Douglas on 2015/1/17.
 */
public class UserListActivity extends Activity {
    //private RestManager restMgr;


    private ListView userListView;
    private ArrayList<User> usrList = new ArrayList<User>();
    private UserListAdapter usrAdapter;
    private ProgressDialog progressDialog;

    private String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        //restMgr =  RestManager.getInstance(getApplication());
        userListView = (ListView)findViewById(R.id.userListView);

      /*  progressDialog = new ProgressDialog(UserListActivity.this);
        progressDialog.setMessage(getString(R.string.info_wait));
        progressDialog.setCancelable(false);
        progressDialog.show();*/

        usrList.clear();
        usrList.addAll(ShowActivity.joinUserList);

        usrAdapter =  new UserListAdapter(this, usrList);
        userListView.setAdapter(usrAdapter);

        /*Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        eventId = bundle.getString("eventId");*/
        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO 前往單一活動頁面
                String fbId = ShowActivity.joinUserList.get(position).getFbId();
                String url = "http://www.facebook.com/app_scoped_user_id/" + fbId;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
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
}
