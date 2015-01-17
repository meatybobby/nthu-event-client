package softwarestudio.douglas.nthu_event.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import softwarestudio.douglas.nthu_event.client.service.rest.RestManager;

/**
 * Created by Douglas on 2015/1/17.
 */
public class CommentActivity extends Activity {
    private RestManager restMgr;

    private EditText addCommentEdt;
    private ListView commentListView;
    private Button submitComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        addCommentEdt = (EditText)findViewById(R.id.cmtEdt);
        commentListView = (ListView) findViewById(R.id.cmtListView);
        submitComment = (Button) findViewById(R.id.submitBtn);


    }
}
