package facebook.login.example.devf;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;

public class PefilActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pefil);

        requestInfoUser();
    }

    private void requestInfoUser() {

        Session session = Session.getActiveSession();
        Request.newMeRequest(session,
                new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user,
                                            Response response) {
                        if (user != null) {
                            showUserInfo(user);
                        } else {
                            Toast.makeText(PefilActivity.this, R.string.message_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                }).executeAsync();
    }

    private void showUserInfo(GraphUser user) {

        ProfilePictureView profilePictureView = (ProfilePictureView) findViewById(R.id.picturePerfil);
        TextView textViewPerfil = (TextView) findViewById(R.id.textViewPerfil);


        profilePictureView.setProfileId(user.getId());

        String fistName = getString(R.string.text_fist_name) + " " + user.getFirstName() + "\n"+ "\n";
        String lastName = getString(R.string.text_last_name) + " " + user.getLastName() + "\n"+ "\n";
        String gender = getString(R.string.text_gender) + " " + user.getProperty("gender") + "\n"+ "\n";
        String email = getString(R.string.text_last_name) + " " + user.getProperty("email") + "\n"+ "\n";
        String birthDay = getString(R.string.text_birthday) + " " + user.getBirthday();
        textViewPerfil.setText(fistName + lastName + gender + email + birthDay);
    }


}
