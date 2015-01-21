package softwarestudio.douglas.nthu_event.client;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Map;

import softwarestudio.douglas.nthu_event.client.model.Session;
import softwarestudio.douglas.nthu_event.client.model.User;
import softwarestudio.douglas.nthu_event.client.service.rest.RestManager;
import softwarestudio.douglas.nthu_event.client.service.rest.facebook.FacebookManager;

public class LoginActivity extends Activity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private FacebookManager fbMgr;
    private RestManager restMgr;

    private ProgressDialog progressDialog;

    private String fbAccessToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        ActionBar ab = getActionBar();
        if (ab != null) {
            ab.hide();
        }

        fbMgr = FacebookManager.getInstance(this);
        restMgr = RestManager.getInstance(this);

        Button loginBtn = (Button) findViewById(R.id.btn_login_with_facebook);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Show progress
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage(getString(R.string.info_wait));
                progressDialog.setCancelable(false);
                progressDialog.show();

                fbLogin();
            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        FacebookManager.getInstance(this).onActivityResult(this, requestCode,
                resultCode, data);
    }

    @Override
    protected void onStop() {
        super.onStop();

        restMgr.cancelAll(TAG);
    }
    private void fbLogin() {

        fbMgr.login(this, new FacebookManager.LoginListener() {
            @Override
            public void onResponse(String accessToken) {

                Log.d(TAG, "FB login success, accessToken: " + fbAccessToken);
                fbAccessToken = accessToken;
                nthuEventLogin();
            }
            @Override
            public void onError(Exception e) {
                Log.e(TAG, e.getMessage());
                Toast.makeText(LoginActivity.this, getString(R.string.info_server_error)+"FB server問題",
                        Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void nthuEventLogin() {
        final Session s = new Session(fbAccessToken);

        Log.d(TAG, "Posting NTHU Event session: " + s);

        restMgr.postResource(Session.class, s, new RestManager.PostResourceListener() {

            @Override
            public void onResponse(int code, Map<String, String> headers) {

                Log.d(TAG, "NTHU Event session created. Code: " + code);

                s.currentSession = s;
                proceedToMain();

            }

            @Override
            public void onRedirect(int code, Map<String, String> headers,
                                   String url) {
                onError(null, null, code, headers);
            }

            @Override
            public void onError(String message, Throwable cause, int code,
                                Map<String, String> headers) {

                // New user
                if (code == 404) {
                    Log.d(TAG, "New user!");
                    proceedToRegister();

                } else {

                    Log.e(TAG, "Cannot create NTHU Event session. Message: " + message, cause);
                    Toast.makeText(LoginActivity.this, "NTHU Event Server 問題 code:"+code,
                            Toast.LENGTH_SHORT).show();

                    progressDialog.dismiss();

                }
            }
        }, TAG);
    }


    private void proceedToMain() {

        progressDialog.dismiss();

        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
       // finish();
    }

    private void proceedToRegister() {
        final User u = new User(fbAccessToken);

        Log.d(TAG, "Posting NTHU Event session: " + u);

        restMgr.postResource(User.class, u, new RestManager.PostResourceListener() {

            @Override
            public void onResponse(int code, Map<String, String> headers) {

                Log.d(TAG, "NTHU Event session created. Code: " + code);

                proceedToMain();

            }

            @Override
            public void onRedirect(int code, Map<String, String> headers,
                                   String url) {
                onError(null, null, code, headers);
            }

            @Override
            public void onError(String message, Throwable cause, int code,
                                Map<String, String> headers) {

                // New user
                if (code == 404) {

                    Log.d(TAG, "New user!");
                    proceedToRegister();

                } else {

                    Log.e(TAG, "Cannot create CampusHunt session. Message: " + message, cause);
                    Toast.makeText(LoginActivity.this, getString(R.string.info_server_error),
                            Toast.LENGTH_SHORT).show();

                    progressDialog.dismiss();

                }
            }
        }, TAG);

    }

}
