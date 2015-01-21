package softwarestudio.douglas.nthu_event.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Map;

import softwarestudio.douglas.nthu_event.client.model.Event;
import softwarestudio.douglas.nthu_event.client.model.Session;
import softwarestudio.douglas.nthu_event.client.service.rest.RestManager;

public class MainActivity  extends Activity {

    private Button findEventBtn, addEventBtn, myPageBtn;
    private RestManager restMgr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        restMgr =  RestManager.getInstance(getApplication());
        findEventBtn = (Button) findViewById(R.id.findEventBtn);
        addEventBtn = (Button) findViewById(R.id.addEventBtn);
        myPageBtn = (Button) findViewById(R.id.myPageBtn);

        findEventBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                goFindPage();
            }
        });
        addEventBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                goAddPage();
            }
        });
        myPageBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                goMyPage();
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle the action bar items pressed
        Intent intent;
        switch (item.getItemId()) {
           /* case R.id.action_about:
                intent = new Intent(this, FindActivity.class);
                startActivity(intent);
                finish();
                return true;*/
            case R.id.action_logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void logout(){
        restMgr.deleteUniversal(Session.class,getString(R.string.rest_server_url)+"session",new RestManager.DeleteResourceListener() {
            @Override
            public void onResponse(int code, Map<String, String> headers) {
               finish();//登出成功
            }

            @Override
            public void onRedirect(int code, Map<String, String> headers, String url) {
            }

            @Override
            public void onError(String message, Throwable cause, int code, Map<String, String> headers) {
                Toast.makeText(MainActivity.this, "登出失敗", Toast.LENGTH_SHORT).show();

            }
        },null);
    }
    private void goFindPage(){
        Intent intent = new Intent(this, FindActivity.class);
        startActivity(intent);
    }
    private void goAddPage() {
        Intent intent = new Intent(this, AddActivity.class);
        startActivity(intent);
    }
    private void goMyPage() {
        Intent intent = new Intent(this, MyPageActivity.class);
        startActivity(intent);
    }
}
