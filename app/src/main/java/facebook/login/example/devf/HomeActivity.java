package facebook.login.example.devf;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.LoginButton;
import com.facebook.widget.WebDialog;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


public class HomeActivity extends ActionBarActivity implements LoginButton.OnErrorListener, Session.StatusCallback, View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        obtainKeyHash();

        LoginButton loginButtonFacebook = (LoginButton) findViewById(R.id.buttonLoginFacebook);
        loginButtonFacebook.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday"));
        loginButtonFacebook.setOnErrorListener(this);
        loginButtonFacebook.setSessionStatusCallback(this);

        findViewById(R.id.buttonPefil).setOnClickListener(this);
        findViewById(R.id.buttonRequestJson).setOnClickListener(this);
        findViewById(R.id.buttonShare).setOnClickListener(this);

    }

    private void obtainKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "facebook.login.example.loginfacebookdevf",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("TAG_KEY_HASH", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void call(Session session, SessionState sessionState, Exception e) {

        if (sessionState.isOpened()) {
            Request.newMeRequest(session,
                    new Request.GraphUserCallback() {
                        @Override
                        public void onCompleted(GraphUser user,
                                                Response response) {
                            if (user != null) {
                                Toast.makeText(HomeActivity.this, getString(R.string.welcome) + " " + user.getName(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(HomeActivity.this, R.string.message_error, Toast.LENGTH_SHORT).show();
                                logoutFacebook();
                            }
                        }
                    }).executeAsync();
        } else if (sessionState.isClosed()) {
            Toast.makeText(HomeActivity.this, R.string.message_log_out, Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onError(FacebookException e) {


    }

    @Override
    public void onClick(View v) {

        Session session = Session.getActiveSession();

        if (session != null && session.isOpened()) {

            switch (v.getId()) {
                case R.id.buttonPefil:
                    Intent intentPerfil = new Intent(HomeActivity.this, PefilActivity.class);
                    startActivity(intentPerfil);
                    break;
                case R.id.buttonRequestJson:
                    Intent intentJson = new Intent(HomeActivity.this, JsonActivity.class);
                    startActivity(intentJson);
                    break;
                case R.id.buttonShare:
                    sharedByFacebook();
                    break;
            }

        } else {
            Toast.makeText(HomeActivity.this, R.string.message_not_logged, Toast.LENGTH_SHORT).show();
        }


    }


    private void sharedByFacebook() {

        if (FacebookDialog.canPresentShareDialog(HomeActivity.this, FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
            FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(HomeActivity.this)
                    .setLink(getString(R.string.url_devf))
                    .setPicture(getString(R.string.ur_devf_logo))
                    .setName(getString(R.string.text_devf_share))
                    .build();
            shareDialog.present();

        } else {

            Bundle params = new Bundle();
            params.putString("name", getString(R.string.text_devf_share));
            params.putString("link", getString(R.string.url_devf));
            params.putString("picture", getString(R.string.ur_devf_logo));

            WebDialog feedDialog =
                    new WebDialog.FeedDialogBuilder(HomeActivity.this,
                            Session.getActiveSession(),
                            params).build();

            feedDialog.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            logoutFacebook();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logoutFacebook() {
        Session session = Session.getActiveSession();
        if (session != null) {
            if (!session.isClosed()) {
                session.closeAndClearTokenInformation();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

}
