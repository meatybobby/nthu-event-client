package softwarestudio.douglas.nthu_event.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity  extends Activity {

    private Button findEventBtn, addEventBtn, myPageBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
