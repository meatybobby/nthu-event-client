package softwarestudio.douglas.nthu_event.client.service.rest.facebook;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class FacebookManager {
    private static final String TAG = FacebookManager.class.getSimpleName();
    private static FacebookManager m;

    private Activity app;

    private Session session;

    protected FacebookManager(Activity app) {
        this.app = app;
    }

    public static FacebookManager getInstance(Activity app) {
        if (m == null) {
            m = new FacebookManager(app);
        }
        return m;
    }

    /**
     * @param activity should delegate {@link android.app.Activity#onActivityResult(int, int, android.content.Intent)} to {@link
     *                 #onActivityResult(android.app.Activity, int, int, android.content.Intent)}.
     * @param listener
     * @see <a href="https://developers.facebook.com/docs/android/login-with-facebook/v2.1#permissions"/>
     * for more permissions.
     */
    public void login(Activity activity, final LoginListener listener) {
        Session.openActiveSession(activity, true, Arrays.asList("email", "user_birthday"),
                new Session.StatusCallback() {
                    @Override
                    public void call(final Session session, SessionState state, Exception e) {
                        FacebookManager.this.session = session;
                        if (e instanceof FacebookOperationCanceledException ||
                                e instanceof FacebookAuthorizationException) {
                            Log.e(TAG, "Cannot log into Facebook (possibly canceled by user)", e);
                            listener.onError(e);
                        } else if (session.isOpened()) {
                            List<String> dp = session.getDeclinedPermissions();
                            if (dp != null && dp.size() > 0) {
                                listener.onError(new Exception("User declined permissions: " + dp));
                                Session.NewPermissionsRequest newPermissionsRequest = new Session
                                        .NewPermissionsRequest(app, dp);
                                session.requestNewReadPermissions(newPermissionsRequest);
                            } else {
                                listener.onResponse(session.getAccessToken());
                            }
                        }
                    }
                });
    }

    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        session.onActivityResult(activity, requestCode, resultCode, data);
    }

    public void logout() {
        if (session != null) {
            session.closeAndClearTokenInformation();
        }
    }

    /**
     * Should be called only after {@link #login(Activity, FacebookManager.LoginListener)}
     */

    public String getAccessToken() {
        return session.getAccessToken();
    }

    /**
     * Should be called only after {@link #login(Activity, FacebookManager.LoginListener)}
     */
    public void getMe(final MeListener listener) {
        Request.newMeRequest(session, new Request.GraphUserCallback() {
            // callback after Graph API response with me object
            @Override
            public void onCompleted(GraphUser user, Response response) {
                if (user != null) {
                    JSONObject json = user.getInnerJSONObject();
                    try {
                        Log.d(TAG, "Facebook me: " + json.toString(4));
                        FbUser fbUser = new FbUser(json.getString("id"), json.getString("name"),
                                json.getString("email"), new SimpleDateFormat("MM/dd/yyyy")
                                .parse(json.getString("birthday")).getTime(),
                                json.getString("gender"), json.getString("locale"));
                        listener.onResponse(fbUser);
                    } catch (Exception e) {
                        listener.onError(e.getMessage(), e);
                    }
                } else {
                    listener.onError(response.getRawResponse(), null);
                }
            }
        }).executeAsync();
    }

    private String getKeyHash(Signature signature) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
        } catch (Exception e) {
            // should never happen
            throw new RuntimeException("Error generating key hash", e);
        }
    }

    public interface LoginListener {
        void onResponse(String accessToken);

        void onError(Exception e);
    }


    public interface MeListener {
        void onResponse(FbUser user);

        void onError(String message, Throwable cause);
    }
}
